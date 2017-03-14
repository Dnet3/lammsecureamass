package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;

/**
 * Created by Max on 8/3/17.
 *
 * An object for storing a LAMM Security Arduino entry
 */

public class LAMMArduinoObject implements Parcelable {

    private HashMap<String, Boolean> mAccounts;
    private String mActivationDate, mDecommissionDate, mName;

    public LAMMArduinoObject() {}

    public LAMMArduinoObject(HashMap<String, Boolean> accounts, String activationDate, String decommissionDate, String name) {
        mAccounts = accounts;
        mActivationDate = activationDate;
        mDecommissionDate = decommissionDate;
        mName = name;
    }

    public HashMap<String, Boolean> getAccounts() {
        return mAccounts;
    }

    public void setAccounts(HashMap<String, Boolean> accounts) {
        mAccounts = accounts;
    }

    public String getActivationDate() {
        return mActivationDate;
    }

    public void setActivationDate(String activationDate) {
        mActivationDate = activationDate;
    }

    public String getDecommissionDate() {
        return mDecommissionDate;
    }

    public void setDecommissionDate(String decommissionDate) {
        mDecommissionDate = decommissionDate;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMArduinoObject that = (LAMMArduinoObject) o;

        if (getAccounts() != null ? !getAccounts().equals(that.getAccounts()) : that.getAccounts() != null)
            return false;
        if (getActivationDate() != null ? !getActivationDate().equals(that.getActivationDate()) : that.getActivationDate() != null)
            return false;
        if (getDecommissionDate() != null ? !getDecommissionDate().equals(that.getDecommissionDate()) : that.getDecommissionDate() != null)
            return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getAccounts() != null ? getAccounts().hashCode() : 0;
        result = 31 * result + (getActivationDate() != null ? getActivationDate().hashCode() : 0);
        result = 31 * result + (getDecommissionDate() != null ? getDecommissionDate().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMArduinoObject{" +
                "mAccounts=" + mAccounts +
                ", mActivationDate='" + mActivationDate + '\'' +
                ", mDecommissionDate='" + mDecommissionDate + '\'' +
                ", mName='" + mName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeSerializable(mAccounts);
        dest.writeString(this.mActivationDate);
        dest.writeString(this.mDecommissionDate);
        dest.writeString(this.mName);
    }

    protected LAMMArduinoObject(Parcel in) {
        this.mAccounts = (HashMap<String, Boolean>) in.readSerializable();
        this.mActivationDate = in.readString();
        this.mDecommissionDate = in.readString();
        this.mName = in.readString();
    }

    public static final Creator<LAMMArduinoObject> CREATOR = new Creator<LAMMArduinoObject>() {
        public LAMMArduinoObject createFromParcel(Parcel source) {
            return new LAMMArduinoObject(source);
        }

        public LAMMArduinoObject[] newArray(int size) {
            return new LAMMArduinoObject[size];
        }
    };
}
