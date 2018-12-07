package com.tiendas3b.almacen.picking.orders;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by danflo on 26/09/2017 madafaka.
 */

public class ScanTimeDTO implements Parcelable {

    long initTimeMillis;
    long endTimeMillis;
    String initTime;
    String endTime;
    String employee;
    int totalQuantity;
//    private int completeQuantity;
//    private int partialQuantity;
//    private int zeroQuantity;

    public ScanTimeDTO() {
    }

    protected ScanTimeDTO(Parcel in) {
        initTimeMillis = in.readLong();
        endTimeMillis = in.readLong();
        initTime = in.readString();
        endTime = in.readString();
        employee = in.readString();
        totalQuantity = in.readInt();
//        completeQuantity = in.readInt();
//        partialQuantity = in.readInt();
//        zeroQuantity = in.readInt();
    }

    public static final Creator<ScanTimeDTO> CREATOR = new Creator<ScanTimeDTO>() {
        @Override
        public ScanTimeDTO createFromParcel(Parcel in) {
            return new ScanTimeDTO(in);
        }

        @Override
        public ScanTimeDTO[] newArray(int size) {
            return new ScanTimeDTO[size];
        }
    };

    public String getInitTime() {
        return initTime;
    }

    public void setInitTime(String initTime) {
        this.initTime = initTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEmployee() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee = employee;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

//    public int getCompleteQuantity() {
//        return completeQuantity;
//    }
//
//    public void setCompleteQuantity(int completeQuantity) {
//        this.completeQuantity = completeQuantity;
//    }
//
//    public int getPartialQuantity() {
//        return partialQuantity;
//    }
//
//    public void setPartialQuantity(int partialQuantity) {
//        this.partialQuantity = partialQuantity;
//    }
//
//    public int getZeroQuantity() {
//        return zeroQuantity;
//    }
//
//    public void setZeroQuantity(int zeroQuantity) {
//        this.zeroQuantity = zeroQuantity;
//    }

    public long getInitTimeMillis() {
        return initTimeMillis;
    }

    public void setInitTimeMillis(long initTimeMillis) {
        this.initTimeMillis = initTimeMillis;
    }

    public long getEndTimeMillis() {
        return endTimeMillis;
    }

    public void setEndTimeMillis(long endTimeMillis) {
        this.endTimeMillis = endTimeMillis;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(initTimeMillis);
        dest.writeLong(endTimeMillis);
        dest.writeString(initTime);
        dest.writeString(endTime);
        dest.writeString(employee);
        dest.writeInt(totalQuantity);
//        dest.writeInt(completeQuantity);
//        dest.writeInt(partialQuantity);
//        dest.writeInt(zeroQuantity);
    }
}
