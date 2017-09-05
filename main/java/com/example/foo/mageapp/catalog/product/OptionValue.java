package com.example.foo.mageapp.catalog.product;

/**
 * Created by foo on 9/2/17.
 */

public class OptionValue {
    protected String mCode;
    protected String mLabel;
    protected String mPrice;
    protected String mFormattedPrice;
    protected OptionValueRelation mRelation;

    public String getCode() {
        return mCode;
    }

    public void setCode(String code) {
        mCode = code;
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

    public String getFormattedPrice() {
        return mFormattedPrice;
    }

    public void setFormattedPrice(String formattedPrice) {
        mFormattedPrice = formattedPrice;
    }

    public OptionValueRelation getRelation() {
        return mRelation;
    }

    public void setRelation(OptionValueRelation relation) {
        mRelation = relation;
    }
}
