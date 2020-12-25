package com.tuya.smart.android.demo.base.view;


import com.tuya.smart.android.demo.base.bean.ColorBean;

/**
 * Created by letian on 15/12/5.
 */
public interface ILampView {

    void showLampView();

    void hideLampView();

    int getLampColor();

    int getLampOriginalColor();

    void setLampColor(int color);

    void setLampColorWithNoMove(int color);

    /**
     * 显示操作栏背景
     */
    void showOperationView();

    void sendLampColor(ColorBean bean);

    void sendTempColor(ColorBean bean);

}
