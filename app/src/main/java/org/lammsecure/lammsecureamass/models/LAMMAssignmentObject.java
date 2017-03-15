package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Max on 8/3/17.
 *
 * An object for storing a LAMM Security Arduino assignment entry
 */

public class LAMMAssignmentObject implements Parcelable {

    private String mEndDate, mStartDate;
    private HashMap<String, Boolean> mUsers;

    public LAMMAssignmentObject() {}

    public LAMMAssignmentObject(String endDate, String startDate, HashMap<String, Boolean> users) {
        mEndDate = endDate;
        mStartDate = startDate;
        mUsers = users;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public void setEndDate(String endDate) {
        mEndDate = endDate;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public void setStartDate(String startDate) {
        mStartDate = startDate;
    }

    public HashMap<String, Boolean> getUsers() {
        return mUsers;
    }

    public void setUsers(HashMap<String, Boolean> users) {
        mUsers = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMAssignmentObject that = (LAMMAssignmentObject) o;

        if (getEndDate() != null ? !getEndDate().equals(that.getEndDate()) : that.getEndDate() != null)
            return false;
        if (getStartDate() != null ? !getStartDate().equals(that.getStartDate()) : that.getStartDate() != null)
            return false;
        return getUsers() != null ? getUsers().equals(that.getUsers()) : that.getUsers() == null;

    }

    @Override
    public int hashCode() {
        int result = getEndDate() != null ? getEndDate().hashCode() : 0;
        result = 31 * result + (getStartDate() != null ? getStartDate().hashCode() : 0);
        result = 31 * result + (getUsers() != null ? getUsers().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMAssignmentObject{" +
                "mEndDate='" + mEndDate + '\'' +
                ", mStartDate='" + mStartDate + '\'' +
                ", mUsers=" + mUsers +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mEndDate);
        dest.writeString(this.mStartDate);
        dest.writeSerializable(mUsers);
    }

    protected LAMMAssignmentObject(Parcel in) {
        this.mEndDate = in.readString();
        this.mStartDate = in.readString();
        this.mUsers = (HashMap<String, Boolean>) in.readSerializable();
    }

    public static final Parcelable.Creator<LAMMAssignmentObject> CREATOR = new Parcelable.Creator<LAMMAssignmentObject>() {
        public LAMMAssignmentObject createFromParcel(Parcel source) {
            return new LAMMAssignmentObject(source);
        }

        public LAMMAssignmentObject[] newArray(int size) {
            return new LAMMAssignmentObject[size];
        }
    };
}
