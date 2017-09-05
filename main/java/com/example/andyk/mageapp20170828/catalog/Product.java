package com.example.andyk.mageapp20170828.catalog;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by foo on 5/29/17.
 */

public class Product implements Parcelable {

    protected String mId;
    protected String mName;
    protected String mLink;
    protected String mIcon;
    protected String mPrice;
    protected String mSpecialPrice;

    public static final Parcelable.Creator<Product> CREATOR
              = new Parcelable.Creator<Product>() {
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    private Product(Parcel in) {
        mId = in.readString();
        mName = in.readString();
        mLink = in.readString();
        mIcon = in.readString();
        mPrice = in.readString();
        mSpecialPrice = in.readString();
    }

    public Product() {
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String link) {
        mLink = link;
    }

    public String getIcon() {
        return mIcon;
    }

    public void setIcon(String icon) {
        mIcon = icon;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getSpecialPrice() {
        return mSpecialPrice;
    }

    public void setSpecialPrice(String specialPrice) {
        mSpecialPrice = specialPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mName);
        dest.writeString(mLink);
        dest.writeString(mIcon);
        dest.writeString(mPrice);
        dest.writeString(mSpecialPrice);
    }
}