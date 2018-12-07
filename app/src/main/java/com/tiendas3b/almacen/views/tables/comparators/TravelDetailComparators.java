package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.dto.TravelDetailDTO;

import java.util.Comparator;

/**
 * Created by dfa on 05/07/2016.
 */
public class TravelDetailComparators {

    public static Comparator<TravelDetailDTO> getActDescComparator() {
        return new ActDescComparator();
    }

    public static Comparator<TravelDetailDTO> getScaffoldComparator() {
        return new ScaffoldComparator();
    }

    public static Comparator<TravelDetailDTO> getDestinationComparator() {
        return new DestinationComparator();
    }

    public static Comparator<TravelDetailDTO> getInitialKmComparator() {
        return new InitialKmComparator();
    }

    public static Comparator<TravelDetailDTO> getFinalKmComparator() {
        return new FinalKmComparator();
    }

    public static Comparator<TravelDetailDTO> getDiffKmComparator() {
        return new DiffKmComparator();
    }

    public static Comparator<TravelDetailDTO> getInitialTimeComparator() {
        return new InitialTimeComparator();
    }

    public static Comparator<TravelDetailDTO> getFinalTimeComparator() {
        return new FinalTimeComparator();
    }

    public static Comparator<TravelDetailDTO> getDiffTimeComparator() {
        return new DiffTimeComparator();
    }

    public static Comparator<TravelDetailDTO> getTollComparator() {
        return new TollComparator();
    }

    public static Comparator<TravelDetailDTO> getDieselComparator() {
        return new DieselComparator();
    }

    public static Comparator<TravelDetailDTO> getTrafficTicketComparator() {
        return new TrafficTicketComparator();
    }

    public static Comparator<TravelDetailDTO> getOtherComparator() {
        return new OtherComparator();
    }


    private static class ActDescComparator implements Comparator<TravelDetailDTO> {

        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return rs1.getActDesc().compareTo(rs2.getActDesc());
        }
    }

    private static class ScaffoldComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Integer.compare(rs1.getScaffoldDownloadNum(), rs2.getScaffoldDownloadNum());
        }
    }

    private static class DestinationComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Integer.compare(rs1.getDestination(), rs2.getDestination());
        }
    }

    private static class InitialKmComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getInitialKm(), rs2.getInitialKm());
        }
    }

    private static class FinalKmComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getFinalKm(), rs2.getFinalKm());
        }
    }

    private static class DiffKmComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getDiffKm(), rs2.getDiffKm());
        }
    }

    private static class InitialTimeComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return rs1.getInitialTime().compareTo(rs2.getInitialTime());
        }
    }

    private static class FinalTimeComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return rs1.getFinalTime().compareTo(rs2.getFinalTime());
        }
    }

    private static class DiffTimeComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Integer.compare(rs1.getDiffTime(), rs2.getDiffTime());
        }
    }

    private static class TollComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getToll(), rs2.getToll());
        }
    }

    private static class DieselComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getDiesel(), rs2.getDiesel());
        }
    }

    private static class TrafficTicketComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getTrafficTicket(), rs2.getTrafficTicket());
        }
    }

    private static class OtherComparator implements Comparator<TravelDetailDTO> {
        @Override
        public int compare(TravelDetailDTO rs1, TravelDetailDTO rs2) {
            return Float.compare(rs1.getOtherCost(), rs2.getOtherCost());
        }
    }
}