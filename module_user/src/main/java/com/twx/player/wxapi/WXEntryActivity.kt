package com.twx.player.wxapi

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import com.example.module_base.base.BaseActivity
import com.example.module_user.repository.UserRepository
import com.example.module_user.utils.Constants
import com.example.module_user.utils.UserInfoHelper
import com.example.module_user.viewmodel.LoginViewModel
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import okhttp3.*
import org.greenrobot.eventbus.EventBus
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class WXEntryActivity: Activity(), IWXAPIEventHandler {

    var iwxapi: IWXAPI? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //接收到分享以及登录的intent传递handleIntent方法，处理结果
        iwxapi = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false)
        iwxapi?.handleIntent(intent, this)
    }

    override fun onReq(p0: BaseReq?) {

    }

    override fun onResp(baseResp: BaseResp?) {
        //登录回调
        when (baseResp?.errCode) {
            BaseResp.ErrCode.ERR_OK -> {
                val code = (baseResp as SendAuth.Resp).code
                //获取accesstoken
                getAccessToken(code)
            }
            BaseResp.ErrCode.ERR_AUTH_DENIED -> finish()
            BaseResp.ErrCode.ERR_USER_CANCEL -> finish()
            else -> finish()
        }
    }

    private fun getAccessToken(code: String) {
        val body: RequestBody = FormBody.Builder()
            .add("appid", Constants.WX_APP_ID)
            .add("secret", Constants.WX_SECRET)
            .add("code", code)
            .add("grant_type", "authorization_code")
            .build()
        val request = Request.Builder()
            .url("https://api.weixin.qq.com/sns/oauth2/access_token")
            .post(body)
            .build()
        val client = OkHttpClient()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {}

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                val responseInfo = response.body()!!.string()
//                LogUtils.INSTANCE.e("获取accesstoken成功--------------->$responseInfo")
                var access: String? = null
                var openId: String? = null
                try {
                    val jsonObject = JSONObject(responseInfo)
                    access = jsonObject.getString("access_token")
                    openId = jsonObject.getString("openid")
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                EventBus.getDefault().post(openId)
//                LoginViewModel().doCheckRegister(openId!!,Constants.WECHAT_TYPE)
                finish()
            }
        })
    }
}