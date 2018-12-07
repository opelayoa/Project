package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.db.dao.ReceiptSheet;

import java.util.Comparator;

/**
 * Created by dfa on 11/04/2016.
 */
public class ReceiptSheetComparators {


    public static Comparator<ReceiptSheet> getProviderComparator() {
        return new ProviderComparator();
    }

    public static Comparator<ReceiptSheet> getDateTimeComparator() {
        return new DateTimeComparator();
    }

    public static Comparator<ReceiptSheet> getPlatfomrComparator() {
        return new PlatfomrComparator();
    }

    private static class ProviderComparator implements Comparator<ReceiptSheet> {

        @Override
        public int compare(ReceiptSheet rs1, ReceiptSheet rs2) {
            return rs1.getProvider().getBusinessName().compareTo(rs2.getProvider().getBusinessName());
        }
    }

    private static class DateTimeComparator implements Comparator<ReceiptSheet> {
        @Override
        public int compare(ReceiptSheet dt1, ReceiptSheet dt2) {
            return dt1.getDateTime().compareTo(dt2.getDateTime());
        }
    }

    private static class PlatfomrComparator implements Comparator<ReceiptSheet> {
        @Override
        public int compare(ReceiptSheet p1, ReceiptSheet p2) {
            return p1.getPlatform().compareTo(p2.getPlatform());
        }
    }
}
