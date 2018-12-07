package com.tiendas3b.almacen.data;

/**
 * Created by dfa on 26/08/2016.
 */
import android.content.Context;
import android.widget.Filter;

import com.tiendas3b.almacen.GlobalState;
import com.tiendas3b.almacen.db.dao.Truck;
import com.tiendas3b.almacen.db.manager.DatabaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataHelper {

//    private static final String COLORS_FILE_NAME = "colors.json";

    private static List<Truck> sTruckWrappers = new ArrayList<>();
    private static List<TruckSuggestion> sTruckSuggestions;

    public static void init(List<Truck> trucks) {
        sTruckWrappers = trucks;
        sTruckSuggestions = new ArrayList<>();
        for (Truck t : sTruckWrappers) {
            sTruckSuggestions.add(new TruckSuggestion(t.getId(), t.getDescription()));
        }
    }

    public interface OnFindColorsListener {
        void onResults(List<Truck> results);
    }

    public interface OnFindSuggestionsListener {
        void onResults(List<TruckSuggestion> results);
    }

    public static List<TruckSuggestion> getHistory(Context context, int count) {

        List<TruckSuggestion> suggestionList = new ArrayList<>();
        TruckSuggestion truckSuggestion;
        for (int i = 0; i < sTruckSuggestions.size(); i++) {
            truckSuggestion = sTruckSuggestions.get(i);
            truckSuggestion.setIsHistory(true);
            suggestionList.add(truckSuggestion);
            if (suggestionList.size() == count) {
                break;
            }
        }
        return suggestionList;
    }

    public static void resetSuggestionsHistory() {
        for (TruckSuggestion truckSuggestion : sTruckSuggestions) {
            truckSuggestion.setIsHistory(false);
        }
    }

    public static void findSuggestions(Context context, String query, final int limit, final long simulatedDelay,
                                       final OnFindSuggestionsListener listener) {
        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                try {
                    Thread.sleep(simulatedDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                DataHelper.resetSuggestionsHistory();
                List<TruckSuggestion> suggestionList = new ArrayList<>();
                if (!(constraint == null || constraint.length() == 0)) {

                    for (TruckSuggestion suggestion : sTruckSuggestions) {
                        if (suggestion.getBody().toUpperCase()
                                .contains(constraint.toString().toUpperCase())) {

                            suggestionList.add(suggestion);
                            if (limit != -1 && suggestionList.size() == limit) {
                                break;
                            }
                        }
                    }
                }

                FilterResults results = new FilterResults();
                Collections.sort(suggestionList, new Comparator<TruckSuggestion>() {
                    @Override
                    public int compare(TruckSuggestion lhs, TruckSuggestion rhs) {
                        return lhs.getIsHistory() ? -1 : 0;
                    }
                });
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<TruckSuggestion>) results.values);
                }
            }
        }.filter(query);

    }


    public static void findColors(Context context, String query, final OnFindColorsListener listener) {
        initTruckWrapperList(context);

        new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {


                List<Truck> suggestionList = new ArrayList<>();

                if (!(constraint == null || constraint.length() == 0)) {

                    for (Truck truck : sTruckWrappers) {
                        if (truck.getDescription().toUpperCase()
                                .startsWith(constraint.toString().toUpperCase())) {

                            suggestionList.add(truck);
                        }
                    }

                }

                FilterResults results = new FilterResults();
                results.values = suggestionList;
                results.count = suggestionList.size();

                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                if (listener != null) {
                    listener.onResults((List<Truck>) results.values);
                }
            }
        }.filter(query);

    }

    private static void initTruckWrapperList(Context context) {

        if (sTruckWrappers.isEmpty()) {
//            String jsonString = loadJson(context);
//            sTruckWrappers = deserializeColors(jsonString);
            sTruckWrappers = new DatabaseManager(context).listActiveTrucks(((GlobalState) context).getRegion());
        }
    }

//    private static String loadJson(Context context) {
//
//        String jsonString;
//
//        try {
//            InputStream is = context.getAssets().open(COLORS_FILE_NAME);
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//            jsonString = new String(buffer, "UTF-8");
//        } catch (IOException ex) {
//            ex.printStackTrace();
//            return null;
//        }
//
//        return jsonString;
//    }
//
//    private static List<Truck> deserializeColors(String jsonString) {
//
//        Gson gson = new Gson();
//
//        Type collectionType = new TypeToken<List<Truck>>() {
//        }.getType();
//        return gson.fromJson(jsonString, collectionType);
//    }

}
