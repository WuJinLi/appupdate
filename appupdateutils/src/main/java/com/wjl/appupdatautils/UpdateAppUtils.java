package com.wjl.appupdatautils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.wjl.appupdatautils.customview.Callback;
import com.wjl.appupdatautils.customview.ConfirmDialog;

/**
 * author: WuJinLi
 * time  : 17/8/16
 * desc  : app更新的工具类
 */

public class UpdateAppUtils {
    private final String TAG = "UpdateAppUtils";
    public static final int CHECK_BY_VERSION_NAME = 1001;//通过检查appname方式
    public static final int CHECK_BY_VERSION_CODE = 1002;//通过检查appcode方式
    public static final int DOWNLOAD_BY_APP = 1003;//通过app自身下载
    public static final int DOWNLOAD_BY_BROWSER = 1004;//通过浏览器来下载


    private Activity activity;
    private int checkBy = CHECK_BY_VERSION_CODE;//检查版本的方式
    private String apkPath = "";//下载apk的路径
    private int downloadBy = DOWNLOAD_BY_APP;
    private int serverVersionCode = 0;
    private String serverVersionName = "";
    private boolean isForce = false; //是否强制更新

    private int localVersionCode = 0;//本地apk的versioncode
    private String localVersionName = "";//本地apk的app名称

    private UpdateAppUtils(Activity activity) {
        this.activity = activity;
        getAPPLocalVersion(activity);
    }


    /**
     * 初始化UpdateAppUtils对象
     *
     * @param activity
     * @return
     */
    public static UpdateAppUtils from(Activity activity) {
        return new UpdateAppUtils(activity);
    }


    /**
     * 检查app版本的方式（appname，appcode）
     *
     * @param checkBy
     * @return
     */
    public UpdateAppUtils checkBy(int checkBy) {
        this.checkBy = checkBy;
        return this;
    }

    /**
     * 下载apk的路径
     *
     * @param apkPath
     * @return
     */
    public UpdateAppUtils apkPath(String apkPath) {
        this.apkPath = apkPath;
        return this;
    }


    /**
     * 下载apk方式
     *
     * @param downloadBy
     * @return
     */
    public UpdateAppUtils downloadBy(int downloadBy) {
        this.downloadBy = downloadBy;
        return this;
    }

    /**
     * 服务器apk的versioncode
     *
     * @param serverVersionCode
     * @return
     */
    public UpdateAppUtils serverVersionCode(int serverVersionCode) {
        this.serverVersionCode = serverVersionCode;
        return this;
    }

    /**
     * 服务器apk的名称
     *
     * @param serverVersionName
     * @return
     */
    public UpdateAppUtils serverVersionName(String serverVersionName) {
        this.serverVersionName = serverVersionName;
        return this;
    }

    /**
     * 是否是强制更新
     *
     * @param isForce
     * @return
     */
    public UpdateAppUtils isForce(boolean isForce) {
        this.isForce = isForce;
        return this;
    }


    /**
     * 获取已安装app的版本信息，包括app 的versioncode，versionname
     *
     * @param context
     */
    private void getAPPLocalVersion(Context context) {
        PackageManager packageManager = context.getPackageManager();

        try {
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            localVersionCode = packageInfo.versionCode;
            localVersionName = packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    /**
     * 检查更新
     */
    public void update() {
        switch (checkBy) {
            case CHECK_BY_VERSION_NAME://检查方式versionmane
                if (!serverVersionName.equals(localVersionName)) {
                    toUpdata();
                } else {
                    Log.i(TAG, "当前版本是最新版本" + serverVersionCode + "/" + serverVersionName);
                }
                break;
            case CHECK_BY_VERSION_CODE://检查方式versioncode
                if (serverVersionCode > localVersionCode) {
                    toUpdata();
                } else {
                    Log.i(TAG, "当前版本是最新版本" + serverVersionCode + "/" + serverVersionName);
                }
                break;

            default:
                break;
        }
    }

    /**
     * 执行更新的操作
     */
    private void toUpdata() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission
                .WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            realUpdate();
        } else {
            Toast.makeText(activity, "读写SD卡权限", Toast.LENGTH_SHORT).show();
        }
    }

    private void realUpdate() {
        ConfirmDialog confirmDialog = new ConfirmDialog(activity, new Callback() {
            @Override
            public void callback(int position) {
                switch (position) {
                    case 1://sure
                        if (downloadBy == DOWNLOAD_BY_APP) {
                            //APP自身更新

                        } else if (downloadBy == DOWNLOAD_BY_BROWSER) {
                            //app通过浏览器下载apk
                        }
                        break;
                    case 0://cancle
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
