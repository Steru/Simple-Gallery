package com.stergiadis.simplegallery.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Steru on 2016-06-29.
 */
public class Image implements Parcelable {
    private String mName;
    private String mPath;

    public Image(){
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String path) {
        this.mPath = path;
    }

    public Image(Parcel in) {
        String[] data = new String[2];
        in.readStringArray(data);
        this.mName = data[0];
        this.mPath = data[1];
    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {this.mName,
                                            this.mPath});
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
