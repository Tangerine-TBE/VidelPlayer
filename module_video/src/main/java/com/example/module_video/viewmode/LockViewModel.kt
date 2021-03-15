package com.example.module_video.viewmode

import androidx.lifecycle.MutableLiveData
import com.example.module_base.base.BaseViewModel
import com.example.module_base.utils.LogUtils
import com.example.module_video.domain.ItemBean
import com.example.module_video.domain.ValuePwdState
import com.example.module_video.utils.BackState
import com.example.module_video.utils.CheckState
import com.example.module_video.utils.Constants
import com.example.module_video.utils.InputPwdState
import com.google.android.exoplayer2.C

/**
 * @name VidelPlayer
 * @class name：com.example.module_video.viewmode
 * @class describe
 * @author wujinming QQ:1245074510
 * @time 2021/3/15 10:26:13
 * @class describe
 */
class LockViewModel : BaseViewModel() {

    val hintList = ArrayList<ItemBean>().apply {
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
    }
    private val beginList = ArrayList<ItemBean>().apply {
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
    }
    private val againList = ArrayList<ItemBean>().apply {
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
        add(ItemBean(title = "0", hasPwd = false))
    }


    private var currentPosition = 0
    private var again = false

    val backState by lazy {
        MutableLiveData(BackState.CANCEL)
    }

    val checkState by lazy {
        MutableLiveData<CheckState>()
    }

    val pwdStateValue by lazy {
        MutableLiveData(ValuePwdState(InputPwdState.BEGIN, hintList))
    }

    val checkPwdState by lazy {
        MutableLiveData<Boolean>()
    }

    fun getCheckState()=checkState.value

    fun getBackState()=backState.value

    private var errorCount=0

    fun setInputState(realPosition: Int) {
        if (realPosition == 12) {
            if (!sp.getBoolean(Constants.SP_SET_PWD_STATE)) {
                //设置密码
                if (currentPosition >= 0) {
                    if (again) {
                        againList.let {
                            if (it[0].hasPwd) currentPosition--
                            it[currentPosition].title = "$realPosition"
                            it[currentPosition].hasPwd = false
                            pwdStateValue.value = ValuePwdState(InputPwdState.AGAIN, it)
                            backState.value = if (it[0].hasPwd) BackState.DELETE else BackState.CANCEL
                        }
                    } else {
                        beginList.let {
                            if (it[0].hasPwd) currentPosition--
                            it[currentPosition].title = "$realPosition"
                            it[currentPosition].hasPwd = false
                            pwdStateValue.value = ValuePwdState(InputPwdState.BEGIN, it)
                            backState.value = if (it[0].hasPwd) BackState.DELETE else BackState.CANCEL
                        }
                    }
                }
            } else {
                //检查密码
                if (currentPosition >= 0) {
                    beginList.let {
                        if (it[0].hasPwd) currentPosition--
                        it[currentPosition].title = "$realPosition"
                        it[currentPosition].hasPwd = false
                        backState.value = if (it[0].hasPwd) BackState.DELETE else BackState.CANCEL
                        if (errorCount == 2) {
                            checkState.value=CheckState.EXIT
                            pwdStateValue.value = ValuePwdState(InputPwdState.NONE, hintList)
                        } else {
                            pwdStateValue.value = ValuePwdState(InputPwdState.BEGIN, it)
                        }

                    }
                }

            }
        } else {
            if (currentPosition < 4) {
                if (!sp.getBoolean(Constants.SP_SET_PWD_STATE)) {
                    //设置密码
                    if (again) {
                        againList.let { it ->
                            it[currentPosition].title = "$realPosition"
                            it[currentPosition].hasPwd = true
                            if (!it[3].hasPwd) {
                                backState.value = BackState.DELETE
                                pwdStateValue.value = ValuePwdState(InputPwdState.AGAIN, it)
                                currentPosition++
                            } else {
                                val beginPwd = StringBuffer()
                                val againPwd = StringBuffer()
                                beginList.forEach {
                                    beginPwd.append(it.title)
                                }
                                againList.forEach {
                                    againPwd.append(it.title)
                                }
                                checkPwdState.value = beginPwd.toString() == againPwd.toString()
                                sp.apply {
                                    putBoolean(Constants.SP_SET_PWD_STATE, beginPwd.toString() == againPwd.toString())
                                    putString(Constants.SP_SET_PWD, againPwd.toString())
                                }

                                if (beginPwd.toString() != againPwd.toString()) {
                                    again = false
                                    currentPosition = 0
                                    beginList.forEach {
                                        it.title = "0"
                                        it.hasPwd = false
                                    }
                                    againList.forEach {
                                        it.title = "0"
                                        it.hasPwd = false
                                    }
                                    backState.value = BackState.CANCEL
                                    pwdStateValue.value = ValuePwdState(InputPwdState.BEGIN, hintList)
                                } else {

                                }
                            }
                        }
                    } else {
                        beginList.let {
                            it[currentPosition].title = "$realPosition"
                            it[currentPosition].hasPwd = true
                            if (!it[3].hasPwd) {
                                backState.value = BackState.DELETE
                                pwdStateValue.value = ValuePwdState(InputPwdState.BEGIN, it)
                                currentPosition++
                            } else {
                                again = true
                                currentPosition = 0
                                backState.value = BackState.CANCEL
                                pwdStateValue.value = ValuePwdState(InputPwdState.AGAIN, hintList)
                            }
                        }
                    }
                } else {
                    //检查密码
                    beginList.let { it ->
                        it[currentPosition].title = "$realPosition"
                        it[currentPosition].hasPwd = true
                        if (!it[3].hasPwd) {
                            backState.value = BackState.DELETE
                            if (errorCount ==0) {
                                pwdStateValue.value = ValuePwdState(InputPwdState.BEGIN, it)
                            } else {
                                pwdStateValue.value = ValuePwdState(InputPwdState.ERROR, it)
                            }
                            currentPosition++
                        } else {
                            //满4位
                            val checkPwd = StringBuffer()
                            beginList.forEach {
                                checkPwd.append(it.title)
                            }
                            backState.value = BackState.CANCEL
                            sp.apply {
                                val savePwd = getString(Constants.SP_SET_PWD)
                                if (checkPwd.toString() == savePwd) {
                                    LogUtils.i("---检查密码-------------$savePwd-------${checkPwd}--------")
                                    putBoolean(Constants.SP_SET_PWD_STATE,false)
                                    checkState.value= CheckState.UNLOCK
                                } else {
                                    if (errorCount>1) {
                                        checkState.value = CheckState.EXIT
                                    } else {
                                        beginList.forEach {
                                            it.title = "0"
                                            it.hasPwd = false
                                        }
                                        currentPosition = 0
                                        checkState.value = CheckState.ERROR
                                        errorCount++
                                    }
                                    LogUtils.i("---检查密码----EXIT---------$savePwd-------${checkPwd}--------")
                                }

                            }
                        }
                    }

                }
            }
        }

    }


}