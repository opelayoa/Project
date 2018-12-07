package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.dto.GeneralTravelDTO;

import java.util.Comparator;

/**
 * Created by dfa on 05/07/2016.
 */
public class TravelComparators {

    public static Comparator<GeneralTravelDTO> getTruckComparator() {
        return new TruckComparator();
    }

    public static Comparator<GeneralTravelDTO> getStatusComparator() {
        return new StatusComparator();
    }

    public static Comparator<GeneralTravelDTO> getStoreComparator() {
        return new StoreComparator();
    }

    public static Comparator<GeneralTravelDTO> getTravelComparator() {
        return new TravelComparator();
    }

    public static Comparator<GeneralTravelDTO> getScaffoldComparator() {
        return new ScaffoldComparator();
    }

    public static Comparator<GeneralTravelDTO> getDownloadComparator() {
        return new DownloadComparator();
    }

    public static Comparator<GeneralTravelDTO> getTravelKmComparator() {
        return new TravelKmComparator();
    }

    public static Comparator<GeneralTravelDTO> getTotalKmComparator() {
        return new TotalKmComparator();
    }

    public static Comparator<GeneralTravelDTO> getTravelTimeComparator() {
        return new TravelTimeComparator();
    }

    public static Comparator<GeneralTravelDTO> getTotalTimeComparator() {
        return new TotalTimeComparator();
    }

    public static Comparator<GeneralTravelDTO> getTollComparator() {
        return new TollComparator();
    }

    public static Comparator<GeneralTravelDTO> getDieselComparator() {
        return new DieselComparator();
    }

    public static Comparator<GeneralTravelDTO> getTrafficTicketComparator() {
        return new TrafficTicketComparator();
    }

    public static Comparator<GeneralTravelDTO> getOtherComparator() {
        return new OtherComparator();
    }


    private static class TruckComparator implements Comparator<GeneralTravelDTO> {

        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return rs1.getTruck().compareTo(rs2.getTruck());
        }
    }

    private static class StatusComparator implements Comparator<GeneralTravelDTO> {

        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getStatus(), rs2.getStatus());
        }
    }

    private static class StoreComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getStoresNum(), rs2.getStoresNum());
        }
    }

    private static class TravelComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getTravelsNum(), rs2.getTravelsNum());
        }
    }

    private static class ScaffoldComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getScaffoldNum(), rs2.getScaffoldNum());
        }
    }

    private static class DownloadComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getDownloadsNum(), rs2.getDownloadsNum());
        }
    }

    private static class TravelKmComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getTravelKm(), rs2.getTravelKm());
        }
    }

    private static class TotalKmComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getTotalKm(), rs2.getTotalKm());
        }
    }

    private static class TravelTimeComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getTravelTime(), rs2.getTravelTime());
        }
    }

    private static class TotalTimeComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Integer.compare(rs1.getTotalTime(), rs2.getTotalTime());
        }
    }

    private static class TollComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getToll(), rs2.getToll());
        }
    }

    private static class DieselComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getDiesel(), rs2.getDiesel());
        }
    }

    private static class TrafficTicketComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getTrafficTicket(), rs2.getTrafficTicket());
        }
    }

    private static class OtherComparator implements Comparator<GeneralTravelDTO> {
        @Override
        public int compare(GeneralTravelDTO rs1, GeneralTravelDTO rs2) {
            return Float.compare(rs1.getOtherCost(), rs2.getOtherCost());
        }
    }
}