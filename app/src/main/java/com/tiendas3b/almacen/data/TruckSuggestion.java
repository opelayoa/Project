package com.tiendas3b.almacen.data;

import android.os.Parcel;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 * Created by dfa on 26/08/2016.
 */
public class TruckSuggestion implements SearchSuggestion {

    private long id;
    private String mTruckName;
    private boolean mIsHistory = false;

    public TruckSuggestion(long id, String suggestion) {
        this.id = id;
        this.mTruckName = suggestion;
    }

    public TruckSuggestion(Parcel source) {
        this.id = source.readLong();
        this.mTruckName = source.readString();
        this.mIsHistory = source.readInt() != 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIsHistory(boolean isHistory) {
        this.mIsHistory = isHistory;
    }

    public boolean getIsHistory() {
        return this.mIsHistory;
    }

    @Override
    public String getBody() {
        return mTruckName;
    }

    public static final Creator<TruckSuggestion> CREATOR = new Creator<TruckSuggestion>() {
        @Override
        public TruckSuggestion createFromParcel(Parcel in) {
            return new TruckSuggestion(in);
        }

        @Override
        public TruckSuggestion[] newArray(int size) {
            return new TruckSuggestion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(mTruckName);
        dest.writeInt(mIsHistory ? 1 : 0);
    }
}