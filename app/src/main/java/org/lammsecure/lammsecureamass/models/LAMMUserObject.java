package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;


/**
 * Created by Max on 21/2/17.
 *
 * An object for storing a LAMM Security User entry
 */

public class LAMMUserObject implements Parcelable {

    private HashMap<String, Boolean> mAccounts;
    private String mEmailAddress, mName;

    public LAMMUserObject() {}

    public LAMMUserObject(HashMap<String, Boolean> accounts, String emailAddress, String name) {
        mAccounts = accounts;
        mEmailAddress = emailAddress;
        mName = name;
    }

    public HashMap<String, Boolean> getAccounts() {
        return mAccounts;
    }

    public void setAccounts(HashMap<String, Boolean> accounts) {
        mAccounts = accounts;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
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

        LAMMUserObject that = (LAMMUserObject) o;

        if (getAccounts() != null ? !getAccounts().equals(that.getAccounts()) : that.getAccounts() != null)
            return false;
        if (getEmailAddress() != null ? !getEmailAddress().equals(that.getEmailAddress()) : that.getEmailAddress() != null)
            return false;
        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;

    }

    @Override
    public int hashCode() {
        int result = getAccounts() != null ? getAccounts().hashCode() : 0;
        result = 31 * result + (getEmailAddress() != null ? getEmailAddress().hashCode() : 0);
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "LAMMUserObject{" +
                "mAccounts=" + mAccounts +
                ", mEmailAddress='" + mEmailAddress + '\'' +
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
        dest.writeString(this.mEmailAddress);
        dest.writeString(this.mName);
    }

    protected LAMMUserObject(Parcel in) {
        this.mAccounts = (HashMap<String, Boolean>) in.readSerializable();
        this.mEmailAddress = in.readString();
        this.mName = in.readString();
    }

    public static final Creator<LAMMUserObject> CREATOR = new Creator<LAMMUserObject>() {
        public LAMMUserObject createFromParcel(Parcel source) {
            return new LAMMUserObject(source);
        }

        public LAMMUserObject[] newArray(int size) {
            return new LAMMUserObject[size];
        }
    };
}
