package com.tiendas3b.almacen.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.dao.Activity;
import com.tiendas3b.almacen.db.dao.Area;
import com.tiendas3b.almacen.db.dao.ArticleDecrease;
import com.tiendas3b.almacen.db.dao.ArticleState;
import com.tiendas3b.almacen.db.dao.ArticleTransference;
import com.tiendas3b.almacen.db.dao.Barcode;
import com.tiendas3b.almacen.db.dao.Check;
import com.tiendas3b.almacen.db.dao.ComplaintCat;
import com.tiendas3b.almacen.db.dao.ConfigProvider;
import com.tiendas3b.almacen.db.dao.DateType;
import com.tiendas3b.almacen.db.dao.Driver;
import com.tiendas3b.almacen.db.dao.EPackage;
import com.tiendas3b.almacen.db.dao.EpackageTruck;
import com.tiendas3b.almacen.db.dao.ExpressProvider;
import com.tiendas3b.almacen.db.dao.InputTransference;
import com.tiendas3b.almacen.db.dao.Level;
import com.tiendas3b.almacen.db.dao.ObservationType;
import com.tiendas3b.almacen.db.dao.OrderType;
import com.tiendas3b.almacen.db.dao.Position;
import com.tiendas3b.almacen.db.dao.PositionPackage;
import com.tiendas3b.almacen.db.dao.Provider;
import com.tiendas3b.almacen.db.dao.ProviderEpackage;
import com.tiendas3b.almacen.db.dao.Rack;
import com.tiendas3b.almacen.db.dao.RackLevel;
import com.tiendas3b.almacen.db.dao.Region;
import com.tiendas3b.almacen.db.dao.Store;
import com.tiendas3b.almacen.db.dao.TimetablePicking;
import com.tiendas3b.almacen.db.dao.TimetableReceipt;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.dao.Unit;
import com.tiendas3b.almacen.db.dao.VArticle;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.epackage.PositionPackageDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.Constants;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.Preferences;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SyncCatalogsServices extends IntentService {
    private static final String TAG = "SyncCatalogsServices";
    private Tiendas3bClient httpService;
    private IDatabaseManager databaseManager;
    private CountDownLatch doneSignal;
    private static final int TOTAL = 25;
    private GlobalState mContext;
    private long regionId;

    public SyncCatalogsServices() {
        super("SyncCatalogsServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            mContext = (GlobalState) getApplicationContext();
            regionId = mContext.getRegion();
            databaseManager = new DatabaseManager(mContext);
            httpService = mContext.getHttpServiceWithAuthTime();

            doneSignal = new CountDownLatch(TOTAL);//num catalogs
//            downloadTimetableReceiptCat(mContext);
            downloadActivityCat();
            downloadUnitCat();
            downloadObservationsTypeCat();
            downloadDriverCat();
            downloadTruckCat();
            downloadCheckCat();
            downloadLevelCat();
            downloadArticleStateCat();
            downloadOrderType();
            downloadDateTypeCat();
            downloadTimetablePicking();
            downloadComplaintCat();
            downloadRegionCat();
            downloadMmpConfigProviderCat();
            downloadExpressProvidersCat();
            downloadStoreCat();
            downloadArticleDecreaseViewCat();
            downloadArticleTransferenceViewCat();
            downloadProviderCat();
            downloadBarcodesCat();
            downloadViewArticleCat();

            downloadAreasCat();
            downloadEpackage();
            downloadEpackageTruckCat();

            downloadEPackageProviders();
//            downloadInputTransferenceCat();

            try {
                doneSignal.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void downloadMmpConfigProviderCat() {
        Call<List<ConfigProvider>> call = httpService.getConfigProviders();
        call.enqueue(new Callback<List<ConfigProvider>>() {
            @Override
            public void onResponse(Call<List<ConfigProvider>> call, Response<List<ConfigProvider>> response) {
                if (response.isSuccessful()) {
                    List<ConfigProvider> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.truncate(ConfigProvider.class);
                        databaseManager.insertOrReplaceInTx(list.toArray(new ConfigProvider[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ConfigProvider>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    private void downloadExpressProvidersCat() {
        Call<List<ExpressProvider>> call = httpService.getExpressProviders();
        call.enqueue(new Callback<List<ExpressProvider>>() {
            @Override
            public void onResponse(Call<List<ExpressProvider>> call, Response<List<ExpressProvider>> response) {
                if (response.isSuccessful()) {
                    List<ExpressProvider> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.truncate(ExpressProvider.class);
                        databaseManager.insertOrReplaceInTx(list.toArray(new ExpressProvider[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ExpressProvider>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    private void downloadOrderType() {
        Call<List<OrderType>> call = httpService.getOrderTypes();
        call.enqueue(new Callback<List<OrderType>>() {
            @Override
            public void onResponse(Call<List<OrderType>> call, Response<List<OrderType>> response) {
                if (response.isSuccessful()) {
                    List<OrderType> list = response.body();
                    if(list != null && !list.isEmpty()) {
//                        databaseManager.truncate(ProviderEpackage.class);
                        databaseManager.insertOrReplaceInTx(list.toArray(new OrderType[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<OrderType>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    /*
    private void downloadEpackageProviderCat() {
        Call<List<ProviderEpackage>> call = httpService.getProviderEpackageCatalog();
        call.enqueue(new Callback<List<ProviderEpackage>>() {
            @Override
            public void onResponse(Call<List<ProviderEpackage>> call, Response<List<ProviderEpackage>> response) {
                if (response.isSuccessful()) {
                    List<ProviderEpackage> providers = response.body();
                    if(providers != null && !providers.isEmpty()) {
                        databaseManager.truncate(ProviderEpackage.class);
                        databaseManager.insertOrReplaceInTx(providers.toArray(new ProviderEpackage[providers.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ProviderEpackage>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }
*/

    protected void downloadEPackageProviders() {
        Call<List<ProviderEpackage>> call = httpService.getProvidersEpackageCat();
        call.enqueue(new Callback<List<ProviderEpackage>>() {
            @Override
            public void onResponse(Call<List<ProviderEpackage>> call, Response<List<ProviderEpackage>> response) {
                if (response.isSuccessful()) {
                    List<ProviderEpackage> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.insertOrReplaceInTx(list.toArray(new ProviderEpackage[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ProviderEpackage>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    private void downloadAreasCat() {
        Call<List<Area>> call = httpService.getAreas();
        call.enqueue(new Callback<List<Area>>() {
            @Override
            public void onResponse(Call<List<Area>> call, Response<List<Area>> response) {
                if (response.isSuccessful()) {
                    List<Area> areas = response.body();
                    if(areas != null && !areas.isEmpty()) {
                        databaseManager.truncate(Position.class);
                        databaseManager.truncate(RackLevel.class);
                        databaseManager.truncate(Rack.class);
                        databaseManager.truncate(Area.class);
                        databaseManager.insertOrReplaceInTx(areas.toArray(new Area[areas.size()]));
                        for (Area area : areas) {
                            List<Rack> racks = area.getRacks();
                            databaseManager.insertOrReplaceInTx(racks.toArray(new Rack[racks.size()]));
                            for (Rack rack : racks) {
                                rack.setAreaId(area.getId());
                                List<RackLevel> levels = rack.getLevels();
                                databaseManager.insertOrReplaceInTx(levels.toArray(new RackLevel[levels.size()]));
                                for (RackLevel level : levels) {
                                    level.setRackId(rack.getId());
                                    List<Position> positions = level.getPositions();
                                    databaseManager.insertOrReplaceInTx(positions.toArray(new Position[positions.size()]));
                                    for (Position position : positions) {
                                        position.setLevelId(level.getId());
                                    }
                                    databaseManager.insertOrReplaceInTx(positions.toArray(new Position[positions.size()]));
                                }
                                databaseManager.insertOrReplaceInTx(levels.toArray(new RackLevel[levels.size()]));
                            }
                            databaseManager.insertOrReplaceInTx(racks.toArray(new Rack[racks.size()]));
                        }
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Area>> call, Throwable t) {
                doneSignal.countDown();
            }
        });
    }

    private void downloadTimetablePicking() {
//        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<TimetablePicking>> call = httpService.getTimetablePicking(regionId);
        call.enqueue(new Callback<List<TimetablePicking>>() {
            @Override
            public void onResponse(Call<List<TimetablePicking>> call, Response<List<TimetablePicking>> response) {
                if (response.isSuccessful()) {
                    List<TimetablePicking> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new TimetablePicking[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<TimetablePicking>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadArticleStateCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ArticleState>> call = httpService.getArticleStates();
        call.enqueue(new Callback<List<ArticleState>>() {
            @Override
            public void onResponse(Call<List<ArticleState>> call, Response<List<ArticleState>> response) {
                if (response.isSuccessful()) {
                    List<ArticleState> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new ArticleState[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ArticleState>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadCheckCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Check>> call = httpService.getChecks();
        call.enqueue(new Callback<List<Check>>() {
            @Override
            public void onResponse(Call<List<Check>> call, Response<List<Check>> response) {
                if (response.isSuccessful()) {
                    List<Check> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.insertOrReplaceInTx(list.toArray(new Check[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Check>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadComplaintCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ComplaintCat>> call = httpService.getComplaints();
        call.enqueue(new Callback<List<ComplaintCat>>() {
            @Override
            public void onResponse(Call<List<ComplaintCat>> call, Response<List<ComplaintCat>> response) {
                if (response.isSuccessful()) {
                    List<ComplaintCat> list = response.body();
                    ComplaintCat c = new ComplaintCat();
                    c.setId(-1L);
                    c.setDescription("---");
                    c.setActive(true);
                    c.setGroup(null);
                    c.setIdStr("NIN");
                    c.setType(14);
                    list.add(c);
                    databaseManager.insertOrReplaceInTx(list.toArray(new ComplaintCat[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ComplaintCat>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadDateTypeCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<DateType>> call = httpService.getDateTypes();
        call.enqueue(new Callback<List<DateType>>() {
            @Override
            public void onResponse(Call<List<DateType>> call, Response<List<DateType>> response) {
                databaseManager.truncate(DateType.class);
                if (response.isSuccessful()) {
                    List<DateType> list = response.body();
                    if(list != null && !list.isEmpty()) {

//                        for (int i = 0; i < list.size(); i++) {
//                            list.get(i).setId((long) i);
//                        }

                        databaseManager.insertOrReplaceInTx(list.toArray(new DateType[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<DateType>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadLevelCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Level>> call = httpService.getLevels();
        call.enqueue(new Callback<List<Level>>() {
            @Override
            public void onResponse(Call<List<Level>> call, Response<List<Level>> response) {
                if (response.isSuccessful()) {
                    List<Level> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.insertOrReplaceInTx(list.toArray(new Level[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Level>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadDriverCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Driver>> call = httpService.getDrivers();
        call.enqueue(new Callback<List<Driver>>() {
            @Override
            public void onResponse(Call<List<Driver>> call, Response<List<Driver>> response) {
                if (response.isSuccessful()) {
                    List<Driver> list = response.body();
                    if(list != null && !list.isEmpty()) {
                        databaseManager.insertOrReplaceInTx(list.toArray(new Driver[list.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Driver>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadTruckCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Truck>> call = httpService.getTrucks();
        call.enqueue(new Callback<List<Truck>>() {
            @Override
            public void onResponse(Call<List<Truck>> call, Response<List<Truck>> response) {
                if (response.isSuccessful()) {
                    List<Truck> list = response.body();
                    databaseManager.insertOrReplaceInTx(list.toArray(new Truck[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Truck>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadActivityCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Activity>> call = httpService.getActivities();
        call.enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                if (response.isSuccessful()) {
                    List<Activity> list = response.body();
                    databaseManager.truncate(Activity.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new Activity[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadUnitCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Unit>> call = httpService.getUnits();
        call.enqueue(new Callback<List<Unit>>() {
            @Override
            public void onResponse(Call<List<Unit>> call, Response<List<Unit>> response) {
                if (response.isSuccessful()) {
                    List<Unit> list = response.body();
                    databaseManager.truncate(Unit.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new Unit[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Unit>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadViewArticleCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<VArticle>> call = httpService.getViewArticles();
        call.enqueue(new Callback<List<VArticle>>() {
            @Override
            public void onResponse(Call<List<VArticle>> call, Response<List<VArticle>> response) {
                if (response.isSuccessful()) {
                    List<VArticle> list = response.body();

                    databaseManager.truncate(VArticle.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new VArticle[list.size()]));
                    Preferences p = new Preferences(mContext);
                    p.setSharedStringSafe(Preferences.KEY_LAST_UPDATE, new SimpleDateFormat(DateUtil.DD_MM_YYYY_HH_MM, Locale.getDefault()).format(new Date()));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<VArticle>> call, Throwable t) {
                Log.e(TAG, "downloadViewArticleCat!");
                doneSignal.countDown();
            }
        });
    }

    private void downloadInputTransferenceCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<InputTransference>> call = httpService.getInputTransference(regionId);
        call.enqueue(new Callback<List<InputTransference>>() {
            @Override
            public void onResponse(Call<List<InputTransference>> call, Response<List<InputTransference>> response) {
                if (response.isSuccessful()) {
                    List<InputTransference> list = response.body();
                    databaseManager.truncate(InputTransference.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new InputTransference[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<InputTransference>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadObservationsTypeCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ObservationType>> call = httpService.getObservationsType();
        call.enqueue(new Callback<List<ObservationType>>() {
            @Override
            public void onResponse(Call<List<ObservationType>> call, Response<List<ObservationType>> response) {
                if (response.isSuccessful()) {
                    List<ObservationType> list = response.body();
                    databaseManager.truncate(ObservationType.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new ObservationType[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ObservationType>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_stores));
                doneSignal.countDown();
            }
        });
    }

    private void downloadStoreCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<Store>> call = httpService.getStores(regionId);
        call.enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.isSuccessful()) {
                    List<Store> stores = response.body();
                    if (stores != null) {
                        final long region = regionId;
                        stores.add(createRegion(region));
                        stores.add(createDieselStation(region));
                        databaseManager.truncate(Store.class);
                        databaseManager.insertOrReplaceInTx(stores.toArray(new Store[stores.size()]));
                    }
                }
                doneSignal.countDown();
            }

            private Store createDieselStation(long region) {
                Store s = new Store();
                s.setId(Constants.DIESEL_ID);
                s.setRegionId(region);
                s.setName("Diesel");
                return s;
            }

            private Store createRegion(long region) {
                Store s = new Store();
                s.setId(region);
                s.setRegionId(region);
                s.setName(String.valueOf(region));
                return s;
            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_stores));
                doneSignal.countDown();
            }
        });
    }

    private void downloadArticleDecreaseViewCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ArticleDecrease>> call = httpService.getViewArticlesDecrease(regionId);
        call.enqueue(new Callback<List<ArticleDecrease>>() {
            @Override
            public void onResponse(Call<List<ArticleDecrease>> call, Response<List<ArticleDecrease>> response) {
                if (response.isSuccessful()) {
                    List<ArticleDecrease> items = response.body();
                    databaseManager.truncate(ArticleDecrease.class);
                    if (items != null) {
                        databaseManager.insertOrReplaceInTx(items.toArray(new ArticleDecrease[items.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ArticleDecrease>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_articles_view));
                doneSignal.countDown();
            }
        });
    }

    private void downloadArticleTransferenceViewCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<ArticleTransference>> call = httpService.getViewArticlesTransference(regionId);
        call.enqueue(new Callback<List<ArticleTransference>>() {
            @Override
            public void onResponse(Call<List<ArticleTransference>> call, Response<List<ArticleTransference>> response) {
                if (response.isSuccessful()) {
                    List<ArticleTransference> items = response.body();
                    if (items != null) {
                        databaseManager.truncate(ArticleTransference.class);
                        databaseManager.insertOrReplaceInTx(items.toArray(new ArticleTransference[items.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<ArticleTransference>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_articles_transference_view));
                doneSignal.countDown();
            }
        });
    }

    private void downloadTimetableReceiptCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<TimetableReceipt>> call = httpService.getTimetableReceiptCatalog(regionId);
        call.enqueue(new Callback<List<TimetableReceipt>>() {
            @Override
            public void onResponse(Call<List<TimetableReceipt>> call, Response<List<TimetableReceipt>> response) {
                if (response.isSuccessful()) {
                    List<TimetableReceipt> providers = response.body();
                    if (providers != null) {
                        databaseManager.truncate(TimetableReceipt.class);
                        databaseManager.insertOrReplaceInTx(providers.toArray(new TimetableReceipt[providers.size()]));
                    }
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<TimetableReceipt>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_timetable_receipt));
                doneSignal.countDown();
            }
        });
    }

    @Override
    public void onDestroy() {
        databaseManager.closeDbConnections();
        super.onDestroy();
    }

    private void downloadProviderCat() {
        Call<List<Provider>> call = httpService.getProviderCatalog();
        call.enqueue(new Callback<List<Provider>>() {
            @Override
            public void onResponse(Call<List<Provider>> call, Response<List<Provider>> response) {
                if (response.isSuccessful()) {
                    List<Provider> providers = response.body();
                    databaseManager.truncate(Provider.class);
                    databaseManager.insertOrReplaceInTx(providers.toArray(new Provider[providers.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Provider>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_provider));
                doneSignal.countDown();
            }
        });
    }

    private void downloadRegionCat() {
        Call<List<Region>> call = httpService.getRegionCatalog();
        call.enqueue(new Callback<List<Region>>() {
            @Override
            public void onResponse(Call<List<Region>> call, Response<List<Region>> response) {
                if (response.isSuccessful()) {
                    List<Region> entities = response.body();
                    databaseManager.truncate(Region.class);
                    databaseManager.insertOrReplaceInTx(entities.toArray(new Region[entities.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Region>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_region));
                doneSignal.countDown();
            }
        });
    }

    private void downloadBarcodesCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTimeGsonTime();
        Call<List<Barcode>> call = httpService.getBarcodeCatalog();
        call.enqueue(new Callback<List<Barcode>>() {
            @Override
            public void onResponse(Call<List<Barcode>> call, Response<List<Barcode>> response) {
                if (response.isSuccessful()) {
                    System.out.println("Barcode");
                    List<Barcode> entities = response.body();
                    databaseManager.truncate(Barcode.class);
                    databaseManager.insertOrReplaceInTx(entities.toArray(new Barcode[entities.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<Barcode>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_barcode));
                doneSignal.countDown();
            }
        });
    }

    private void downloadEpackage() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<EPackage>> call = httpService.getEpackages(regionId);
        call.enqueue(new Callback<List<EPackage>>() {
            @Override
            public void onResponse(Call<List<EPackage>> call, Response<List<EPackage>> response) {
                if (response.isSuccessful()) {
                    List<EPackage> list = response.body();
                    databaseManager.truncate(EPackage.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new EPackage[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<EPackage>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

    private void downloadEpackageTruckCat() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuthTime();
        Call<List<EpackageTruck>> call = httpService.getEpackageTrucks();
        call.enqueue(new Callback<List<EpackageTruck>>() {
            @Override
            public void onResponse(Call<List<EpackageTruck>> call, Response<List<EpackageTruck>> response) {
                if (response.isSuccessful()) {
                    List<EpackageTruck> list = response.body();
                    databaseManager.truncate(EpackageTruck.class);
                    databaseManager.insertOrReplaceInTx(list.toArray(new EpackageTruck[list.size()]));
                }
                doneSignal.countDown();
            }

            @Override
            public void onFailure(Call<List<EpackageTruck>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_sync_input_transference));
                doneSignal.countDown();
            }
        });
    }

}
