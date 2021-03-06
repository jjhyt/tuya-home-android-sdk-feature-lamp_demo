package com.tuya.smart.android.demo.base.presenter;

import android.app.Activity;
import android.graphics.Color;

import com.tuya.smart.android.demo.base.bean.ColorBean;
import com.tuya.smart.android.demo.base.bean.RgbBean;
import com.tuya.smart.android.demo.base.view.ILampView;
import com.tuya.smart.android.mvp.presenter.BasePresenter;
import com.tuya.smart.centralcontrol.TuyaLightDevice;
import com.tuya.smart.sdk.api.IResultCallback;
import com.tuya.smart.sdk.centralcontrol.api.ILightListener;
import com.tuya.smart.sdk.centralcontrol.api.bean.LightDataPoint;
import com.tuya.smart.sdk.centralcontrol.api.constants.LightScene;
import com.tuya.smart.sdk.centralcontrol.parser.bean.LightColourData;

import java.util.HashMap;


/**
 * Created by letian on 15/12/5.
 */
public class LampPresenter extends BasePresenter implements ILightListener {

    protected ILampView mView;
    private TuyaLightDevice mLightDevice;

    private LampOperationFactory mLampOperationColorFactory;
    private boolean mIsOpenLastStatus;

    public LampPresenter(Activity activity, ILampView view, String devId) {
        mView = view;
        mLampOperationColorFactory = new LampOperationColorFactory(activity, mView);
        mLightDevice = new TuyaLightDevice(devId);
        mLightDevice.registerLightListener(this);
        updateLampSwitchStatus(mLightDevice.getLightDataPoint());
    }

    private void startCloseLamp() {
        mLightDevice.powerSwitch(false, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    /**
     * 灯光的开关状态依赖服务端
     */
    private void startOpenLamp() {
        mLightDevice.powerSwitch(true, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
    //添加阅读场景
    private void startYuedu(){
        mLightDevice.scene(LightScene.SCENE_READ, new IResultCallback(){

            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
    //添加工作场景
    public void startGongzuo(){
        mLightDevice.scene(LightScene.SCENE_WORK, new IResultCallback(){

            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
    //添加休闲场景
    public void startXiuxian(){
        mLightDevice.scene(LightScene.SCENE_CASUAL, new IResultCallback(){

            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }
    //添加晚安场景
    public void startWanan(){
        mLightDevice.scene(LightScene.SCENE_GOODNIGHT, new IResultCallback(){

            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }

    //添加调节白光亮度
    public void changeWhiteLight(ColorBean bean) {
        /*mLightDevice.brightness(bean.getWhiteBright(), new IResultCallback() {

            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });*/
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("bright_value", bean.getWhiteBright());
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    private void updateColorModeData(LightDataPoint lightDataPoint) {
        LightColourData data = lightDataPoint.colorHSV;
        updateLampView(changeToRGB(data));
    }


    private RgbBean changeToRGB(LightColourData data) {
        int h = data.getH();
        float[] hsv = new float[]{h / 360.0f, data.getS() / 1000.0f, data.getV() / 1000.0f};
        int color = Color.HSVToColor(255, hsv);
        RgbBean rgbBean = new RgbBean();
        rgbBean.setColor(color);
        return rgbBean;
    }


    public void syncColorToLamp(ColorBean bean) {
        float[] hsv = new float[3];
        Color.colorToHSV(bean.getColor(), hsv);

        mLightDevice.colorHSV((int) hsv[0], (int) (hsv[1] * 100), (int) (hsv[2] * 100), new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });
    }

    public void syncTempToLamp(ColorBean bean) {
  //  应该是更改色温的代码，先写在这，测试使用
            mLightDevice.colorTemperature(bean.getWhite(), new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }


    /**
     * 更新灯泡的开关状态
     * @param lightDataPoint
     */
    private void updateLampSwitchStatus(LightDataPoint lightDataPoint) {
        boolean isOpen = lightDataPoint.powerSwitch;
        if (isOpen != mIsOpenLastStatus) {
            if (isOpen) {
                mView.showLampView();
                mView.showOperationView();
                mLampOperationColorFactory.showOperationView();
            } else {
                mView.hideLampView();
            }
        }
        mIsOpenLastStatus = isOpen;
    }


    public void onClickLampSwitch() {
        boolean isOpen = mLightDevice.getLightDataPoint().powerSwitch;
        if (isOpen) {
            startCloseLamp();
        } else {
            startOpenLamp();
        }
    }


    public void hideOperation() {
        mLampOperationColorFactory.hideOperationView();
    }


    private void updateLampView(RgbBean bean) {
        mLampOperationColorFactory.updateOperationView(bean);
    }


    @Override
    public void onDpUpdate(LightDataPoint lightDataPoint) {
        updateLampSwitchStatus(lightDataPoint);
        updateColorModeData(lightDataPoint);
    }

    @Override
    public void onRemoved() {

    }

    @Override
    public void onStatusChanged(boolean b) {

    }

    @Override
    public void onNetworkStatusChanged(boolean b) {

    }

    @Override
    public void onDevInfoUpdate() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLightDevice.unRegisterDevListener();
    }
    public void onButtonscene1(){
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "scene_1");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }
    public void onButtonscene2(){
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "scene_2");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }
    public void onButtonscene3(){
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "scene_3");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }
    public void onButtonscene(){
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "scene");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });

    }
    public void onButtonwhite(){
        /*HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "white");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });*/
        startYuedu();

    }
    public void onButtoncolor(){
        HashMap<String, Object> dpCodeMap = new HashMap<>();
        dpCodeMap.put("work_mode", "scene_4");
        mLightDevice.publishCommands(dpCodeMap, new IResultCallback() {
            @Override
            public void onError(String code, String error) {

            }

            @Override
            public void onSuccess() {

            }
        });


    }
}
