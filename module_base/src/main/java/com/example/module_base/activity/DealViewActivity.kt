package com.example.module_base.activity

import android.graphics.Color
import android.os.Build
import android.webkit.*
import com.example.module_base.R
import com.example.module_base.base.BaseViewActivity
import com.example.module_base.databinding.ActivityDealBinding
import com.example.module_base.utils.Constants
import com.example.module_base.utils.MyStatusBarUtil
import com.example.module_base.utils.PackageUtil
import com.example.module_base.utils.toolbarEvent
import kotlinx.android.synthetic.main.activity_deal.*


class DealViewActivity : BaseViewActivity<ActivityDealBinding>()  {


    var mTitleMsg="用户协议"
    var mContent=R.string.user
    private val email = "2681706890@qq.com"
    private val com = "深圳市天王星互娱科技有限公司"


    override fun setFullScreenWindow() {
        MyStatusBarUtil.setColor(this, Color.TRANSPARENT)
    }
    override fun getLayoutView(): Int = R.layout.activity_deal
    override fun initView() {
        when (intent.getIntExtra(Constants.SET_DEAL1, 0)) {
            1 -> {
                mTitleMsg="用户协议"
                mContent=R.string.user
                text.text = PackageUtil.difPlatformName(this,mContent)
            }
            2-> {
                mTitleMsg="隐私政策"
                visibleView(webView)
                initWebView()
            }
            3->{
                mTitleMsg="功能说明"
                mContent=R.string.shareText
                text.text = PackageUtil.difPlatformName(this,mContent)
            }

        }
        privacy_toolbar.setTitle(mTitleMsg)
    }

    override fun initEvent() {

        privacy_toolbar.toolbarEvent(this) {}




    }

    private fun initWebView() {
        webView.webChromeClient = WebChromeClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
//        webSettings.useWideViewPort = true
//        webSettings.loadWithOverviewMode = true

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        webSettings.domStorageEnabled = true//不加这句有些h5登陆窗口出不来 H5页面使用DOM storage API导致的页面加载问题
        webSettings.cacheMode = WebSettings.LOAD_DEFAULT
        webSettings.allowFileAccess = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                webView.loadUrl("javascript:androidSetAppName('${packageManager.getApplicationLabel(applicationInfo)}')")
//                webView.loadUrl("javascript:androidSetCompanyName('${com}')")
//                webView.loadUrl("javascript:androidSetEmail('${email}')")
//            }
        }
        //加载网络资源
        webView.loadUrl("http://test.aisou.club/privacy_policy/privacy_policy.html?app_name=${packageManager.getApplicationLabel(applicationInfo)}&pack_name=${this.packageName}")
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }

}