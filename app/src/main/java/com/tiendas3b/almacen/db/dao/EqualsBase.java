package com.tiendas3b.almacen.db.dao;

import java.io.Serializable;

/**
 * Created by dfa on 24/03/2016.
 */
public abstract class EqualsBase implements Serializable {

    public abstract Long getId();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EqualsBase that = (EqualsBase) o;
//        return !(getId() != null ? !getId().equals(that.getId()) : that.getId() != null);
        return getId() == null ? that.getId() == null : getId().equals(that.getId());

    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

}
