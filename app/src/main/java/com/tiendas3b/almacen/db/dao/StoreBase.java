package com.tiendas3b.almacen.db.dao;

/**
 * Created by dfa on 08/08/2016.
 */
public abstract class StoreBase extends EqualsBase {

    @Override
    public String toString() {
        return String.valueOf(getId());
    }
}
