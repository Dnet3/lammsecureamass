package org.lammsecure.lammsecureamass.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Max on 21/2/17.
 *
 * An object for storing a LAMM Security user object entry
 *
 * Implements Parcelable so as to allow this custom object to be passed in
 * an Android Intent.
 */

public class LAMMUserObject implements Parcelable {

    private String mUsername;
    private ArrayList<Integer> mArduinos;
    private String mDateJoined, mEmailAddress;
    private ArrayList<String> mGroups;
    private String mRealName, mUniqueUserIdentifier;

    public LAMMUserObject(String username, ArrayList<Integer> arduinos, String dateJoined, String emailAddress, ArrayList<String> groups, String realName, String uniqueUserIdentifier) {
        mUsername = username;
        mArduinos = arduinos;
        mDateJoined = dateJoined;
        mEmailAddress = emailAddress;
        mGroups = groups;
        mRealName = realName;
        mUniqueUserIdentifier = uniqueUserIdentifier;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public ArrayList<Integer> getArduinos() {
        return mArduinos;
    }

    public void setArduinos(ArrayList<Integer> arduinos) {
        mArduinos = arduinos;
    }

    public String getDateJoined() {
        return mDateJoined;
    }

    public void setDateJoined(String dateJoined) {
        mDateJoined = dateJoined;
    }

    public String getEmailAddress() {
        return mEmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        mEmailAddress = emailAddress;
    }

    public ArrayList<String> getGroups() {
        return mGroups;
    }

    public void setGroups(ArrayList<String> groups) {
        mGroups = groups;
    }

    public String getRealName() {
        return mRealName;
    }

    public void setRealName(String realName) {
        mRealName = realName;
    }

    public String getUniqueUserIdentifier() {
        return mUniqueUserIdentifier;
    }

    public void setUniqueUserIdentifier(String uniqueUserIdentifier) {
        mUniqueUserIdentifier = uniqueUserIdentifier;
    }

    @Override
    public String toString() {
        return "LAMMUserObject{" +
                "mUsername='" + mUsername +
                ", mArduinos=" + mArduinos + '\'' +
                ", mDateJoined='" + mDateJoined + '\'' +
                ", mEmailAddress='" + mEmailAddress + '\'' +
                ", mGroups=" + mGroups +
                ", mRealName='" + mRealName + '\'' +
                ", mUniqueUserIdentifier='" + mUniqueUserIdentifier + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(this.mUsername);
        dest.writeSerializable(this.mArduinos);
        dest.writeString(this.mDateJoined);
        dest.writeString(this.mEmailAddress);
        dest.writeSerializable(this.mGroups);
        dest.writeString(this.mRealName);
        dest.writeString(this.mUniqueUserIdentifier);
    }

    protected LAMMUserObject(Parcel in) {
        this.mUsername = in.readString();
        this.mArduinos = (ArrayList<Integer>) in.readSerializable();
        this.mDateJoined = in.readString();
        this.mEmailAddress = in.readString();
        this.mGroups = (ArrayList<String>) in.readSerializable();
        this.mRealName = in.readString();
        this.mUniqueUserIdentifier = in.readString();
    }

    public static final Parcelable.Creator<LAMMUserObject> CREATOR = new Parcelable.Creator<LAMMUserObject>() {
        public LAMMUserObject createFromParcel(Parcel source) {
            return new LAMMUserObject(source);
        }

        public LAMMUserObject[] newArray(int size) {
            return new LAMMUserObject[size];
        }
    };
}
