package com.iskhakovayrat.aiweather.model;

import com.google.gson.annotations.SerializedName;

public class Clouds {

    @SerializedName("all")
    private int value;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
