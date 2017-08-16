package com.wjl.appupdatautils;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;

/**
 * author: WuJinLi
 * time  : 17/8/16
 * desc  : 下载文件的工具类
 */

public class DownloadAppUtils {
    private static final String TAG = DownloadAppUtils.class.getSimpleName();
    public static long downloadUpdateApkId = -1;//下载更新Apk 下载任务对应的Id
    public static String downloadUpdateApkFilePath;//下载更新Apk 文件路径

    /**
     * 通过浏览器的方式下载
     *
     * @param context
     * @param url
     */
    public static void downloadForWebView(Context context, String url) {
        Uri uri = Uri.parse(url);

        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    /**
     * 下载完成并完成更新
     * uses-permission android:name="android.permission.DOWNLOAD_WITHOUT_NOTIFICATION"
     *
     * @param context
     * @param url
     * @param fileName
     * @param title
     */
    public static void downloadForAutoInstall(Context context, String url, String fileName,
                                              String title) {
        if (TextUtils.isEmpty(url)) {
            return;
        }
        try {
            Uri uri = Uri.parse(url);
            DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context
                    .DOWNLOAD_SERVICE);

            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setVisibleInDownloadsUi(true);
            request.setTitle(title);

            String filePath = null;

            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                Log.i(TAG, "没有SD卡");
                return;
            }


            downloadUpdateApkFilePath = filePath + File.separator + fileName;

            //若文件存在删除
            deleteFile(downloadUpdateApkFilePath);

            Uri fileUri = Uri.parse("file://" + downloadUpdateApkFilePath);

            request.setDestinationUri(fileUri);

            downloadUpdateApkId = downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
            downloadForWebView(context, url);
        } finally {
//            registerReceiver(receiver, new IntentFilter(DownloadManager
// .ACTION_DOWNLOAD_COMPLETE));
        }

    }

    /**
     * 删除文件
     *
     * @param fileStr
     * @return
     */
    private static boolean deleteFile(String fileStr) {
        File file = new File(fileStr);
        return file.delete();
    }
}
