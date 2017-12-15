package com.lubook.os;

import android.Manifest;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ToastUtils;
import com.example.refreshview.CustomRefreshView;
import com.joker.annotation.PermissionsDenied;
import com.joker.annotation.PermissionsGranted;
import com.joker.annotation.PermissionsRationale;
import com.joker.annotation.PermissionsRequestSync;
import com.joker.api.Permissions4M;

import org.opencv.core.CvType;

import static com.lubook.os.PermissionMActivity.CALENDAR_CODE;
import static com.lubook.os.PermissionMActivity.LOCATION_CODE;
import static com.lubook.os.PermissionMActivity.SENSORS_CODE;

@PermissionsRequestSync(permission = {Manifest.permission.BODY_SENSORS, Manifest.permission
        .ACCESS_FINE_LOCATION, Manifest.permission.READ_CALENDAR},
        value = {SENSORS_CODE, LOCATION_CODE, CALENDAR_CODE})
public class PermissionMActivity extends AppCompatActivity {
    public static final int CALENDAR_CODE = 7;
    public static final int SENSORS_CODE = 8;
    public static final int LOCATION_CODE = 9;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_m);
        // 一键申请
        Button accept_pm = (Button) findViewById(R.id.accept_pm);
        accept_pm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Permissions4M
                        .get(PermissionMActivity.this)
                        .requestSync();
            }
        });


    }

    @PermissionsGranted({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncGranted(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtils.showShort("地理位置权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  地理位置权限授权成功 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtils.showShort("传感器权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  传感器权限授权成功 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtils.showShort("读取日历权限授权成功 in activity with annotation");
                Log.d("TAG", "syncGranted:  读取日历权限授权成功 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsDenied({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncDenied(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtils.showShort("地理位置权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  地理位置权限授权失败 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtils.showShort("传感器权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  传感器权限授权失败 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtils.showShort("读取日历权限授权失败 in activity with annotation");
                Log.d("TAG", "syncDenied:  读取日历权限授权失败 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @PermissionsRationale({LOCATION_CODE, SENSORS_CODE, CALENDAR_CODE})
    public void syncRationale(int code) {
        switch (code) {
            case LOCATION_CODE:
                ToastUtils.showShort("请开启地理位置权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启地理位置权限 in activity with annotation");
                break;
            case SENSORS_CODE:
                ToastUtils.showShort("请开启传感器权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启传感器权限 in activity with annotation");
                break;
            case CALENDAR_CODE:
                ToastUtils.showShort("请开启读取日历权限 in activity with annotation");
                Log.d("TAG", "syncRationale:  请开启读取日历权限 in activity with annotation");
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        Permissions4M.onRequestPermissionsResult(this, requestCode, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
