package com.aries.title;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.aries.title.adapter.TitleAdapter;
import com.aries.title.base.BaseRecycleActivity;
import com.aries.title.entity.DrawerEntity;
import com.aries.title.entity.TitleEntity;
import com.aries.title.util.ViewUtil;
import com.aries.ui.util.StatusBarUtil;
import com.aries.ui.view.title.TitleBarView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created: AriesHoo on 2017-02-09 10:24
 * Function: 演示TitleBarView常见用法
 * Desc:
 */
public class MainActivity extends BaseRecycleActivity<TitleEntity> {

    @BindView(R.id.drawer_root) DrawerLayout drawerRoot;
    @BindView(R.id.sv_slide) ScrollView svSlide;
    @BindView(R.id.titleBarDrawer) TitleBarView titleBarDrawer;
    @BindView(R.id.fLayout_drawer) FrameLayout fLayoutDrawer;
    @BindView(R.id.rv_contentDrawer) RecyclerView mRecyclerViewDrawer;
    private SwitchCompat sBtnImmersible;
    private SwitchCompat sBtnLight;
    private SwitchCompat sBtnLine;
    private SeekBar sBarAlpha;
    private TextView tvStatusAlpha;

    private boolean isImmersible = true;
    private boolean isLight = true;
    private boolean canImmersible = true;
    private boolean canLight = true;

    private BaseQuickAdapter mAdapter;
    private BaseQuickAdapter mAdapterDrawer;
    protected View vHeader;
    private int mAlpha = 102;

    @Override
    protected boolean setLoadMore() {
        return false;
    }

    @Override
    protected void setTitleBar() {
        titleBar.setTitleMainText("主标题");
        titleBar.setTitleSubText("副标题");
        titleBar.setRightTextDrawable(isWhite ? R.drawable.ic_menu : R.drawable.ic_menu_white);
        titleBar.setOnRightTextClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerRoot.openDrawer(svSlide);
            }
        });
        if (StatusBarUtil.isMIUI()) {
            titleBar.setTitleSubText("MIUI" + StatusBarUtil.getMIUIVersionName());
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected BaseQuickAdapter<TitleEntity, BaseViewHolder> getAdapter() {
        mAdapter = new TitleAdapter(mContext);
        return mAdapter;
    }

    @Override
    protected void loadData(int page) {

    }


    @Override
    protected void initView(Bundle bundle) {
        super.initView(bundle);
        titleBarDrawer.setImmersible(mContext, isImmersible, isLight);
        vHeader = View.inflate(mContext, R.layout.layout_title_header, null);
        sBtnImmersible = (SwitchCompat) vHeader.findViewById(R.id.sBtn_immersible);
        sBtnLight = (SwitchCompat) vHeader.findViewById(R.id.sBtn_light);
        sBtnLine = (SwitchCompat) vHeader.findViewById(R.id.sBtn_line);
        sBarAlpha = (SeekBar) vHeader.findViewById(R.id.sBar_alpha);
        tvStatusAlpha = (TextView) vHeader.findViewById(R.id.tv_statusAlpha);
        initView();
        setDrawerList();
        initData();
    }

    private void setDrawerList() {
        List<DrawerEntity> listDrawer = new ArrayList<>();
        listDrawer.add(new DrawerEntity("AriesHoo", "点击跳转GitHub个人主页", "https://github.com/AriesHoo"));
        listDrawer.add(new DrawerEntity("TitleBarView", "点击跳转GitHub项目页", "https://github.com/AriesHoo/TitleBarView"));
        listDrawer.add(new DrawerEntity("UIWidget", "点击跳转GitHub项目页", "https://github.com/AriesHoo/UIWidget"));
        DrawerHelper.getInstance().initRecyclerView(mContext, mRecyclerViewDrawer, listDrawer);
    }

    private void initData() {
        List<TitleEntity> list = new ArrayList<>();
        list.add(new TitleEntity("TitleBarView与底部EditText结合", "点击查看示例", TitleEditActivity.class));
        list.add(new TitleEntity("白色主题", "点击切换白色主题", android.R.color.white));
        list.add(new TitleEntity("红色主题", "点击切换红色主题", android.R.color.holo_red_light));
        list.add(new TitleEntity("橙色主题", "点击切换橙色主题", android.R.color.holo_orange_light));
        list.add(new TitleEntity("绿色主题", "点击切换绿色主题", android.R.color.holo_green_light));
        list.add(new TitleEntity("蓝色主题", "点击切换蓝色主题", android.R.color.holo_blue_light));
        list.add(new TitleEntity("紫色主题", "点击切换紫色主题", android.R.color.holo_purple));
        mAdapter.setHeaderView(vHeader);
        mAdapter.setNewData(list);
        ViewUtil.getInstance().setViewHeight(fLayoutDrawer, (int) (getResources().getDimension(R.dimen.dp_drawer_header)) + titleBar.getStatusBarHeight());
    }

    private void initView() {
        canImmersible = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        canLight = canImmersible;
        if (!canImmersible) {
            sBtnImmersible.setClickable(false);
            sBtnImmersible.setChecked(false);
            sBtnImmersible.setText("4.4以下不支持沉浸状态栏");
            sBtnLight.setClickable(false);
            sBtnLight.setChecked(false);
        }
        if (!canLight) {
            sBtnLight.setClickable(false);
            sBtnLight.setChecked(false);
            sBtnLight.setText("4.4以下不支持全透明");
        }
        sBarAlpha.setMax(255);
        sBtnImmersible.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isImmersible = isChecked;
                titleBar.setImmersible(mContext, isImmersible, isLight);//一般情况下使用
                titleBarDrawer.setImmersible(mContext, isImmersible, isLight);
                sBtnImmersible.setText(isChecked ? "沉浸" : "不沉浸");
                if (!isImmersible) {
                    sBtnLight.setChecked(false);
                    sBarAlpha.setProgress(255);
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.setStatusBarDarkMode(mContext);
                    }
                }
            }
        });
        sBtnLight.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLight = isChecked;
                if (isLight) {
                    sBtnImmersible.setChecked(true);
                    sBarAlpha.setProgress(0);
                } else {
                    sBarAlpha.setProgress(102);
                }
                titleBar.setImmersible(mContext, isImmersible, isLight);//一般情况下使用
                titleBarDrawer.setImmersible(mContext, isImmersible, isLight);
                if (!isImmersible) {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.setStatusBarDarkMode(mContext);
                    }
                }
                sBtnLight.setText(isChecked ? "状态栏全透明" : "状态栏半透明");
            }
        });
        sBtnLine.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                titleBar.setDividerVisible(isChecked);
                sBtnLine.setText(isChecked ? "显示下划线" : "隐藏下划线");
            }
        });
        sBarAlpha.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvStatusAlpha.setText(progress + "");
                mAlpha = progress;
                sBtnImmersible.setChecked(mAlpha < 230);
                sBtnLight.setChecked(mAlpha == 0);
                titleBar.setStatusAlpha(mAlpha);
                if (mAlpha > 230 && isWhite) {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                } else {
                    if (isWhite) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        drawerRoot.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (type > 0) {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (type > 0) {
                    if (isWhite && isImmersible) {
                        StatusBarUtil.setStatusBarLightMode(mContext);
                    } else {
                        StatusBarUtil.setStatusBarDarkMode(mContext);
                    }
                }
            }
        });
        if (canLight && Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            sBtnLight.setChecked(false);
            sBarAlpha.setProgress(titleBar.DEFAULT_STATUS_BAR_ALPHA);
        } else {
            sBarAlpha.setProgress(0);
        }
    }

    @Override
    protected void onItemClicked(BaseQuickAdapter<TitleEntity, BaseViewHolder> adapter, View view, int position) {
        super.onItemClicked(adapter, view, position);
        TitleEntity entity = adapter.getItem(position);
        if (entity.colorRes != 0) {
            isWhite = entity.colorRes == android.R.color.white;
            titleBar.setBackgroundResource(entity.colorRes);
            titleBar.setLeftTextDrawable(isWhite ? R.drawable.ic_arrow_left : R.drawable.ic_arrow_back_white);
            titleBar.setRightTextDrawable(isWhite ? R.drawable.ic_menu : R.drawable.ic_menu_white);
            titleBar.setTitleMainTextColor(isWhite ? getResources().getColor(R.color.colorTextBlack) : Color.WHITE);
            titleBar.setTitleSubTextColor(isWhite ? getResources().getColor(R.color.colorTextBlack) : Color.WHITE);
            if (type > 0 && isImmersible) {
                if (isWhite) {
                    StatusBarUtil.setStatusBarLightMode(mContext);
                } else {
                    StatusBarUtil.setStatusBarDarkMode(mContext);
                }
            }
        } else if (entity.activity != null) {
            startActivity(entity.activity);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerRoot.isDrawerOpen(svSlide)) {
            drawerRoot.closeDrawer(svSlide);
        } else {
            super.onBackPressed();
        }
    }
}
