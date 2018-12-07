package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.db.dao.VArticle;

import java.util.Comparator;

/**
 * Created by dfa on 11/04/2016.
 */
public class DecreaseCaptureComparators {


    public static Comparator<VArticle> getDateTimeComparator() {
        return new DateTimeComparator();
    }

    public static Comparator<VArticle> getIdComparator() {
        return new PlatfomrComparator();
    }

    private static class DateTimeComparator implements Comparator<VArticle> {
        @Override
        public int compare(VArticle dt1, VArticle dt2) {
            return dt1.getDescription().compareTo(dt2.getDescription());
        }
    }

    private static class PlatfomrComparator implements Comparator<VArticle> {
        @Override
        public int compare(VArticle p1, VArticle p2) {
            return p1.getId().compareTo(p2.getId());
        }
    }
}
