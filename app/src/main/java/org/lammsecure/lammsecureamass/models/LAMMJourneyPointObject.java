package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Max on 8/3/17.
 *
 * An object for storing a LAMM Security Arduino jounrey point entry
 */

public class LAMMJourneyPointObject implements Parcelable {

    private String mLatitude, mLongitude;

    public LAMMJourneyPointObject() {}

    public LAMMJourneyPointObject(String latitude, String longitude) {
        mLatitude = latitude;
        mLongitude = longitude;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMJourneyPointObject that = (LAMMJourneyPointObject) o;

        if (getLatitude() != null ? !getLatitude().equals(that.getLatitude()) : that.getLatitude() != null)
            return false;
        return getLongitude() != null ? getLongitude().equals(that.getLongitude()) : that.getLongitude() == null;

    }

    @Override
    public int hashCode() {
        int result = getLatitude() != null ? getLatitude().hashCode() : 0;
        result = 31 * result + (getLongitude() != null ? getLongitude().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMJourneyPointObject{" +
                "mLatitude='" + mLatitude + '\'' +
                ", mLongitude='" + mLongitude + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mLatitude);
        dest.writeString(this.mLongitude);
    }

    protected LAMMJourneyPointObject(Parcel in) {
        this.mLatitude = in.readString();
        this.mLongitude = in.readString();
    }

    public static final Creator<LAMMJourneyPointObject> CREATOR = new Creator<LAMMJourneyPointObject>() {
        public LAMMJourneyPointObject createFromParcel(Parcel source) {
            return new LAMMJourneyPointObject(source);
        }

        public LAMMJourneyPointObject[] newArray(int size) {
            return new LAMMJourneyPointObject[size];
        }
    };
}
