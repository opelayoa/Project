package com.tiendas3b.almacen.views.tables.comparators;

import com.tiendas3b.almacen.db.dao.ArticleResearch;

import java.util.Comparator;

/**
 * Created by dfa on 11/04/2016.
 */
public class ArticleResearchComparators {

    public static Comparator<ArticleResearch> getAvgPedComparator() {
        return new AvgPedComparator();
    }

    public static Comparator<ArticleResearch> getAvgFzdComparator() {
        return new AvgFzdComparator();
    }

    public static Comparator<ArticleResearch> getAvgSurtComparator() {
        return new AvgSurtComparator();
    }

    public static Comparator<ArticleResearch> getAvgSoComparator() {
        return new AvgSoComparator();
    }

    public static Comparator<ArticleResearch> getAvgInvestComparator() {
        return new AvgInvestComparator();
    }

    public static Comparator<ArticleResearch> getAvgCancelComparator() {
        return new AvgCancelComparator();
    }


    private static class AvgPedComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgPed().compareTo(rs2.getAvgPed());
        }
    }

    private static class AvgFzdComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgFzd().compareTo(rs2.getAvgFzd());
        }
    }

    private static class AvgSurtComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgSurt().compareTo(rs2.getAvgSurt());
        }
    }

    private static class AvgSoComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgSo().compareTo(rs2.getAvgSo());
        }
    }

    private static class AvgInvestComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgInvest().compareTo(rs2.getAvgInvest());
        }
    }

    private static class AvgCancelComparator implements Comparator<ArticleResearch> {

        @Override
        public int compare(ArticleResearch rs1, ArticleResearch rs2) {
            return rs1.getAvgCancel().compareTo(rs2.getAvgCancel());
        }
    }

}
