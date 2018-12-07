package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.db.dao.Complaint;

import java.util.Comparator;

/**
 * Created by dfa on 05/07/2016.
 */
public class ComplaintsComparators {

    public static Comparator<Complaint> getIclaveComparator() {
        return new IclaveComparator();
    }

    public static Comparator<Complaint> getStoreComparator() {
        return new StoreComparator();
    }

    public static Comparator<Complaint> getDesComparator() {
        return new DesComparator();
    }

    public static Comparator<Complaint> getCosComparator() {
        return new CosComparator();
    }

    public static Comparator<Complaint> getVenComparator() {
        return new VenComparator();
    }

    public static Comparator<Complaint> getRAComparator() {
        return new RAComparator();
    }

    public static Comparator<Complaint> getRSComparator() {
        return new RAComparator();
    }

    private static class StoreComparator implements Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return Long.compare(rs1.getStoreId(), rs2.getStoreId());
        }
    }
    private static class IclaveComparator implements Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return Long.compare(rs1.getIclave(), rs2.getIclave());
        }
    }

    private static class DesComparator implements java.util.Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return rs1.getVarticle().getDescription().compareTo(rs2.getVarticle().getDescription());
        }
    }

    private static class CosComparator implements java.util.Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return rs1.getVarticle().getCost().compareTo(rs2.getVarticle().getCost());
        }
    }

    private static class VenComparator implements java.util.Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return rs1.getVarticle().getSale().compareTo(rs2.getVarticle().getSale());
        }
    }

    private static class RAComparator implements java.util.Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return rs1.getResponsibleArea().compareTo(rs2.getResponsibleArea());
        }
    }

    private static class RSComparator implements Comparator<Complaint> {

        @Override
        public int compare(Complaint rs1, Complaint rs2) {
            return rs1.getResponsibleSubarea().compareTo(rs2.getResponsibleSubarea());
        }
    }
}