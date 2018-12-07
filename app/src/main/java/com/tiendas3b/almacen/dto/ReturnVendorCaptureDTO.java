package com.tiendas3b.almacen.dto;

import java.io.Serializable;

/**
 * Created by dfa on 12/05/2016.
 */
public class ReturnVendorCaptureDTO extends ArticleCaptureDTO implements Serializable{

    private long providerId;
    private Long ivaId;

    public Long getIvaId() {
        return ivaId;
    }

    public void setIvaId(Long ivaId) {
        this.ivaId = ivaId;
    }

    public Long getProviderId() {
        return providerId;
    }

    public void setProviderId(long providerId) {
        this.providerId = providerId;
    }
}
