package com.tiendas3b.almacen.views.tables.vo;

import java.io.Serializable;

/**
 * Created by dfa on 28/06/2016.
 */
public class ReturnVendorArticleVO extends ArticleVO implements Serializable{

    private long providerId;
    private long ivaId;

    public long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }

    public long getIvaId() {
        return ivaId;
    }

    public void setIvaId(long ivaId) {
        this.ivaId = ivaId;
    }
}
