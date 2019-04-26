package com.example.pornographic.base;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pornographic.R;
import com.example.pornographic.util.StatusBarUtil;
import com.example.pornographic.weight.MyDialog;

import java.util.ArrayList;

public abstract class BaseActivity extends AppCompatActivity {


    ArrayList<Activity> activities = new ArrayList<Activity>();

    /**
     * 屏幕宽
     */
    public static int SCREEN_WIDTH;

    /**
     * 屏幕高
     */
    public static int SCREEN_HEIGHT;


    public interface OnItemClickListener {
        void onConfirm(MyDialog dialog);

        void onCancel(MyDialog dialog);
    }

    private MyDialog myDialog1;

    public void showMsgDialog(String title, String content, final OnItemClickListener onItemClickListener) {
        myDialog1 = new MyDialog(this, new int[]{R.id.dialog_btn_close, R.id.dialog_btn_cancel});
        if (myDialog1 != null) {
            myDialog1.setContent(content);
            myDialog1.setTitle(title);
            myDialog1.setOnCenterItemClickListener(new MyDialog.OnCenterItemClickListener() {
                @Override
                public void onCenterItemClick(MyDialog dialog, View view) {
                    int i = view.getId();
                    if (i == R.id.dialog_btn_close) {
                        dialog.dismiss();
                        onItemClickListener.onCancel(dialog);
                    } else if (i == R.id.dialog_btn_cancel) {
                        onItemClickListener.onConfirm(dialog);
                        dialog.dismiss();
                    }
                }
            });
            myDialog1.show();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initActivityLayout());
        StatusBarUtil.setStatusBarTextColor(this);
        WindowManager manager = getWindowManager();
        Display display = manager.getDefaultDisplay();
        DisplayMetrics dm = new DisplayMetrics();
        display.getMetrics(dm);
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        addActivity();
        initView();
        initData();
    }

    public BaseActivity initToolbar(String title) {
        TextView viewById = findViewById(R.id.tv_title);
        viewById.setText(title);
        ImageView viewById1 = findViewById(R.id.iv_close);
        viewById1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        return this;
    }

    public abstract int initActivityLayout();

    public abstract void initView();

    public abstract void initData();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeActivity();
    }

    public void addActivity() {
        activities.add(this);
    }

    public void removeActivity() {
        activities.remove(this);
    }

    /**
     * 结束所以活动
     */
    public void finishAllAcitivity() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 跳转
     */
    public void startAction(@SuppressWarnings("rawtypes") Class cls,
                            String name, String value) {
        Intent intent = new Intent(this, cls);
        intent.putExtra(name, value);
        startActivity(intent);
    }
}

