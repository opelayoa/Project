package com.tiendas3b.almacen.db.dao;

/**
 * Created by dfa on 08/08/2016.
 */
public abstract class SpinnerBase extends EqualsBase {

    public static final String DESCRIPTION_ATT = "description";

    public abstract String getDescription();



    @Override
    public String toString() {
        return getDescription();
    }

    public static <T extends SpinnerBase> T addEmpty() {
        return (T) new Empty();
    }

    private static class Empty extends SpinnerBase{

        @Override
        public Long getId() {
            return -1L;
        }

        @Override
        public String getDescription() {
            return "Sin información";
        }
    }


}
