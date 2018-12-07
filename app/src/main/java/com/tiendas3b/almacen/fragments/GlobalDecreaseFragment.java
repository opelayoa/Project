package com.tiendas3b.almacen.fragments;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ViewSwitcher;

import com.androidplot.Region;
import com.androidplot.ui.HorizontalPositioning;
import com.androidplot.ui.SeriesAndFormatter;
import com.androidplot.ui.SeriesRenderer;
import com.androidplot.ui.Size;
import com.androidplot.ui.SizeMode;
import com.androidplot.ui.TextOrientation;
import com.androidplot.ui.VerticalPositioning;
import com.androidplot.ui.widget.TextLabelWidget;
import com.androidplot.util.PixelUtils;
import com.androidplot.util.SeriesUtils;
import com.androidplot.xy.BarFormatter;
import com.androidplot.xy.BarRenderer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYSeriesFormatter;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.dto.GlobalDecreaseDTO;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.NetworkUtil;
import com.tiendas3b.almacen.util.NumbersUtil;

import java.text.DateFormatSymbols;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class GlobalDecreaseFragment extends Fragment {

    private static final String TAG = GlobalDecreaseFragment.class.getSimpleName();
    private GlobalState mContext;
//    private XYPlot plot;
    private List<GlobalDecreaseDTO> items;
    private IDatabaseManager databaseManager;
    private ViewSwitcher viewSwitcher;
    //    private GeneralSwipeRefreshLayout swipeRefresh;
    private String mDate;
//    private List<Float> series1Numbers;
//    private List<Float> series2Numbers;
//    private List<Integer> seriesDates;
    private Float max = .0f;

    public GlobalDecreaseFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = (GlobalState) getActivity().getApplicationContext();
        View rootView = inflater.inflate(R.layout.fragment_decrease, container, false);
        if (rootView instanceof ViewSwitcher) {
            viewSwitcher = ((ViewSwitcher) rootView);
            Animation slide_in_left = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            Animation slide_out_right = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_out_right);
            viewSwitcher.setInAnimation(slide_in_left);
            viewSwitcher.setOutAnimation(slide_out_right);
        }
        databaseManager = new DatabaseManager(getActivity());
        init(rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        items = null;
        if (databaseManager != null) databaseManager.closeDbConnections();
        super.onDestroyView();
    }

    private void init(View rootView) {
        Date date = new Date();
        mDate = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault()).format(date);
        plot = (XYPlot) rootView.findViewById(R.id.plot);
//        swipeRefresh = (GeneralSwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
//        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                downloadStatus();
//            }
//        });
//        if (items == null) {
//            getDatabaseInfo();
            if ((items == null || items.isEmpty()) && NetworkUtil.isConnected(mContext)) {
                download();
            }
//        }
    }

    private void showEmptyView() {
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.VISIBLE);
        viewSwitcher.findViewById(R.id.plot).setVisibility(View.GONE);
        viewSwitcher.setDisplayedChild(1);
    }

//    private void getDatabaseInfo() {
//        items = databaseManager.listGlobalDecrease(mDate, mContext.getRegion());
//        if (items.isEmpty()) {
////            swipeRefresh.setOnChildScrollUpListener(new GeneralSwipeRefreshLayout.OnChildScrollUpListener() {
////                @Override
////                public boolean canChildScrollUp() {
////                    return false;
////                }
////            });
//        } else {
//            setAdapter();
////            viewSwitcher.setDisplayedChild(1);
//        }
//    }

    private void download() {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getDecrease2(mContext.getRegion()).enqueue(new Callback<List<GlobalDecreaseDTO>>() {
            @Override
            public void onResponse(Call<List<GlobalDecreaseDTO>> call, Response<List<GlobalDecreaseDTO>> response) {
                if (response.isSuccessful()) {
                    fillList(response.body());
                } else {
                    showEmptyView();
                }
//                swipeRefresh.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<List<GlobalDecreaseDTO>> call, Throwable t) {
//                swipeRefresh.setRefreshing(false);
                showEmptyView();
                Log.e(TAG, mContext.getString(R.string.error_odc));
            }
        });

    }

    private void fillList(List<GlobalDecreaseDTO> list) {
        if (list == null) {
            showEmptyView();
        } else {
            save(list);
            setAdapter();
        }
    }

    private void setAdapter() {
        draw();
        viewSwitcher.findViewById(R.id.emptyView).setVisibility(View.GONE);
        viewSwitcher.findViewById(R.id.plot).setVisibility(View.VISIBLE);
        viewSwitcher.setDisplayedChild(1);
    }

    private void save(List<GlobalDecreaseDTO> list) {
//        databaseManager.insertOrReplaceInTx(list.toArray(new GlobalDecrease[list.size()]));
//        items = databaseManager.listGlobalDecrease(mDate, mContext.getRegion());
        items = list;
    }

    private void setData() {
        series1Numbers = new ArrayList<>();
//        seriesDates = new ArrayList<>();
        int i = 0;
        for (GlobalDecreaseDTO gd : items) {
            series1Numbers.add(gd.getCost());
//            seriesDates.add(gd.getMonth());
            if (++i == 4){
                break;
            }
//            max = max < serie1 ? (serie1 < serie2 ? serie2 : serie1) : (max < serie2 ? serie2 : max);
        }
    }

//    private void draw() {
//        // create a couple arrays of y-values to plot:
//        setData();
//
//        plot.setPlotMargins(0, 0, 0, 0);
////        plot.setDrawRangeOriginEnabled(false);
//
//        // turn the above arrays into XYSeries':
//        // (Y_VALS_ONLY means use the element index as the x value)
//        XYSeries series1 = new SimpleXYSeries(seriesDates, series1Numbers, "Merma");
//        XYSeries series2 = new SimpleXYSeries(seriesDates, series2Numbers, "Promedio");
//
//        // create formatters to use for drawing a series using LineAndPointRenderer
//        // and configure them from xml:
//        LineAndPointFormatter series1Format = new LineAndPointFormatter();
//        series1Format.setPointLabelFormatter(new PointLabelFormatter());
//        series1Format.configure(mContext, R.xml.line_point_formatter_with_labels);
//
//        LineAndPointFormatter series2Format = new LineAndPointFormatter();
//        series2Format.setPointLabelFormatter(new PointLabelFormatter());
//        series2Format.configure(mContext, R.xml.line_point_formatter_with_labels_2);
//
//        // add an "dash" effect to the series2 line:
//        series2Format.getLinePaint().setPathEffect(new DashPathEffect(new float[]{
//
//                // always use DP when specifying pixel sizes, to keep things consistent across devices:
//                PixelUtils.dpToPix(10), PixelUtils.dpToPix(2)}, 0));
//
////        // just for fun, add some smoothing to the lines:
////        // see: http://androidplot.com/smooth-curves-and-androidplot/
////        series1Format.setInterpolationParams(new CatmullRomInterpolator.Params(13, CatmullRomInterpolator.Type.Centripetal));
//        series2Format.setInterpolationParams(new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));
//
//        // add a new series' to the xyplot:
//        plot.addSeries(series1, series1Format);
//        plot.addSeries(series2, series2Format);
//
//        // reduce the number of range labels
////        plot.setTicksPerRangeLabel(2);
//
//        // rotate domain labels 45 degrees to make them more compact horizontally:
////        plot.getGraphWidget().setDomainLabelOrientation(-45);
//
//        float rangeMax = max + 500;
//        plot.setRangeBoundaries(0, rangeMax, BoundaryMode.FIXED);
//        plot.setRangeStepValue(9);
////        plot.setDomainStep(XYStepMode.SUBDIVIDE, items.size());
//
//        Paint p = new Paint();
//        p.setColor(Color.TRANSPARENT);
////        plot.getGraphWidget().setRangeOriginLinePaint(p);
//        plot.getGraph().setRangeOriginLinePaint(p);
//
//        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).setFormat(new Format() {
//
//            private SimpleDateFormat dateFormat = new SimpleDateFormat(Constants.DD_MM, Locale.getDefault());
//
//            @Override
//            public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos) {
//                long timestamp = ((Number) obj).longValue();
//                Date date = new Date(timestamp);
//                return dateFormat.format(date, toAppendTo, pos);
//            }
//
//            @Override
//            public Object parseObject(String source, ParsePosition pos) {
//                return null;
//
//            }
//        });
//
//        plot.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    onPlotClicked(new PointF(event.getX(), event.getY()));
//                }
////                return true;
//                return false;
//            }
//        });
//
//    }
//
//    private void onPlotClicked(PointF point) {
////        XYGraphWidget widget = plot.getGraphWidget();
//        XYGraphWidget widget = plot.getGraph();
//        if (widget.containsPoint(point)) {
////            float left = widget.getGridDimensions().paddedRect.left;
//            float left = widget.getWidgetDimensions().paddedRect.left;
//            if (point.x < left) {
//                point.x = left + 1;
//            }
//            long date = widget.getXVal(point).longValue();
//
//            SimpleDateFormat df = new SimpleDateFormat(DateUtil.YYYY_MM_DD, Locale.getDefault());
//            String dateStr = df.format(new Date(date));
//            date = DateUtil.getDate(dateStr).getTime();
//            String decrease = getDecrease(date);
//            String avgDecrease = getDecreaseAvg(date);
//            Snackbar sb = Snackbar.make(plot, "Fecha: " + dateStr + "\nCosto: $" + decrease + "\nCosto promedio (28 días): $" + avgDecrease, Snackbar.LENGTH_INDEFINITE)
//                    .setAction("Detalle", new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e(TAG, "new activity");
//                }
//            });
//            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
//                sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary, mContext.getTheme()));
//                sb.setActionTextColor(getResources().getColor(android.R.color.white, mContext.getTheme()));
//            } else {
//                sb.getView().setBackgroundColor(getResources().getColor(R.color.colorPrimary));
//                sb.setActionTextColor(getResources().getColor(android.R.color.white));
//            }
//            ((TextView)sb.getView().findViewById(android.support.design.R.id.snackbar_text)).setMaxLines(3);
//            sb.show();
//        } else {
//            Log.e(TAG, "No in plot");
//        }
//    }
//
//    private String getDecreaseAvg(long time) {
//        for (int i = 0; i < seriesDates.size(); i++) {
//            if (seriesDates.get(i) == time) {
//                return series2Numbers.get(i).toString();
//            }
//        }
//        return null;
//    }
//
//    private String getDecrease(long time) {
//        for (int i = 0; i < seriesDates.size(); i++) {
//            if (seriesDates.get(i) == time) {
//                return series1Numbers.get(i).toString();
//            }
//        }
//        return null;
//    }



















    private static final String NO_SELECTION_TXT = "Toca una barra para ver detalle.";
    private XYPlot plot;
    // Create a couple arrays of y-values to plot:
//    Number[] series1Numbers10 = {3, 7, 4, 5};
    List<Double> series1Numbers;// = series1Numbers10;
    private MyBarFormatter formatter1;
    private MyBarFormatter selectionFormatter;
    private TextLabelWidget selectionWidget;
    private Pair<Integer, XYSeries> selection;

//    @Override
    public void draw() {

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.bar_plot_example);

        // initialize our XYPlot reference:
//        plot = (XYPlot) findViewById(R.id.plot);
        setData();

//        formatter1 = new MyBarFormatter(Color.rgb(100, 150, 100), Color.LTGRAY);
        formatter1 = new MyBarFormatter(Color.rgb(155, 17, 17), Color.LTGRAY);
        selectionFormatter = new MyBarFormatter(Color.rgb(215, 45, 45), Color.WHITE);

        selectionWidget = new TextLabelWidget(plot.getLayoutManager(), NO_SELECTION_TXT,
                new Size(
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE,
                        PixelUtils.dpToPix(100), SizeMode.ABSOLUTE),
                TextOrientation.HORIZONTAL);

        selectionWidget.getLabelPaint().setTextSize(PixelUtils.dpToPix(16));

//         add a dark, semi-transparent background to the selection label widget:
        Paint p = new Paint();
        p.setARGB(100, 0, 0, 0);
        selectionWidget.setBackgroundPaint(p);

        selectionWidget.position(
                PixelUtils.dpToPix(100), HorizontalPositioning.ABSOLUTE_FROM_CENTER,
                PixelUtils.dpToPix(45), VerticalPositioning.ABSOLUTE_FROM_TOP
//                , Anchor.RIGHT_TOP
        );
        selectionWidget.pack();

        // reduce the number of range labels
        plot.setLinesPerRangeLabel(3);
        plot.setRangeLowerBoundary(0, BoundaryMode.FIXED);

//        plot.getGraph().setMarginLeft(100);

        plot.setLinesPerDomainLabel(2);

        plot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    onPlotClicked(new PointF(motionEvent.getX(), motionEvent.getY()));
                }
                return true;
            }
        });

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).
                setFormat(new DecimalFormat("#,###"));

        plot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new NumberFormat() {
                    @Override
                    public StringBuffer format(double value, StringBuffer buffer, FieldPosition field) {
                        if((value == -1.0D || value == 4.0D) || (items.size() == 3 && value == 3.0D)){
                            return new StringBuffer("");
                        }
//                        int month = (int) ((value + 0.5d) % 12);
                        int month = items.get((int) value).getMonth()-1;
                        return new StringBuffer(DateFormatSymbols.getInstance().getShortMonths()[month]);
                    }

                    @Override
                    public StringBuffer format(long value, StringBuffer buffer,
                                               FieldPosition field) {
                        throw new UnsupportedOperationException("Not yet implemented.");
                    }

                    @Override
                    public Number parse(String string, ParsePosition position) {
                        throw new UnsupportedOperationException("Not yet implemented.");
                    }
                });

        updatePlot();

    }

    private void updatePlot() {

        // Remove all current series from each plot
        plot.clear();

        // Setup our Series with the selected number of elements
        XYSeries series1 = new SimpleXYSeries(series1Numbers,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "Costo");

        plot.setDomainBoundaries(-1, series1.size(), BoundaryMode.FIXED);
        int r = NumbersUtil.round(SeriesUtils.minMax(series1).getMaxY().floatValue() + 1000, 0).intValue();
        plot.setRangeUpperBoundary(
//                NumbersUtil.round(SeriesUtils.minMax(series1).getMaxY().floatValue() + 1000, 0), BoundaryMode.FIXED);
                r - r % 100, BoundaryMode.FIXED);

        plot.setDomainStep(StepMode.INCREMENT_BY_VAL, 1);

        // add a new series' to the xyplot:
        plot.addSeries(series1, formatter1);

        // Setup the BarRenderer with our selected options
        MyBarRenderer renderer = plot.getRenderer(MyBarRenderer.class);
        renderer.setStyle(BarRenderer.Style.STACKED);
        renderer.setBarWidthMode(BarRenderer.BarWidthMode.FIXED_WIDTH);
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.bar_width, outValue, false);
        float value = outValue.getFloat();
        renderer.setBarWidth(value);
        plot.setRangeTopMin(0);

        plot.redraw();

    }

    private void onPlotClicked(PointF point) {

        // make sure the point lies within the graph area.  we use gridrect
        // because it accounts for margins and padding as well.
        if (plot.containsPoint(point.x, point.y)) {
            Number x = plot.getXVal(point);
            Number y = plot.getYVal(point);

            selection = null;
            double xDistance = 0;
            double yDistance = 0;

            // find the closest value to the selection:
            for (SeriesAndFormatter<XYSeries, ? extends XYSeriesFormatter> sfPair : plot
                    .getSeriesRegistry()) {
                XYSeries series = sfPair.getSeries();
                for (int i = 0; i < series.size(); i++) {
                    Number thisX = series.getX(i);
                    Number thisY = series.getY(i);
                    if (thisX != null && thisY != null) {
                        double thisXDistance =
                                Region.measure(x, thisX).doubleValue();
                        double thisYDistance =
                                Region.measure(y, thisY).doubleValue();
                        if (selection == null) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance < xDistance) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        } else if (thisXDistance == xDistance &&
                                thisYDistance < yDistance &&
                                thisY.doubleValue() >= y.doubleValue()) {
                            selection = new Pair<>(i, series);
                            xDistance = thisXDistance;
                            yDistance = thisYDistance;
                        }
                    }
                }
            }

        } else {
            // if the press was outside the graph area, deselect:
            selection = null;
        }

        if (selection == null) {
            selectionWidget.setText(NO_SELECTION_TXT);
        } else {
            selectionWidget.setText("Costo: " + NumbersUtil.truncate2decimalsStr(selection.second.getY(selection.first).doubleValue()));
        }
        plot.redraw();
    }

    class MyBarFormatter extends BarFormatter {

        public MyBarFormatter(int fillColor, int borderColor) {
            super(fillColor, borderColor);
        }

        @Override
        public Class<? extends SeriesRenderer> getRendererClass() {
            return MyBarRenderer.class;
        }

        @Override
        public SeriesRenderer getRendererInstance(XYPlot plot) {
            return new MyBarRenderer(plot);
        }
    }

    class MyBarRenderer extends BarRenderer<MyBarFormatter> {

        public MyBarRenderer(XYPlot plot) {
            super(plot);
        }

        /**
         * Implementing this method to allow us to inject our
         * special selection getFormatter.
         * @param index index of the point being rendered.
         * @param series XYSeries to which the point being rendered belongs.
         * @return
         */
        @Override
        public MyBarFormatter getFormatter(int index, XYSeries series) {
            if (selection != null &&
                    selection.second == series &&
                    selection.first == index) {
                return selectionFormatter;
            } else {
                return getFormatter(series);
            }
        }
    }
}
