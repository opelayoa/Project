package com.tiendas3b.almacen.picking.orders;

import android.os.Parcel;

/**
 * Created by danflo on 26/09/2017 madafaka.
 */

public class PickingScanTimeDTO extends ScanTimeDTO {

    private int completeQuantity;
    private int partialQuantity;
    private int zeroQuantity;

    public PickingScanTimeDTO() {
    }

    protected PickingScanTimeDTO(Parcel in) {
        initTimeMillis = in.readLong();
        endTimeMillis = in.readLong();
        initTime = in.readString();
        endTime = in.readString();
        employee = in.readString();
        totalQuantity = in.readInt();
        completeQuantity = in.readInt();
        partialQuantity = in.readInt();
        zeroQuantity = in.readInt();
    }

    public static final Creator<PickingScanTimeDTO> CREATOR = new Creator<PickingScanTimeDTO>() {
        @Override
        public PickingScanTimeDTO createFromParcel(Parcel in) {
            return new PickingScanTimeDTO(in);
        }

        @Override
        public PickingScanTimeDTO[] newArray(int size) {
            return new PickingScanTimeDTO[size];
        }
    };

    public int getCompleteQuantity() {
        return completeQuantity;
    }

    public void setCompleteQuantity(int completeQuantity) {
        this.completeQuantity = completeQuantity;
    }

    public int getPartialQuantity() {
        return partialQuantity;
    }

    public void setPartialQuantity(int partialQuantity) {
        this.partialQuantity = partialQuantity;
    }

    public int getZeroQuantity() {
        return zeroQuantity;
    }

    public void setZeroQuantity(int zeroQuantity) {
        this.zeroQuantity = zeroQuantity;
    }

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
        dest.writeInt(completeQuantity);
        dest.writeInt(partialQuantity);
        dest.writeInt(zeroQuantity);
    }
}
