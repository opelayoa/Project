package com.tiendas3b.almacen.activities;

import android.content.Intent;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alamkanak.weekview.WeekViewEvent;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.TimetableInfo;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Utilization;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.TimetableInfoDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimetableReceiptActivity extends BaseActivity {//AppCompatActivity

    //    private CalendarView calendarView;
//    private final SimpleDateFormat df = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
    private final SimpleDateFormat df = new SimpleDateFormat("MMMdd", Locale.getDefault());
    private CompactCalendarView compactCalendarView;
    private List<TimetableReceipt> items = Collections.emptyList();
    private IDatabaseManager databaseManager;
    private int currentColor;
    private Date currentDay;
    private boolean shouldShow = false;
    private GlobalState mContext;
    private TimetableInfo timetableInfo;
    private Date yesterday;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable_receipt);
        mContext = (GlobalState) getApplicationContext();
        databaseManager = new DatabaseManager(this);
        onCreateBase();
        currentColor = R.color.event_color_01;
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        CollapsingToolbarLayout toolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
//        toolbarLayout.setTitleEnabled(false);

        setTitle(df.format(new Date()).replace(".", "-").toUpperCase());

        compactCalendarView = (CompactCalendarView) findViewById(R.id.compactcalendar_view);
        compactCalendarView.setLocale(Locale.getDefault());
        compactCalendarView.setDayColumnNames(new String[]{"L", "M", "M", "J", "V", "S", "D"});
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date date) {
                currentDay = date;
//                getDatabaseInfo();
//                if (((items == null || items.isEmpty()) || currentDay.after(yesterday)) && NetworkUtil.isConnected(mContext)) {
                if (NetworkUtil.isConnected(mContext)) {
                    download();
                } else {
                    getDatabaseInfo();
                }
//                }
                hideCalendar();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                setTitle(df.format(firstDayOfNewMonth).replace(".", "-").toUpperCase());
            }
        });
        hideCalendar();


//        calendarView = (CalendarView) findViewById(R.id.calendar_view);
//        calendarView.setFirstDayOfWeek(Calendar.MONDAY);
//        calendarView.setIsOverflowDateVisible(true);
//        calendarView.setCurrentDay(new Date(System.currentTimeMillis()));
//        calendarView.setBackButtonColor(R.color.white);
//        calendarView.setNextButtonColor(R.color.white);
//        calendarView.refreshCalendar(Calendar.getInstance(Locale.getDefault()));
//        calendarView.setOnDateSelectedListener(new CalendarView.OnDateSelectedListener() {
//            @Override
//            public void onDateSelected(@NonNull Date date) {
//                toolbar.setTitle(dfThousnds.format(date));
//                getDatabaseInfo(date);
//            }
//        });
//
//        calendarView.setOnMonthChangedListener(new CalendarView.OnMonthChangedListener() {
//            @Override
//            public void onMonthChanged(@NonNull Date monthDate) {
////                SimpleDateFormat dfThousnds = new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault());
////                if (null != toolbar)
////                    toolbar.setTitle(dfThousnds.format(monthDate));
//            }
//        });

//        DayView dayView = calendarView.findViewByDate(new Date(System.currentTimeMillis()));
//        if(null != dayView)
//            Toast.makeText(getApplicationContext(), "Today is: " + dayView.getText().toString() + "/" + calendarView.getCurrentMonth() + "/" +  calendarView.getCurrentYear(), Toast.LENGTH_SHORT).show();

        init();
    }

    @Override
    protected void onRestart() {
        databaseManager = new DatabaseManager(this);
        items = databaseManager.listTimetable(currentDay, mContext.getRegion());
//        databaseManager.closeDbConnections();
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                compactCalendarView.setVisibility(View.VISIBLE);
            }
        }, 1000);
    }

    @Override
    protected void onStop() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_timetable_receipt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_today:
                hideCalendar();
                return true;
            case R.id.action_info:
                downloadInfo();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void downloadInfo() {
        if (!currentDay.after(yesterday)) {
            Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
            httpService.getTimetableInfo(mContext.getRegion(), new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(currentDay)).enqueue(new Callback<TimetableInfoDTO>() {
                @Override
                public void onResponse(Call<TimetableInfoDTO> call, Response<TimetableInfoDTO> response) {
                    if (response.isSuccessful()) {
                        fillInfo(response.body());
                    } else {
//                    showEmptyView();
                    }
////                swipeRefresh.setRefreshing(false);
                }

                @Override
                public void onFailure(Call<TimetableInfoDTO> call, Throwable t) {
////                swipeRefresh.setRefreshing(false);
//                showEmptyView();
//                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
                }
            });
        } else {
            Toast.makeText(this, "Intente con fecha anterior", Toast.LENGTH_LONG).show();
        }
    }

    private void fillInfo(TimetableInfoDTO timetableInfoDTO) {
        if (timetableInfoDTO == null) {
//            showEmptyView();
        } else {
            saveInfo(timetableInfoDTO);
            showInfo();
//            setAdapter();
//            getWeekView().notifyDatasetChanged();
//            getWeekView().goToHour(7.0d);
//            setTitle(dfThousnds.format(currentDay));

        }
//        viewSwitcher.setDisplayedChild(1);
    }

    private void showInfo() {
        List<Utilization> utilizations = timetableInfo.getUtilizations();
        String des = "";
        for (Utilization u : utilizations) {
            if (u.getNumAnden() == 5) {
                break;
            }
            des += "Uso anden " + u.getNumAnden() + ":  " + u.getUsePercentage() + "%  pallaets:" + u.getPalletsSum() + "\n";
        }
        final Snackbar sb = Snackbar.make(getWeekView(), des, Snackbar.LENGTH_INDEFINITE);
        sb.setAction("Aceptar", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sb.dismiss();
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, mContext.getTheme()));
            sb.setActionTextColor(getResources().getColor(android.R.color.white, mContext.getTheme()));
        } else {
            sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            sb.setActionTextColor(getResources().getColor(android.R.color.white));
        }
        ((TextView) sb.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(12);
        ((TextView) sb.getView().findViewById(android.support.design.R.id.snackbar_text)).setMinLines(5);
        sb.show();
    }

    private void saveInfo(TimetableInfoDTO timetableInfoDTO) {
        List<Utilization> utilizations = timetableInfoDTO.getUtilization();
        Long ttiId = timetableInfo.getId();
        for (Utilization u : utilizations) {
            u.setTimetableInfoId(ttiId);
        }
        databaseManager = DatabaseManager.getInstance(this);
        databaseManager.insertOrReplaceInTx(utilizations.toArray(new Utilization[utilizations.size()]));
//        databaseManager.closeDbConnections();
    }

    private void hideCalendar() {
        if (shouldShow) {
            compactCalendarView.showCalendarWithAnimation();
        } else {
            compactCalendarView.hideCalendar();
        }
        shouldShow = !shouldShow;
    }

    private void init() {
//viewSwitcher
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH, -1);
        yesterday = c.getTime();
        getWeekView().setNumberOfVisibleDays(5);
        if (items == null || items.isEmpty()) {
            try {
                currentDay = DateUtil.getDateWithoutTime(new Date());
//                getDatabaseInfo();
                if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                    download();
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void getDatabaseInfo() {
//        Calendar c = Calendar.getInstance(Locale.getDefault());
////        c.setFirstDayOfWeek(Calendar.MONDAY);
//        c.setTime(date);
//        currentDay = c.get(Calendar.DAY_OF_WEEK) - 1;
//        if(currentDay == 0){
//
//        }else{
        databaseManager = new DatabaseManager(this);
        items = databaseManager.listTimetable(currentDay, mContext.getRegion());
//        databaseManager.closeDbConnections();
        if (items.isEmpty()) {
            //            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
            //                @Override
            //                public boolean canChildScrollUp() {
            //                    return false;
            //                }
            //            });
//            final Snackbar sb = Snackbar.make(getWeekView(), "No hay datos de este día " + dfThousnds.format(currentDay), Snackbar.LENGTH_LONG);
//            sb.setAction(getString(R.string.ok), new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    sb.dismiss();
//                }
//            }).show();
        } else {
            //            setAdapter();
            //            viewSwitcher.setDisplayedChild(1);
//                getWeekView().getMonthChangeListener().onMonthChange(c.get(Calendar.YEAR), c.get(Calendar.MONTH));
            String date = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(currentDay);
            databaseManager = new DatabaseManager(this);
            timetableInfo = databaseManager.getTimetableInfo(mContext.getRegion(), date);
//            databaseManager.closeDbConnections();

            getWeekView().notifyDatasetChanged();
//            getWeekView().goToHour(7.0d);
            setTitle(df.format(currentDay).replace(".", "-").toUpperCase());
        }
//        }
    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getTimetableReceiptCatalog(mContext.getRegion(), new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(currentDay)).enqueue(new Callback<List<TimetableReceipt>>() {
            @Override
            public void onResponse(Call<List<TimetableReceipt>> call, Response<List<TimetableReceipt>> response) {
                if (response.isSuccessful()) {
                    fill(response.body());
                } else {
//                    showEmptyView();
                }
////                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<TimetableReceipt>> call, Throwable t) {
////                swipeRefresh.setRefreshing(false);
//                showEmptyView();
//                Log.e(TAG, mContext.getString(R.string.error_download_receipt_sheets));
            }
        });
    }

    private void fill(List<TimetableReceipt> receiptSheets) {
        if (receiptSheets == null) {
//            showEmptyView();
        } else {
            saveReceiptSheets(receiptSheets);
//            setAdapter();
            getWeekView().notifyDatasetChanged();
//            getWeekView().goToHour(7.0d);
            setTitle(df.format(currentDay).replace(".", "-").toUpperCase());
        }
//        viewSwitcher.setDisplayedChild(1);
    }

    private void saveReceiptSheets(List<TimetableReceipt> list) {
        Long regionId = mContext.getRegion();
        databaseManager = new DatabaseManager(this);
        databaseManager.insertOrReplaceInTx(list.toArray(new TimetableReceipt[list.size()]));
        items = databaseManager.listTimetable(currentDay, regionId);

        String date = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(currentDay);
        TimetableInfo tti = new TimetableInfo();
        tti.setRegionId(regionId);
        tti.setDateTime(date);
        databaseManager.insertOrReplaceInTx(tti);
        timetableInfo = databaseManager.getTimetableInfo(regionId, date);
//        databaseManager.closeDbConnections();
//        this.items = list;
    }

//    private void setAdapter() {
////        tableView = (ReceiptSheetTableView) findViewById(R.id.list);
////        ReceiptSheetTableDataAdapter adapter = new ReceiptSheetTableDataAdapter(this, items);
////        swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
////            @Override
////            public boolean canChildScrollUp() {
////                return tableView.getChildAt(1).canScrollVertically(-1);
////            }
////        });
////        tableView.setDataAdapter(adapter);
////        tableView.addDataClickListener(new ClickListener());
////        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
////        viewSwitcher.findViewById(R.id.scrollTable).setVisibility(View.VISIBLE);
//    }

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        List<WeekViewEvent> events = new ArrayList<>();
        if (!items.isEmpty()) {
            Date today = null;
            try {
                today = DateUtil.getDate(new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(new Date()));
                Date dateInfo = DateUtil.getDate(items.get(0).getDateTime());
                boolean nextColor = false;
                if (dateInfo.after(today)) {
                    nextColor = true;
                }
                for (TimetableReceipt tr : items) {
                    Integer platform = tr.getPlatform();
                    if (platform != null)
                        switch (platform) {
                            case 1:
                                events.add(newEvent(0, tr, newMonth, newYear, nextColor));
                                break;
                            case 2:
                                events.add(newEvent(1, tr, newMonth, newYear, nextColor));
                                break;
                            case 3:
                                events.add(newEvent(2, tr, newMonth, newYear, nextColor));
                                break;
                            case 4:
                                events.add(newEvent(3, tr, newMonth, newYear, nextColor));
                                break;
                            case 5:
//                                if (mContext.getRegion() != 1000L) {
//                                if (getWeekView().getNumberOfVisibleDays() < 5) {
//                                    getWeekView().setNumberOfVisibleDays(5);
//                                }
                                events.add(newEvent(4, tr, newMonth, newYear, nextColor));
//                                }
                                break;
                            case 6:
                                if (getWeekView().getNumberOfVisibleDays() < 6) {
                                    getWeekView().setNumberOfVisibleDays(6);
                                }
                                events.add(newEvent(5, tr, newMonth, newYear, nextColor));
                                break;
                            case 7:
                                if (getWeekView().getNumberOfVisibleDays() < 7) {
                                    getWeekView().setNumberOfVisibleDays(7);
                                }
                                events.add(newEvent(6, tr, newMonth, newYear, nextColor));
                                break;
                            case 8:
                                if (getWeekView().getNumberOfVisibleDays() < 8) {
                                    getWeekView().setNumberOfVisibleDays(8);
                                }
                                events.add(newEvent(7, tr, newMonth, newYear, nextColor));
                                break;
                        }
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
//        getWeekView().goToHour(6.0d);

        return events;
    }

    private WeekViewEvent newEvent(int platform, TimetableReceipt tr, int newMonth, int newYear, boolean nextColor) {//platform -1
        String[] time = tr.getDeliveryTime().split(":");
        Calendar startTime = Calendar.getInstance();
        startTime.set(Calendar.YEAR, newYear);
        startTime.set(Calendar.MONTH, newMonth - 1);
        startTime.add(Calendar.DAY_OF_MONTH, platform);
        startTime.set(Calendar.HOUR_OF_DAY, Integer.valueOf(time[0]) - 6);//fix for init time
        startTime.set(Calendar.MINUTE, Integer.valueOf(time[1]));
        startTime.set(Calendar.SECOND, 0);
        startTime.set(Calendar.MILLISECOND, 0);

        String[] deliverytime = tr.getDownloadTime().split(":");
        Calendar endTime = (Calendar) startTime.clone();
        int deliveryHour = Integer.valueOf(deliverytime[0]);
//        endTime.set(Calendar.MONTH, newMonth - 1);
        endTime.add(Calendar.HOUR_OF_DAY, deliveryHour > 10 ? 0 : deliveryHour);
        endTime.add(Calendar.MINUTE, Integer.valueOf(deliverytime[1]));
        endTime.set(Calendar.SECOND, 0);
        endTime.set(Calendar.MILLISECOND, 0);
        Provider provider = tr.getProvider();
        String providerStr = provider == null ? "N/A" : provider.getName();

        WeekViewEvent event = new WeekViewEvent(tr.getId(), getEventTitle(startTime, providerStr, endTime, tr.getFolio()), startTime, endTime);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (nextColor) {
                event.setColor(getResources().getColor(getNextColor(), getTheme()));
            } else {
                event.setColor(getResources().getColor(getColor(tr), getTheme()));
            }
        } else {
            if (nextColor) {
                event.setColor(getResources().getColor(getNextColor()));
            } else {
                event.setColor(getResources().getColor(getColor(tr)));
            }
        }
        return event;
    }

    private int getColor(TimetableReceipt timetableReceipt) {
        if (timetableReceipt.getArrive()) {
            String timeStr = timetableReceipt.getTime();
            if ("00:00".equals(timeStr)) {
                return R.color.timetable_without_date;
            } else {
                Date arriveTime = null;
                try {
                    arriveTime = DateUtil.getDate(timetableReceipt.getArriveTime(), DateUtil.HH_MM);
                    Date time = DateUtil.getDate(timetableReceipt.getTime(), DateUtil.HH_MM);
                    if (arriveTime.after(time)) {
                        return R.color.timetable_late;
                    } else {
                        Date deliveryTime = DateUtil.getDate(timetableReceipt.getDeliveryTime(), DateUtil.HH_MM);
                        if (deliveryTime.after(time)) {
                            return R.color.timetable_arrive_on_time_receipt_late;
                        } else {
                            return R.color.timetable_arrive_on_time;
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return -1;
                }
            }
        } else {
            return R.color.timetable_not_arrive;
        }
    }

    private int getNextColor() {

        switch (currentColor) {
            case R.color.event_color_01:
                currentColor = R.color.event_color_02;
                break;
            case R.color.event_color_02:
                currentColor = R.color.event_color_03;
                break;
            case R.color.event_color_03:
                currentColor = R.color.event_color_04;
                break;
            case R.color.event_color_04:
                currentColor = R.color.event_color_01;
                break;
        }

        return currentColor;
    }

    @Override
    public void onEventClick(final WeekViewEvent event, RectF eventRect) {
        try {
            Date dateWithoutTime = DateUtil.getDateWithoutTime(new Date());
            if (/*currentDay.before(dateWiyhoutTime)
                    ||*/ currentDay.compareTo(dateWithoutTime) <= 0) {
                TimetableReceipt timeTable = null;
                for (TimetableReceipt tr : items) {
                    if (tr.getId() == event.getId()) {
                        timeTable = tr;
                        break;
                    }
                }
                if (timeTable.getArrive()) {
                    Intent i = new Intent(this, ReceiptSheetDetailActivity.class);
                    i.putExtra(Constants.EXTRA_RECEIP_SHEET, timeTable.getId());
                    i.putExtra(Constants.EXTRA_PROVIDER_NAME, timeTable.getProvider().getName());
                    startActivity(i);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onEventLongPress(WeekViewEvent event, RectF eventRect) {
//        super.onEventLongPress(event, eventRect);
        long buyId = databaseManager.findBuyByTimetableId(event.getId(), mContext.getRegion());
        if (buyId == -1) {
            //descargar odc e insertarla en bd
            startActivity(new Intent(mContext, ODCsActivity.class));
        } else {
            Intent intent = new Intent(mContext, BuyDetailActivity.class);
            intent.putExtra(Constants.EXTRA_BUY, buyId);
            startActivity(intent);
        }
    }


}
