package com.tuya.smart.android.demo.device;

import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.tuya.smart.android.demo.R;
import com.tuya.smart.android.demo.base.activity.BaseActivity;
import com.tuya.smart.android.demo.base.bean.ColorBean;
import com.tuya.smart.android.demo.base.presenter.LampPresenter;
import com.tuya.smart.android.demo.base.utils.AnimationUtil;
import com.tuya.smart.android.demo.base.view.ILampView;
import com.tuya.smart.android.demo.base.widget.ColorPicker;
import com.tuya.smart.android.demo.base.widget.LampView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LampActivity extends BaseActivity implements ILampView {

    private static final String TAG = "LampActivity";
    protected LampPresenter mLampPresenter;

    @BindView(R.id.picker)
    public LampView mLampView;

    @BindView(R.id.iv_lamp_close)
    public ImageView mLampSwitchButton;

    @BindView(R.id.iv_lamp_light)
    public ImageView mLampCloseLight;

    @BindView(R.id.tv_lamp_operationTip)
    public TextView mLampViewTip;

    @BindView(R.id.tv_lamp_color_mode)
    public TextView mLampModeViewTip;


    @BindView(R.id.fl_lamp_white_operation)
    public View mOperationView;

    //定义拖动条和ID绑定
    @BindView(R.id.sb_lamp_white_lighting)
    public SeekBar mWhiteBar;

    //绑定按钮ID
    @BindView(R.id.button_scene_1)
    public TextView mButtonscene1;
    @BindView(R.id.button_scene_2)
    public TextView mButtonscene2;
    @BindView(R.id.button_scene_3)
    public TextView mButtonscene3;
    @BindView(R.id.button_scene_scene)
    public TextView mButtonscene;
    @BindView(R.id.button_scene_white)
    public TextView mButtonwhite;
    @BindView(R.id.button_scene_color)
    public TextView mButtoncolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lamp);

        initToolbar();
        initMenu();
        initView();
        initPresenter();

//添加按钮事件
        mButtonscene1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.startWanan();
               // mButtonscene1.setText("SUCC");
            }
        });
        mButtonscene2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.onButtonscene2();
            }
        });
        mButtonscene3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.onButtonscene3();
            }
        });
        mButtonscene.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.startXiuxian();
            }
        });
        mButtonwhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.onButtonwhite();
            }
        });
        mButtoncolor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLampPresenter.startGongzuo();
            }
        });


    }

    private void initView() {
        ButterKnife.bind(this);
    }

    protected void initPresenter() {
        mLampPresenter = new LampPresenter(this, this, getIntent().getStringExtra(SwitchActivity.INTENT_DEVID));
    }

    protected void initMenu() {
        setDisplayHomeAsUpEnabled();
    }

    @Override
    public void showLampView() {
        if (mLampView.getVisibility() != View.VISIBLE) {
            mLampView.setVisibility(View.VISIBLE);
        }
        mLampSwitchButton.setImageResource(R.drawable.transpant_bg);
        if (mLampCloseLight.getVisibility() != View.GONE) {
            mLampCloseLight.setVisibility(View.GONE);
        }
        if (mLampModeViewTip.getVisibility() != View.GONE) {
            mLampModeViewTip.setVisibility(View.GONE);
        }
        if (mLampViewTip.getVisibility() != View.VISIBLE) {
            mLampViewTip.setVisibility(View.VISIBLE);
        }
        mLampViewTip.setText(R.string.lamp_close_tip);
    }

    @Override
    public void hideLampView() {
        if (mLampView.getVisibility() != View.GONE) {
            mLampView.setVisibility(View.GONE);
        }
        mLampSwitchButton.setImageResource(R.drawable.transpant_bg);
        mLampCloseLight.setVisibility(View.VISIBLE);
        mLampViewTip.setVisibility(View.VISIBLE);
        mLampViewTip.setText(R.string.lamp_open_tip);
        if (mLampModeViewTip.getVisibility() != View.GONE) {
            mLampModeViewTip.setVisibility(View.GONE);
        }
        mLampPresenter.hideOperation();
        mOperationView.setVisibility(View.GONE);
    }


    @Override
    public int getLampColor() {
        return mLampView.getColor();
    }

    @Override
    public int getLampOriginalColor() {
        return mLampView.getOriginalColor();
    }

    @Override
    public void setLampColor(int color) {
        mLampView.setColor(color);
    }

    @Override
    public void setLampColorWithNoMove(int color) {
        mLampView.setColorWithNoAngle(color, ColorPicker.S_RESPONING);
    }

    @Override
    public void showOperationView() {
        mOperationView.setVisibility(View.VISIBLE);
        AnimationUtil.translateView(mOperationView, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f,
            Animation.RELATIVE_TO_SELF, 0f, 300, false, null);
    }


    @Override
    public void sendLampColor(ColorBean bean) {
        mLampPresenter.syncColorToLamp(bean);
    }

    @Override
    public void sendTempColor(ColorBean bean) {
        mLampPresenter.syncTempToLamp(bean);
    }

    @Override
    public void sendWhiteLight(ColorBean bean) { mLampPresenter.changeWhiteLight(bean); }

    @OnClick(R.id.iv_lamp_close)
    public void onLampClick() {
        mLampPresenter.onClickLampSwitch();
    }


    @OnClick(R.id.ll_lamp_bottom_operation)
    public void onClickArrawUp() {
        AnimationUtil.translateView(mOperationView, Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 0f,
            Animation.RELATIVE_TO_SELF, 1f, 300, false, new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    mOperationView.setVisibility(View.GONE);
                    mLampViewTip.setVisibility(View.VISIBLE);
                    mLampPresenter.hideOperation();

                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        mLampPresenter.onDestroy();
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }




}
