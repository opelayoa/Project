package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.db.dao.ScannerCount;

import java.util.Comparator;

/**
 * Created by dfa on 05/07/2016.
 */
public class ScannerCountComparators {

    public static Comparator<ScannerCount> getIclaveComparator() {
        return new IclaveComparator();
    }

    public static Comparator<ScannerCount> getFolComparator() {
        return new FolComparator();
    }

    public static Comparator<ScannerCount> getCanComparator() {
        return new CanComparator();
    }

    public static Comparator<ScannerCount> getDesComparator() {
        return new DesComparator();
    }

    public static Comparator<ScannerCount> getCosComparator() {
        return new CosComparator();
    }

    public static Comparator<ScannerCount> getVenComparator() {
        return new VenComparator();
    }

    private static class IclaveComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return Long.compare(rs1.getIclave(), rs2.getIclave());
        }
    }

    private static class FolComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return rs1.getFolio().compareTo(rs2.getFolio());
        }
    }

    private static class CanComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return rs1.getAmount().compareTo(rs2.getAmount());
        }
    }

    private static class DesComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return rs1.getVarticle().getDescription().compareTo(rs2.getVarticle().getDescription());
        }
    }

    private static class CosComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return rs1.getVarticle().getCost().compareTo(rs2.getVarticle().getCost());
        }
    }

    private static class VenComparator implements Comparator<ScannerCount> {

        @Override
        public int compare(ScannerCount rs1, ScannerCount rs2) {
            return rs1.getVarticle().getSale().compareTo(rs2.getVarticle().getSale());
        }
    }
}