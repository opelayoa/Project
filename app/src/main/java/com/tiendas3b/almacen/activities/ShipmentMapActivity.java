package com.tiendas3b.almacen.activities;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.SearchSuggestionsAdapter;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.R;
import com.tiendas3b.almacen.data.DataHelper;
import com.tiendas3b.almacen.data.TruckSuggestion;
import com.tiendas3b.almacen.db.dao.Message;
import com.tiendas3b.almacen.db.dao.Route;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;
import com.tiendas3b.almacen.db.manager.IDatabaseManager;
import com.tiendas3b.almacen.http.Tiendas3bClient;
import com.tiendas3b.almacen.util.DateUtil;
import com.tiendas3b.almacen.util.DialogUtil;
import com.tiendas3b.almacen.util.PermissionUtil;
import com.tiendas3b.almacen.util.ResourceUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShipmentMapActivity extends FragmentActivity implements OnMapReadyCallback, DatePickerDialog.OnDateSetListener {



    protected static final String STATE_DATE = "date";
    private GoogleMap mMap;
    @BindView(R.id.floating_search_view)
    FloatingSearchView mSearchView;

    private final String TAG = "BlankFragment";
    public static final long FIND_SUGGESTION_SIMULATED_DELAY = 250;
    private String mLastQuery = "";

    protected GlobalState mContext;
    protected IDatabaseManager db;
    protected Calendar calendar;
    protected Date selectedDate;

    //    /**
//     * The number of points allowed per API request. This is a fixed value.
//     */
//    private static final int PAGE_SIZE_LIMIT = 100;
//    /**
//     * Define the number of data points to re-send at the start of subsequent requests. This helps
//     * to influence the API with prior data, so that paths can be inferred across multiple requests.
//     * You should experiment with this value for your use-case.
//     */
//    private static final int PAGINATION_OVERLAP = 5;
//    List<com.google.maps.model.LatLng> mCapturedLocations;
//    List<SnappedPoint> mSnappedPoints;
    private GeoApiContext geoContext;
    //
//    AsyncTask<Void, Void, List<SnappedPoint>> mTaskSnapToRoads =
//            new AsyncTask<Void, Void, List<SnappedPoint>>() {
//                @Override
//                protected void onPreExecute() {
////                    mProgressBar.setVisibility(View.VISIBLE);
////                    mProgressBar.setIndeterminate(true);
//                }
//
//                @Override
//                protected List<SnappedPoint> doInBackground(Void... params) {
//                    try {
//                        return snapToRoads();
//                    } catch (final Exception ex) {
////                        toastException(ex);
//                        ex.printStackTrace();
//                        return null;
//                    }
//                }
//
//                @Override
//                protected void onPostExecute(List<SnappedPoint> snappedPoints) {
//                    mSnappedPoints = snappedPoints;
////                    mProgressBar.setVisibility(View.INVISIBLE);
//
//                    com.google.android.gms.maps.model.LatLng[] mapPoints =
//                            new com.google.android.gms.maps.model.LatLng[mSnappedPoints.size()];
//                    int i = 0;
//                    LatLngBounds.Builder bounds = new LatLngBounds.Builder();
//                    for (SnappedPoint point : mSnappedPoints) {
//                        mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.location.lat,
//                                point.location.lng);
//                        bounds.include(mapPoints[i]);
//                        i += 1;
//                    }
//
//                    mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
//                    mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
//                }
//            };
//
//    private List<SnappedPoint> snapToRoads() throws Exception {
//        List<SnappedPoint> snappedPoints = new ArrayList<>();
//
//        int offset = 0;
//        while (offset < mCapturedLocations.size()) {
//            // Calculate which points to include in this request. We can't exceed the APIs
//            // maximum and we want to ensure some overlap so the API can infer a good location for
//            // the first few points in each request.
//            if (offset > 0) {
//                offset -= PAGINATION_OVERLAP;   // Rewind to include some previous points
//            }
//            int lowerBound = offset;
//            int upperBound = Math.min(offset + PAGE_SIZE_LIMIT, mCapturedLocations.size());
//
//            // Grab the data we need for this page.
//            com.google.maps.model.LatLng[] page = mCapturedLocations
//                    .subList(lowerBound, upperBound)
//                    .toArray(new com.google.maps.model.LatLng[upperBound - lowerBound]);
//
//            // Perform the request. Because we have interpolate=true, we will get extra data points
//            // between our originally requested path. To ensure we can concatenate these points, we
//            // only start adding once we've hit the first new point (i.e. skip the overlap).
//            SnappedPoint[] points = RoadsApi.snapToRoads(geoContext, true, page).await();
//            boolean passedOverlap = false;
//            for (SnappedPoint point : points) {
//                if (offset == 0 || point.originalIndex >= PAGINATION_OVERLAP) {
//                    passedOverlap = true;
//                }
//                if (passedOverlap) {
//                    snappedPoints.add(point);
//                }
//            }
//
//            offset = upperBound;
//        }
//
//        return snappedPoints;
//    }
    private String[] waypoints;
    private String origin;
    private String destination;

    private List<com.google.maps.model.LatLng> directions() throws Exception {

        DirectionsResult res = DirectionsApi.getDirections(geoContext, origin, destination).waypoints(waypoints).await();
        DirectionsRoute route = res.routes[0];
        List<com.google.maps.model.LatLng> points = route.overviewPolyline.decodePath();

        return points;
    }

    @Override
    protected void onRestart() {
        db = new DatabaseManager(this);
        super.onRestart();
    }

    @Override
    protected void onStop() {
        //no se todavia si poner los items = null...
        if (db != null) db.closeDbConnections();
        super.onStop();
    }

    @Override
    protected void onResume() {
        db = DatabaseManager.getInstance(this);
        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipment_map);

        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

        if (savedInstanceState == null) {
            calendar = Calendar.getInstance();
//            calendar.add(Calendar.DAY_OF_MONTH, -1);
        } else {
            calendar = (Calendar) savedInstanceState.getSerializable(STATE_DATE);
        }
        selectedDate = calendar.getTime();

        geoContext = new GeoApiContext.Builder().apiKey(getString(R.string.google_maps_web_services_key)).build();

        init();
    }

    private void init() {
        mContext = (GlobalState) getApplicationContext();
        ButterKnife.bind(this);
        db = DatabaseManager.getInstance(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setupFloatingSearch();
    }

    private void setupFloatingSearch() {
        List<Truck> trucks = db.listActiveTrucks(mContext.getRegion());
        DataHelper.init(trucks);
        mSearchView.setOnQueryChangeListener(new FloatingSearchView.OnQueryChangeListener() {

            @Override
            public void onSearchTextChanged(String oldQuery, final String newQuery) {

                if (!oldQuery.equals("") && newQuery.equals("")) {
                    mSearchView.clearSuggestions();
                } else {

                    //this shows the top left circular progress
                    //you can call it where ever you want, but
                    //it makes sense to do it when loading something in
                    //the background.
                    mSearchView.showProgress();

//                    List<Truck> trucks = db.listActiveTrucks(mContext.getRegion());

                    //simulates a query call to a data source
                    //with a new query.
                    DataHelper.findSuggestions(ShipmentMapActivity.this, newQuery, 5, FIND_SUGGESTION_SIMULATED_DELAY, new DataHelper.OnFindSuggestionsListener() {

                        @Override
                        public void onResults(List<TruckSuggestion> results) {

                            //this will swap the data and
                            //render the collapse/expand animations as necessary
                            mSearchView.swapSuggestions(results);

                            //let the users know that the background
                            //process has completed
                            mSearchView.hideProgress();
                        }
                    });
                }

                Log.d(TAG, "onSearchTextChanged()");
            }
        });

        mSearchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {
            @Override
            public void onSuggestionClicked(final SearchSuggestion searchSuggestion) {

                TruckSuggestion truckSuggestion = (TruckSuggestion) searchSuggestion;
//                DataHelper.findColors(getApplicationContext(), truckSuggestion.getBody(), new DataHelper.OnFindColorsListener() {
//
//                    @Override
//                    public void onResults(List<Truck> results) {
//                        //TODO
//                    }
//
//                });
                Log.d(TAG, "onSuggestionClicked() id:" + truckSuggestion.getId() + " date: " + selectedDate);
                mLastQuery = searchSuggestion.getBody();

                mSearchView.showProgress();

                requestInfo(truckSuggestion.getId());//TODO activar cuando estén los servicios!!!!
            }

            @Override
            public void onSearchAction(String query) {
                mLastQuery = query;

                DataHelper.findColors(getApplicationContext(), query, new DataHelper.OnFindColorsListener() {

                    @Override
                    public void onResults(List<Truck> results) {
                        //TODO
                    }

                });
                Log.d(TAG, "onSearchAction()");
            }
        });

        mSearchView.setOnFocusChangeListener(new FloatingSearchView.OnFocusChangeListener() {
            @Override
            public void onFocus() {

                //show suggestions when search bar gains focus (typically history suggestions)
                mSearchView.swapSuggestions(DataHelper.getHistory(ShipmentMapActivity.this, 3));

                Log.d(TAG, "onFocus()");
            }

            @Override
            public void onFocusCleared() {

                //set the title of the bar so that when focus is returned a new query begins
                mSearchView.setSearchBarTitle(mLastQuery);

                //you can also set setSearchText(...) to make keep the query there when not focused and when focus returns
                //mSearchView.setSearchText(searchSuggestion.getBody());

                Log.d(TAG, "onFocusCleared()");
            }
        });


        //handle menu clicks the same way as you would
        //in a regular activity
        mSearchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                if (item.getItemId() == R.id.action_calendar) {
                    showCalendar();
                }

            }
        });

        //use this listener to listen to menu clicks when app:floatingSearch_leftAction="showHome"
        mSearchView.setOnHomeActionClickListener(new FloatingSearchView.OnHomeActionClickListener() {
            @Override
            public void onHomeClicked() {
                //TODO
                Log.d(TAG, "onHomeClicked()");
            }
        });

        /*
         * Here you have access to the left icon and the textAndPrint of a given suggestion
         * item after as it is bound to the suggestion list. You can utilize this
         * callback to change some properties of the left icon and the textAndPrint. For example, you
         * can load the left icon images using your favorite image loading library, or change textAndPrint color.
         *
         *
         * Important:
         * Keep in mind that the suggestion list is a RecyclerView, so views are reused for different
         * items in the list.
         */
        mSearchView.setOnBindSuggestionCallback(new SearchSuggestionsAdapter.OnBindSuggestionCallback() {
            @Override
            public void onBindSuggestion(View suggestionView, ImageView leftIcon, TextView textView, SearchSuggestion item, int itemPosition) {
                //TODO
//                TruckSuggestion colorSuggestion = (TruckSuggestion) item;
//
//                String textColor = mIsDarkSearchTheme ? "#ffffff" : "#000000";
//                String textLight = mIsDarkSearchTheme ? "#bfbfbf" : "#787878";
//
//                if (colorSuggestion.getIsHistory()) {
//                    leftIcon.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_history_black_24dp, null));
//
//                    Util.setIconColor(leftIcon, Color.parseColor(textColor));
//                    leftIcon.setAlpha(.36f);
//                } else {
//                    leftIcon.setAlpha(0.0f);
//                    leftIcon.setImageDrawable(null);
//                }
//
//                textView.setTextColor(Color.parseColor(textColor));
//                String text = colorSuggestion.getBody().replaceFirst(mSearchView.getQuery(), "<font color=\"" + textLight + "\">" + mSearchView.getQuery() + "</font>");
//                textView.setText(Html.fromHtml(text));
            }

        });

        //listen for when suggestion list expands/shrinks in order to move down/up the
        //search results list
        mSearchView.setOnSuggestionsListHeightChanged(new FloatingSearchView.OnSuggestionsListHeightChanged() {
            @Override
            public void onSuggestionsListHeightChanged(float newHeight) {
                //TODO
            }
        });
    }

    private void requestInfo(long truckId) {
        mMap.clear();
        String selectedDateStr = DateUtil.getDateStr(selectedDate);
        requestLocation(selectedDateStr, truckId);
        requestDirections(selectedDateStr, truckId);
        requestRoutes(selectedDateStr, truckId);

    }

    private void requestRoutes(String selectedDateStr, long truckId) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getRoutes(mContext.getRegion(), selectedDateStr, truckId).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                Log.e(TAG, "Y");
                if (response.isSuccessful()) {
//                    drawInfo(response.body());
                } else {
//                    List<Route> routes = dummyRoutes();
//                    int size = routes.size();
//                    waypoints = new String[size - 2];
//                    for (int i = 1, limit = size - 1; i < limit; i++) {
//                        Route r = routes.get(i);
//                        waypoints[i - 1] = r.getLat() + "," + r.getLng();
//                    }
//                    Route rI = routes.get(0);
//                    origin = rI.getLat() + "," + rI.getLng();
//                    Route rF = routes.get(size - 1);
//                    destination = rF.getLat() + "," + rF.getLng();
//                    drawInfo(routes);
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
//                List<Route> routes = dummyRoutes();
//                int size = routes.size();
//                waypoints = new String[size - 2];
//                for (int i = 1, limit = size - 1; i < limit; i++) {
//                    Route r = routes.get(i);
//                    waypoints[i - 1] = r.getLat() + "," + r.getLng();
//                }
//                Route rI = routes.get(0);
//                origin = rI.getLat() + "," + rI.getLng();
//                Route rF = routes.get(size - 1);
//                destination = rF.getLat() + "," + rF.getLng();
//                drawInfo(routes);
            }
        });
    }

    private void requestDirections(String selectedDateStr, long truckId) {
        Tiendas3bClient httpService = mContext.getHttpServiceWithAuth();
        httpService.getRoutes(mContext.getRegion(), selectedDateStr, truckId).enqueue(new Callback<List<Route>>() {
            @Override
            public void onResponse(Call<List<Route>> call, Response<List<Route>> response) {
                Log.e(TAG, "Y");
                if (response.isSuccessful()) {
                    drawInfo(response.body());
                } else {
                    List<Route> routes = dummyRoutes();
                    int size = routes.size();
                    waypoints = new String[size - 2];
                    for (int i = 1, limit = size - 1; i < limit; i++) {
                        Route r = routes.get(i);
                        waypoints[i - 1] = r.getLat() + "," + r.getLng();
                    }
                    Route rI = routes.get(0);
                    origin = rI.getLat() + "," + rI.getLng();
                    Route rF = routes.get(size - 1);
                    destination = rF.getLat() + "," + rF.getLng();
                    drawInfo(routes);
                }
            }

            @Override
            public void onFailure(Call<List<Route>> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                List<Route> routes = dummyRoutes();
                int size = routes.size();
                waypoints = new String[size - 2];
                for (int i = 1, limit = size - 1; i < limit; i++) {
                    Route r = routes.get(i);
                    waypoints[i - 1] = r.getLat() + "," + r.getLng();
                }
                Route rI = routes.get(0);
                origin = rI.getLat() + "," + rI.getLng();
                Route rF = routes.get(size - 1);
                destination = rF.getLat() + "," + rF.getLng();
                drawInfo(routes);
            }
        });
    }

    private void requestLocation(String selectedDateStr, long truckId) {
        Tiendas3bClient httpService = mContext.getHttpService();
        httpService.getLocation(mContext.getRegion(), selectedDateStr, truckId).enqueue(new Callback<Message>() {
            @Override
            public void onResponse(Call<Message> call, Response<Message> response) {
                Log.e(TAG, "X");
                if (response.isSuccessful()) {
                    drawLocation(response.body());
                } else {
                    Message truckLocation = dummyLocation();
                    drawLocation(truckLocation);
                }
            }

            @Override
            public void onFailure(Call<Message> call, Throwable t) {
                Log.e(TAG, mContext.getString(R.string.error_odc));
                Message truckLocation = dummyLocation();
                drawLocation(truckLocation);
            }
        });
    }

    private void drawLocation(Message truckLocation) {
        LatLng p = new LatLng(truckLocation.getLat(), truckLocation.getLng());
        mMap.addMarker(new MarkerOptions().position(p).title(String.valueOf(/*truckLocation.getName()*/"Camión XX"))
                .snippet(DateUtil.getDateStr(truckLocation.getEmissionDateMillis(), DateUtil.DD_MM_YYYY_HH_MM_SS)).anchor(.5F, .5F).rotation(truckLocation.getBearing()).icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(this, R.drawable.truck))));
    }

    private Message dummyLocation() {
        Message m = new Message();
        m.setLat(19.579848);
        m.setLng(-99.217996);
        m.setBearing(180.0F);
        m.setEmissionDateMillis(1473774258667L);
        m.setAccuracy(5.0F);
        m.setSpeed(45.0F);
        return m;
    }

    private void drawInfo(List<Route> routes) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

//        PolylineOptions options = new PolylineOptions();
//        options.color(Color.parseColor("#00BFFF"));
//        options.width(15);
//        options.visible(true);
////        mCapturedLocations = new ArrayList<>();

        for (Route r : routes) {
            LatLng p = new LatLng(r.getLat(), r.getLng());
            Marker m = mMap.addMarker(new MarkerOptions().position(p).title(String.valueOf(r.getStoreId())).snippet(r.getDate() + " " + r.getActivity()).icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(this, R.drawable.ic_poi_check))));
            builder.include(m.getPosition());
//            options.add(p);

//            mCapturedLocations.add(new com.google.maps.model.LatLng(r.getLat(), r.getLng()));
        }
//        mMap.addPolyline(options);

        int padding = 130; // offset from edges of the map in pixels
        LatLngBounds bounds = builder.build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cameraUpdate);

        drawDirections();
    }

    private void drawDirections() {
        new AsyncTask<Void, Void, List<com.google.maps.model.LatLng>>() {
            @Override
            protected void onPreExecute() {
//                    mProgressBar.setVisibility(View.VISIBLE);
//                    mProgressBar.setIndeterminate(true);
            }

            @Override
            protected List<com.google.maps.model.LatLng> doInBackground(Void... params) {
                try {
                    return directions();
                } catch (final Exception ex) {
//                        toastException(ex);
                    ex.printStackTrace();
                    return null;
                }
            }

            @Override
            protected void onPostExecute(List<com.google.maps.model.LatLng> mSnappedPoints) {
//                    mSnappedPoints = snappedPoints;
//                    mProgressBar.setVisibility(View.INVISIBLE);

                com.google.android.gms.maps.model.LatLng[] mapPoints = new com.google.android.gms.maps.model.LatLng[mSnappedPoints.size()];
                int i = 0;
                LatLngBounds.Builder bounds = new LatLngBounds.Builder();
                for (com.google.maps.model.LatLng point : mSnappedPoints) {
                    mapPoints[i] = new com.google.android.gms.maps.model.LatLng(point.lat, point.lng);
                    bounds.include(mapPoints[i]);
                    i += 1;
                }

                PolylineOptions options = new PolylineOptions();
                options.color(Color.parseColor("#00BFFF"));
                options.width(15);
                options.visible(true);
                options.add(mapPoints);
////        mCapturedLocations = new ArrayList<>();
//
//        for (Route r : routes) {
//            LatLng p = new LatLng(r.getLat(), r.getLng());
//            Marker m = mMap.addMarker(new MarkerOptions().position(p).title(String.valueOf(r.getStoreId())).snippet(r.getDate() + " " + r.getActivity()).icon(BitmapDescriptorFactory.fromBitmap(ResourceUtil.getBitmap(this, R.drawable.ic_poi_check))));
//            builder.include(m.getPosition());
//            options.add(p);
//
////            mCapturedLocations.add(new com.google.maps.model.LatLng(r.getLat(), r.getLng()));
//        }
                mMap.addPolyline(options);

//            mMap.addPolyline(new PolylineOptions().add(mapPoints).color(Color.BLUE));
                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 130));


                mSearchView.hideProgress();
            }
        }.execute();
    }

    private List<Route> dummyRoutes() {
        List<Route> routes = new ArrayList<>();
        Route r1 = new Route();
        r1.setRegionId(1000);
        r1.setDate("2016-08-30");
        r1.setActivity("start");
        r1.setLat(19.565157);
        r1.setLng(-99.2025);
        r1.setStoreId(1000);
        r1.setTravel(1);
        r1.setTruck("blanco xx");
        routes.add(r1);

        Route r2 = new Route();
        r2.setRegionId(1000);
        r2.setDate("2016-08-30");
        r2.setActivity("service");
        r2.setLat(19.61777488);
        r2.setLng(-99.19886862);
        r2.setStoreId(103);
        r2.setTravel(1);
        r2.setTruck("blanco xx");
        routes.add(r2);

        Route r3 = new Route();
        r3.setRegionId(1000);
        r3.setDate("2016-08-30");
        r3.setActivity("service");
        r3.setLat(19.591945);
        r3.setLng(-99.237070);
        r3.setStoreId(450);
        r3.setTravel(1);
        r3.setTruck("blanco xx");
        routes.add(r3);
//        Route r4 = new Route();
//        r4.setRegionId(1000);
//        r4.setDate("2016-08-30");
//        r4.setActivity("service");
//        r4.setLat(19.63651733);
//        r4.setLng(-98.99226171);
//        r4.setStoreId(378);
//        r4.setTravel(1);
//        r4.setTruck("blanco xx");
//        routes.add(r4);
        Route rF = new Route();
        rF.setRegionId(1000);
        rF.setDate("2016-08-30");
        rF.setActivity("end");
        rF.setLat(19.565157);
        rF.setLng(-99.2025);
        rF.setStoreId(1000);
        rF.setTravel(1);
        rF.setTruck("blanco xx");
        routes.add(rF);
        return routes;
    }

    private void showCalendar() {
        DialogUtil.showCalendarTodayMax(this, calendar, this);
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//        btnCalendar.setVisible(false);
//        progressBar.setVisibility(View.VISIBLE);
        calendar.set(year, monthOfYear, dayOfMonth);
        selectedDate = calendar.getTime();
        super.onRetainCustomNonConfigurationInstance();
//        getDatabaseInfo();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STATE_DATE, calendar);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    @SuppressWarnings("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Resources resources = getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = 0;
        if (resourceId > 0) {
            height = resources.getDimensionPixelSize(resourceId);
        }

        mMap.setPadding(0, getStatusBarHeight(), 0, height);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(19.432587, -99.133207), 10.0f));

        if (PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_FINE_LOCATION) /*|| PermissionUtil.permissionGranted(this, Manifest.permission.ACCESS_COARSE_LOCATION)*/) {
            mMap.setMyLocationEnabled(true);
        } else {
            PermissionUtil.showDialog(this, PermissionUtil.REQUEST_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);
        }
    }

    public int getStatusBarHeight() {
        final TypedArray styledAttributes = getTheme().obtainStyledAttributes(new int[]{android.R.attr.actionBarSize});
        int result = (int) styledAttributes.getDimension(0, 0);
        styledAttributes.recycle();

        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            int statusBarHeight = getResources().getDimensionPixelSize(resourceId);
            result += statusBarHeight;
            mSearchView.setPadding(0, statusBarHeight - 5, 0, 0);
        }
        return result;
    }

    @Override
    @SuppressWarnings("MissingPermission")
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PermissionUtil.REQUEST_FINE_LOCATION:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                } else {

                }
            default:
                break;
        }
    }

}
