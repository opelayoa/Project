package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.dto.MeErrorDTO;

import java.util.Comparator;

/**
 * Created by dfa on 05/07/2016.
 */
public class MeErrorsComparators {

    public static Comparator<MeErrorDTO> getClaveComparator() {
        return new CveComparator();
    }

    public static Comparator<MeErrorDTO> getErrorsComparator() {
        return new ErrorsComparator();
    }

    
    private static class CveComparator implements Comparator<MeErrorDTO> {

        @Override
        public int compare(MeErrorDTO rs1, MeErrorDTO rs2) {
            return rs1.getMemberId().compareTo(rs2.getMemberId());
        }
    }

    private static class ErrorsComparator implements Comparator<MeErrorDTO> {

        @Override
        public int compare(MeErrorDTO rs1, MeErrorDTO rs2) {
            return Integer.compare(rs1.getNumErrors(), rs2.getNumErrors());
        }
    }
    
}