package com.suwell.fastpng;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class LaunchActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 10001;
    String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};
    List<String> mPermissionList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        if (Build.VERSION.SDK_INT >= 23) {//6.0才用动态权限
            initPermission();
        } else {
            permissionSuccess();
        }
    }

    //权限判断和申请
    private void initPermission() {
        mPermissionList.clear();
        for (int i = 0; i < permissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mPermissionList.add(permissions[i]);//添加还未授予的权限
            }
        }
        if (mPermissionList.size() > 0) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE);
        } else {
            //权限都已经通过
            permissionSuccess();
        }
    }

    private void permissionSuccess() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            if (AppOpsUtils.isAllowed(this, AppOpsUtils.OP_READ_EXTERNAL_STORAGE) &&
                    AppOpsUtils.isAllowed(this, AppOpsUtils.OP_WRITE_EXTERNAL_STORAGE)) {
                permissionSuccess();
            } else {
                Toast.makeText(this, "权限申请失败,请手动到设置-应用-权限打开", Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            boolean hasPermissionDismiss = false;//有权限没有通过
            if (REQUEST_CODE == requestCode) {
                for (int i = 0; i < grantResults.length; i++) {
                    if (grantResults[i] == -1) {
                        hasPermissionDismiss = true;
                    }
                }
                //如果有权限没有被允许
                if (hasPermissionDismiss) {
                    Toast.makeText(this, "权限申请失败,请手动到设置-应用-权限打开", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    permissionSuccess();
                }
            }
        }
    }
}