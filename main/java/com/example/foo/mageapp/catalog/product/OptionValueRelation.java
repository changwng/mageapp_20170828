package com.example.foo.mageapp.catalog.product;

import java.util.List;

/**
 * Created by foo on 9/2/17.
 */

public class OptionValueRelation {

    protected String mTo;
    protected List<OptionValue> mValues;
    protected boolean mDefaultValueSet = false;

    public String getTo() {
        return mTo;
    }

    public void setTo(String to) {
        mTo = to;
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
