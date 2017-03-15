package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


/**
 * Created by Max on 7/3/17.
 *
 * An object for storing a LAMM Security user account entry
 */

public class LAMMAccountObject implements Parcelable {

    private String mAccountType, mAddress;
    private HashMap<String, Boolean> mArduinos, mDrivers, mManagers;
    private String mPhoneNumber;

    public LAMMAccountObject() {}

    public LAMMAccountObject(String accountType, String address, HashMap<String, Boolean> arduinos, HashMap<String, Boolean> drivers, HashMap<String, Boolean> managers, String phoneNumber) {
        mAccountType = accountType;
        mAddress = address;
        mArduinos = arduinos;
        mDrivers = drivers;
        mManagers = managers;
        mPhoneNumber = phoneNumber;
    }

    public String getAccountType() {
        return mAccountType;
    }

    public void setAccountType(String accountType) {
        mAccountType = accountType;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public HashMap<String, Boolean> getArduinos() {
        return mArduinos;
    }

    public void setArduinos(HashMap<String, Boolean> arduinos) {
        mArduinos = arduinos;
    }

    public HashMap<String, Boolean> getDrivers() {
        return mDrivers;
    }

    public void setDrivers(HashMap<String, Boolean> drivers) {
        mDrivers = drivers;
    }

    public HashMap<String, Boolean> getManagers() {
        return mManagers;
    }

    public void setManagers(HashMap<String, Boolean> managers) {
        mManagers = managers;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        LAMMAccountObject that = (LAMMAccountObject) o;

        if (getAccountType() != null ? !getAccountType().equals(that.getAccountType()) : that.getAccountType() != null)
            return false;
        if (getAddress() != null ? !getAddress().equals(that.getAddress()) : that.getAddress() != null)
            return false;
        if (getArduinos() != null ? !getArduinos().equals(that.getArduinos()) : that.getArduinos() != null)
            return false;
        if (getDrivers() != null ? !getDrivers().equals(that.getDrivers()) : that.getDrivers() != null)
            return false;
        if (getManagers() != null ? !getManagers().equals(that.getManagers()) : that.getManagers() != null)
            return false;
        return getPhoneNumber() != null ? getPhoneNumber().equals(that.getPhoneNumber()) : that.getPhoneNumber() == null;

    }

    @Override
    public int hashCode() {
        int result = getAccountType() != null ? getAccountType().hashCode() : 0;
        result = 31 * result + (getAddress() != null ? getAddress().hashCode() : 0);
        result = 31 * result + (getArduinos() != null ? getArduinos().hashCode() : 0);
        result = 31 * result + (getDrivers() != null ? getDrivers().hashCode() : 0);
        result = 31 * result + (getManagers() != null ? getManagers().hashCode() : 0);
        result = 31 * result + (getPhoneNumber() != null ? getPhoneNumber().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMAccountObject{" +
                "mAccountType='" + mAccountType + '\'' +
                ", mAddress='" + mAddress + '\'' +
                ", mArduinos=" + mArduinos +
                ", mDrivers=" + mDrivers +
                ", mManagers=" + mManagers +
                ", mPhoneNumber='" + mPhoneNumber + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mAccountType);
        dest.writeString(this.mAddress);
        dest.writeSerializable(mArduinos);
        dest.writeSerializable(mDrivers);
        dest.writeSerializable(mManagers);
        dest.writeString(this.mPhoneNumber);
    }

    protected LAMMAccountObject(Parcel in) {
        this.mAccountType = in.readString();
        this.mAddress = in.readString();
        this.mArduinos = (HashMap<String, Boolean>) in.readSerializable();
        this.mDrivers = (HashMap<String, Boolean>) in.readSerializable();
        this.mManagers = (HashMap<String, Boolean>) in.readSerializable();
        this.mPhoneNumber = in.readString();
    }

    public static final Creator<LAMMAccountObject> CREATOR = new Creator<LAMMAccountObject>() {
        public LAMMAccountObject createFromParcel(Parcel source) {
            return new LAMMAccountObject(source);
        }

        public LAMMAccountObject[] newArray(int size) {
            return new LAMMAccountObject[size];
        }
    };
}
