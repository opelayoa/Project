package com.tiendas3b.almacen.dto;

/**
 * Created by dfa on 21/07/2016.
 */
public class MeErrorDTO {

    private String memberId;
    private int numErrors;

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public int getNumErrors() {
        return numErrors;
    }

    public void setNumErrors(int numErrors) {
        this.numErrors = numErrors;
    }
}
