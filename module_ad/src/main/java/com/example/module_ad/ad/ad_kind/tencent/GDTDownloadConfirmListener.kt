package com.example.module_ad.ad.ad_kind.tencent

import android.util.Log
import com.example.module_ad.ad.ad_kind.tencent.DownloadApkConfirmDialog.getApkJsonInfoUrl
import com.qq.e.comm.compliance.DownloadConfirmListener

object GDTDownloadConfirmListener {
    val DOWNLOAD_CONFIRM_LISTENER =
        DownloadConfirmListener { context, scenes, infoUrl, callBack ->

            //获取对应的json数据并自定义显示
            DownloadApkConfirmDialog(context, getApkJsonInfoUrl(infoUrl), callBack).show()

            //如果不想自己解析json数据可以直接使用webView展示应用信息，和上面二选一
//            DownloadApkConfirmDialogWebView(context, infoUrl, callBack).show();//使用webview显示
        }
}