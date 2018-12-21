package com.tvscs.ilead.model;

import java.io.Serializable;

public class LegendModel implements Serializable {

    private String legendName;

    private int legendColor;

    public String getLegendName() {
        return legendName;
    }

    public void setLegendName(String legendName) {
        this.legendName = legendName;
    }

    public int getLegendColor() {
        return legendColor;
    }

    public void setLegendColor(int legendColor) {
        this.legendColor = legendColor;
    }
}
