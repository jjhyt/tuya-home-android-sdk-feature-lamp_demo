package com.tuya.smart.android.demo.base.presenter;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import com.tuya.smart.android.common.utils.L;
import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.bean.ColorBean;
import com.tuya.smart.android.demo.base.bean.RgbBean;
import com.tuya.smart.android.demo.base.view.ILampView;
import com.tuya.smart.android.demo.base.widget.ColorPicker;
import com.tuya.smart.android.demo.base.widget.LampView;

import butterknife.BindView;
import butterknife.ButterKnife;
/**
 * Created by letian on 15/12/10.  注意这里面的setValue getValue 白光采光亮度共用的，不知道有没有什么Bug
 */
public class LampOperationColorFactory implements LampOperationFactory, SeekBar.OnSeekBarChangeListener {
//定义拖动条和ID绑定
    @BindView(R.id.sb_lamp_color_lighting)
    public SeekBar mLightBar;
    @BindView(R.id.sb_lamp_white_lighting)
    public SeekBar mWhiteLightBar;
    @BindView(R.id.sb_lamp_color_saturation)
    public SeekBar mSaturationBar;
    @BindView(R.id.sb_lamp_colorlengnuan)
    public SeekBar mLengnuanBar;

//定义拖动条最小值和最大值
    private static final int S_COLOR_LIGHT_MIN_COLOR = 11;
    private static final int S_WHITE_LIGHT_MIN = 25;
    private static final int S_COLOR_SATURATION_MAX = 255;
    private static final int S_COLOR_LIGHT_MAX = S_COLOR_SATURATION_MAX ;
    private static final int S_LENGNUAN_MAX= 100;
    private static final int S_WHITE_LIGHT_MAX= 230;

    @BindView(R.id.ll_lamp_color_lighting)
    public View mLightView;

    @BindView(R.id.tv_lamp_color_mode)
    public TextView mLampModeViewTip;


    @BindView(R.id.picker)
    public LampView mLampView;

    public ILampView mView;

    public LampOperationColorFactory(Activity activity, ILampView lampView) {
        mView = lampView;
        initView(activity);
        initSeekBar();
        initListener();
    }

    private void initListener() {
        mLampView.setOnColorSelectedListener(new ColorPicker.OnColorSelectedListener() {
            @Override
            public void onColorSelected(int color) {
                sendLampColor();
            }
        });


    }

    //初始化拖动条
    private void initSeekBar() {
        mLightBar.setOnSeekBarChangeListener(this);
        mLightBar.setMax(S_COLOR_LIGHT_MAX);
        mWhiteLightBar.setOnSeekBarChangeListener(this);
        mWhiteLightBar.setMax(S_WHITE_LIGHT_MAX);
        mSaturationBar.setOnSeekBarChangeListener(this);
        mSaturationBar.setMax(S_COLOR_LIGHT_MAX);
        mLengnuanBar.setOnSeekBarChangeListener(this);
        mLengnuanBar.setMax(S_LENGNUAN_MAX);
//        mLengnuanBar.setMin(S_LENGNUAN_MIN);
        mLightBar.setProgress(S_COLOR_LIGHT_MAX);
        mWhiteLightBar.setProgress(S_WHITE_LIGHT_MAX);
        mSaturationBar.setProgress(S_COLOR_LIGHT_MAX);
        mLengnuanBar.setProgress(S_COLOR_LIGHT_MAX);

    }

    private void initView(Activity activity) {
        ButterKnife.bind(this, activity);
    }

    @Override
    public void showOperationView() {
        mLampModeViewTip.setVisibility(View.VISIBLE);
        mLightView.setVisibility(View.VISIBLE);
        mLampModeViewTip.setText(R.string.lamp_color_mode);
        mLampView.showColorWheel();
        PuzzleAnimation puzzleAnimation = new PuzzleAnimation(0.5f, 1f);
        puzzleAnimation.setDuration(500);
        mLampView.setCenterCircleColor(Color.parseColor("#EAEAEA"));
        mLampView.startAnimation(puzzleAnimation);
        mLampView.setCenterCircleLayer(1);
    }

    @Override
    public void hideOperationView() {

        mLightView.setVisibility(View.GONE);
        mLampModeViewTip.setVisibility(View.GONE);

        PuzzleAnimation puzzleAnimation = new PuzzleAnimation(1f, 0.5f);
        puzzleAnimation.setDuration(300);
        puzzleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mLampView.hideColorWheel();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mLampView.startAnimation(puzzleAnimation);
    }

    //出界面里设定拖动条的值和颜色值
    @Override
    public void updateOperationView(RgbBean bean) {
            mLightBar.setProgress(((ColorBean) bean).getValue() - S_COLOR_LIGHT_MIN_COLOR);
            mSaturationBar.setProgress(((ColorBean) bean).getSaturation());
            mLengnuanBar.setProgress(((ColorBean) bean).getWhite());
            mWhiteLightBar.setProgress(((ColorBean) bean).getValue() + S_WHITE_LIGHT_MIN);
            mLampView.setColor(bean.getColor());
            if (mLampView.getVisibility() == View.GONE) {
                mLampView.setVisibility(View.VISIBLE);
            }
    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }
//手指滑动结束，这里发送颜色值。
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        //判断是否冷暖进度条拖动,是则设置冷暖光,不是则设置颜色
        switch(seekBar.getId()) {
            case R.id.sb_lamp_colorlengnuan:{
                sendtempColor();
                break;
            }
            case R.id.sb_lamp_white_lighting:{
                sendwhiteLight();
                break;
            }
            default:
                sendLampColor();
                break;
        }

    }

    private void sendLampColor() {
        ColorBean bean = new ColorBean();
        bean.setColor(mView.getLampColor());

        bean.setValue(mLightBar.getProgress() + S_COLOR_LIGHT_MIN_COLOR);
        bean.setSaturation(mSaturationBar.getProgress());

        int lampColor = mView.getLampColor();
        float[] hsv = new float[3];
        Color.colorToHSV(lampColor, hsv);
        hsv[2] =(mLightBar.getProgress() / 255.0f);
        hsv[1] = (mSaturationBar.getProgress() / 255.0f);
        int color = Color.HSVToColor(hsv);
        bean.setColor(color);
        mView.sendLampColor(bean);

    }
    private void sendtempColor() {
        ColorBean bean = new ColorBean();

        //发送冷暖光的值
        bean.setWhite(mLengnuanBar.getProgress());
        mView.sendTempColor(bean);
    }
    private void sendwhiteLight() {
        ColorBean bean = new ColorBean();

        //发送亮度的值
        bean.setWhiteBright(mWhiteLightBar.getProgress() + S_WHITE_LIGHT_MIN);
        mView.sendWhiteLight(bean);
    }


    public class PuzzleAnimation extends Animation {
        private static final String TAG = "PuzzleAnimation";
        private float mStartScale;
        private float mEndScale;

        public PuzzleAnimation(float start, float end) {
            mStartScale = start;
            mEndScale = end;
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            L.d(TAG, "interoplateTime" + interpolatedTime);
            mLampView.setColorWheelScale(mStartScale + ((mEndScale - mStartScale) * interpolatedTime));
            ViewCompat.postInvalidateOnAnimation(mLampView);
        }
    }
}
