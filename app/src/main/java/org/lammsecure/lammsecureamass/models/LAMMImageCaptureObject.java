package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Max on 8/3/17.
 *
 * An object for storing a LAMM Security Arduino Image Capture entry
 */

public class LAMMImageCaptureObject implements Parcelable {

    private String mImageStorageAddress, mLatitude, mLongitude, mTimestamp;

    public LAMMImageCaptureObject() {}

    public LAMMImageCaptureObject(String imageStorageAddress, String latitude, String longitude, String timestamp) {
        mImageStorageAddress = imageStorageAddress;
        mLatitude = latitude;
        mLongitude = longitude;
        mTimestamp = timestamp;
    }

    public String getImageStorageAddress() {
        return mImageStorageAddress;
    }

    public void setImageStorageAddress(String imageStorageAddress) {
        mImageStorageAddress = imageStorageAddress;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setTimestamp(String timestamp) {
        mTimestamp = timestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMImageCaptureObject that = (LAMMImageCaptureObject) o;

        if (getImageStorageAddress() != null ? !getImageStorageAddress().equals(that.getImageStorageAddress()) : that.getImageStorageAddress() != null)
            return false;
        if (getLatitude() != null ? !getLatitude().equals(that.getLatitude()) : that.getLatitude() != null)
            return false;
        if (getLongitude() != null ? !getLongitude().equals(that.getLongitude()) : that.getLongitude() != null)
            return false;
        return getTimestamp() != null ? getTimestamp().equals(that.getTimestamp()) : that.getTimestamp() == null;

    }

    @Override
    public int hashCode() {
        int result = getImageStorageAddress() != null ? getImageStorageAddress().hashCode() : 0;
        result = 31 * result + (getLatitude() != null ? getLatitude().hashCode() : 0);
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        result = 31 * result + (getTimestamp() != null ? getTimestamp().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMImageCaptureObject{" +
                "mImageStorageAddress='" + mImageStorageAddress + '\'' +
                ", mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                ", mTimestamp='" + mTimestamp + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mImageStorageAddress);
        dest.writeString(this.mLatitude);
        dest.writeString(this.mLongitude);
        dest.writeString(this.mTimestamp);
    }

    protected LAMMImageCaptureObject(Parcel in) {
        this.mImageStorageAddress = in.readString();
        this.mLatitude = in.readString();
        this.mLongitude = in.readString();
        this.mTimestamp = in.readString();
    }

    public static final Creator<LAMMImageCaptureObject> CREATOR = new Creator<LAMMImageCaptureObject>() {
        public LAMMImageCaptureObject createFromParcel(Parcel source) {
            return new LAMMImageCaptureObject(source);
        }

        public LAMMImageCaptureObject[] newArray(int size) {
            return new LAMMImageCaptureObject[size];
        }
    };
}
