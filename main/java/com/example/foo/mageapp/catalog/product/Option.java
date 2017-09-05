package com.example.foo.mageapp.catalog.product;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by foo on 9/2/17.
 */

public class Option {
    protected String mCode;
    protected String mType;
    protected String mLabel;
    protected String mPrice;
    protected String mFormatedPrice;
    protected List<OptionValue> mValues = new ArrayList<>();
    protected String mIsRequred;
    protected boolean mRequired;
    protected boolean mDefaultValueSet = false;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
    }

    public String getType() {
        return mType;
    }

    public void setType(String type) {
        mType = type;
    }

    public String getLabel() {
        return mLabel;
    }

    public void setLabel(String label) {
        mLabel = label;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    public String getFormatedPrice() {
        return mFormatedPrice;
    }

    public void setFormatedPrice(String formatedPrice) {
        mFormatedPrice = formatedPrice;
    }

    public String getIsRequred() {
        return mIsRequred;
    }

    public void setIsRequred(String isRequred) {
        mIsRequred = isRequred;
        if ((isRequred != null) && isRequred.equals("1")) {
            mRequired = true;
        } else {
            mRequired = false;
        }
    }

    public boolean isRequired() {
        return mRequired;
    }

    public void setRequired(boolean required) {
        mRequired = required;
    }

    public List<OptionValue> getValues() {
        return mValues;
    }

    public void setValues(List<OptionValue> values) {
        mValues = values;
    }

    public List<OptionValue> getSpinnerValues() {
        if (!mDefaultValueSet) {
            OptionValue val = new OptionValue();
            val.setLabel("Please Select");
            mValues.add(0, val);
            mDefaultValueSet = true;
        }
        return mValues;
    }
}
