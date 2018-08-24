package com.ebwebtech.rocket;

public class ModelAllUsers {
    private String mImage, mName, mStatus;

    public ModelAllUsers() {
    }

    public ModelAllUsers(String mImage, String mName, String mStatus) {
        this.mImage = mImage;
        this.mName = mName;
        this.mStatus = mStatus;
    }

    public String getmImage() {
        return mImage;
    }

    public void setmImage(String mImage) {
        this.mImage = mImage;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmStatus() {
        return mStatus;
    }

    public void setmStatus(String mStatus) {
        this.mStatus = mStatus;
    }
}
