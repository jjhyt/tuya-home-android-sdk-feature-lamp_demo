package com.tuya.smart.android.demo.base.bean;

/**
 * Created by letian on 15/12/8.
 */
public class ColorBean extends RgbBean {
    private int white;
    private int bright;
    private int value;
    private int saturation;

    public int getWhite() {
        return white;
    }

    public void setWhite(int white) {
        this.white = white;
    }

    public int getWhiteBright() {
        return bright;
    }

    public void setWhiteBright(int bright) {
        this.bright = bright;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSaturation() {
        return saturation;
    }

    public void setSaturation(int saturation) {
        this.saturation = saturation;
    }


}
