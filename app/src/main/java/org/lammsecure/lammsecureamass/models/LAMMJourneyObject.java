package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Max on 8/3/17.
 *
 * An object for storing a LAMM Security Arduino journey entry
 */

public class LAMMJourneyObject implements Parcelable {

    private String mEndTime;
    private ArrayList<LAMMJourneyPointObject> mPoints;
    private String mStartTime;

    public LAMMJourneyObject() {}

    public LAMMJourneyObject(String endTime, ArrayList<LAMMJourneyPointObject> points, String startTime) {
        mEndTime = endTime;
        mPoints = points;
        mStartTime = startTime;
    }

    public String getEndTime() {
        return mEndTime;
    }

    public void setEndTime(String endTime) {
        mEndTime = endTime;
    }

    public ArrayList<LAMMJourneyPointObject> getPoints() {
        return mPoints;
    }

    public void setPoints(ArrayList<LAMMJourneyPointObject> points) {
        mPoints = points;
    }

    public String getStartTime() {
        return mStartTime;
    }

    public void setStartTime(String startTime) {
        mStartTime = startTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMJourneyObject that = (LAMMJourneyObject) o;

        if (getEndTime() != null ? !getEndTime().equals(that.getEndTime()) : that.getEndTime() != null)
            return false;
        if (getPoints() != null ? !getPoints().equals(that.getPoints()) : that.getPoints() != null)
            return false;
        return getStartTime() != null ? getStartTime().equals(that.getStartTime()) : that.getStartTime() == null;

    }

    @Override
    public int hashCode() {
        int result = getEndTime() != null ? getEndTime().hashCode() : 0;
        result = 31 * result + (getPoints() != null ? getPoints().hashCode() : 0);
        result = 31 * result + (getStartTime() != null ? getStartTime().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMJourneyObject{" +
                "mEndTime='" + mEndTime + '\'' +
                ", mPoints=" + mPoints +
                ", mStartTime='" + mStartTime + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mEndTime);
        dest.writeList(this.mPoints);
        dest.writeString(this.mStartTime);
    }

    protected LAMMJourneyObject(Parcel in) {
        this.mEndTime = in.readString();
        mPoints = new ArrayList<>();
        this.mPoints = in.readArrayList(LAMMJourneyPointObject.class.getClassLoader());
        this.mStartTime = in.readString();
    }

    public static final Creator<LAMMJourneyObject> CREATOR = new Creator<LAMMJourneyObject>() {
        public LAMMJourneyObject createFromParcel(Parcel source) {
            return new LAMMJourneyObject(source);
        }

        public LAMMJourneyObject[] newArray(int size) {
            return new LAMMJourneyObject[size];
        }
    };
}
