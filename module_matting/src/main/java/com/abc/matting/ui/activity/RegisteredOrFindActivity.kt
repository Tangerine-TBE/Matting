package com.abc.matting.ui.activity

import android.os.CountDownTimer
import android.text.TextUtils
import android.util.Log
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.bean.DataBean
import com.abc.matting.utils.HttpUtils
import com.feisukj.base.BaseApplication
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.*
import kotlinx.android.synthetic.main.activity_registered_or_find.*
import java.util.*

class RegisteredOrFindActivity : BaseActivity() {
    companion object{
        const val WORK_TYPE = "work_type"
        const val TYPE_ADD = "type_add"
        const val TYPE_FIND = "type_find"
    }

    private var type = ""

    override fun getLayoutId(): Int = R.layout.activity_registered_or_find

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        type = intent.getStringExtra(WORK_TYPE)?: TYPE_ADD

        when(type){
            TYPE_ADD -> {
                etUserName.hint = "请输入手机号"
                etUserCode.hint = "请输入验证码"
                etUserPwd.hint = "请输入密码"
                title_text.text = "立即注册"
                registerBtn.text = "注册"
                barTitle.text = "用户注册"
            }
            TYPE_FIND -> {
                etUserName.hint = "请输入手机号"
                etUserCode.hint = "请输入验证码"
                etUserPwd.hint = "请输入新密码"
                title_text.text = "修改密码"
                registerBtn.text = "修改"
                barTitle.text = "找回密码"
            }
        }
        codeBtn.setOnClickListener {
            if (RegularUtils.validatePhoneNumber(etUserName.text.toString())) {
                TimeCount(Constants.SECOND * 1000L, 990).start()
                getRegisterCode()
            } else {
                ToastUtil.showCenterToast("手机号格式有误")
            }
        }
        registerBtn.setOnClickListener {
            checkCode()
        }
        barBack.setOnClickListener {
            finish()
        }
    }

    /**
     * 获取验证码
     */
    private fun getRegisterCode() {
        if (!RegularUtils.validatePhoneNumber(etUserName.text.toString())) {
            ToastUtil.showCenterToast("手机号或密码格式错误")
        } else {
            loadingDialog.setTitleText("正在获取验证码")
            loadingDialog.show()
            val mMap: MutableMap<String, String> = TreeMap()
            mMap["mobile"] = etUserName.text.toString()
            mMap["package"] = BaseApplication.application.packageName
            //获取验证码逻辑
            HttpUtils.getData(mMap, Constants.GET_CODE, object : HttpUtils.RequestCallback {
                override fun onSuccess(response: String) {
                    LogUtils.e("获取验证码---------->$response")
                    var msg = ""
                    msg = try {
                        if (GsonUtils.parseObject(response, DataBean::class.java).msg == "OK") {
                            "获取验证码成功"
                        } else {
                            GsonUtils.parseObject(response, DataBean::class.java).msg
                        }
                    } catch (e: Exception) {
                        "获取验证码失败"
                    }
                    if (!TextUtils.isEmpty(msg)){
                        runOnUiThread {
                            ToastUtil.showCenterToast(msg)
                            loadingDialog.dismiss()
                        }
                    }
                }

                override fun onFailure(msg: String, e: Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("无法连接到服务器")
                        loadingDialog.dismiss()
                    }
                }
            })
        }
    }

    /**
     * 校验验证码
     */
    private fun checkCode() {
        val code: String = etUserCode.text.toString()
        if (TextUtils.isEmpty(code) || code.length < 5) {
            ToastUtil.showCenterToast("验证码错误")
        } else if (!(RegularUtils.validatePhoneNumber(etUserName.text.toString()) && RegularUtils.isPassword(etUserPwd.text.toString()))) {
            ToastUtil.showCenterToast("手机号或密码格式错误")
        } else {
            val map1: MutableMap<String, String> = TreeMap()
            map1["mobile"] = etUserName.text.toString()
            map1["key"] = code
            loadingDialog.setTitleText("正在验证...")
            loadingDialog.show()
            HttpUtils.getData(map1, Constants.CHECK_CODE, object : HttpUtils.RequestCallback {
                override fun onSuccess(response: String) {
                    LogUtils.e("校验验证码---------->", response)
                    try {
                        if (GsonUtils.parseObject(response, DataBean::class.java).msg == "OK") {
                            runOnUiThread {
                                loadingDialog.dismiss()
                                if (type == TYPE_ADD)
                                    registerByPhone()
                                else
                                    findPwd()
                            }
                        } else {
                            runOnUiThread {
                                ToastUtil.showCenterToast(GsonUtils.parseObject(response, DataBean::class.java).msg)
                                loadingDialog.dismiss()
                            }
                        }
                    } catch (e: Exception) {
                        runOnUiThread {
                            ToastUtil.showCenterToast("校验验证码失败")
                            loadingDialog.dismiss()
                        }
                    }
                }

                override fun onFailure(msg: String, e: Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("无法连接到服务器")
                        loadingDialog.dismiss()
                    }
                }
            })
        }
    }

    /**
     * 用户注册
     */
    private fun registerByPhone() {
        if (!(RegularUtils.validatePhoneNumber(etUserName.text.toString()) && RegularUtils.isPassword(etUserPwd.text.toString()))) {
            ToastUtil.showCenterToast("手机号或密码格式错误")
        } else {
            val map: MutableMap<String, String> = TreeMap()
            map["mobile"] = etUserName.text.toString()
            map["code"] = etUserCode.text.toString()
            map["package"] = BaseApplication.application.packageName
            map["password"] = etUserPwd.text.toString()
            map["platform"] = PackageUtils.getAppMetaData(BaseApplication.application, "CHANNEL")
            loadingDialog.setTitleText("注册账号中...")
            loadingDialog.show()
            HttpUtils.getData(map, Constants.ADD_USER, object : HttpUtils.RequestCallback {
                override fun onSuccess(response: String) {
                    LogUtils.e("注册账号----------->", response)
                    try {
                        if (GsonUtils.parseObject(response, DataBean::class.java).msg == "OK") {
                            runOnUiThread {
                                ToastUtil.showCenterToast("注册成功")
                                loadingDialog.dismiss()
                                finish()
                            }
                            SPUtil.getInstance().putBoolean(Constants.IS_REMEMBERPWD, true)
                                    .putString(Constants.USER_NAME, etUserName.text.toString())
                                    .putString(Constants.USER_PWD, etUserPwd.text.toString())
                                    .putBoolean(Constants.IS_REMEMBERUSER, true)
                        } else {
                            runOnUiThread {
                                ToastUtil.showCenterToast(GsonUtils.parseObject(response, DataBean::class.java).msg)
                                loadingDialog.dismiss()
                            }
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            ToastUtil.showCenterToast("注册失败")
                            loadingDialog.dismiss()
                        }
                    }
                }

                override fun onFailure(msg: String, e: java.lang.Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("无法连接到服务器")
                        loadingDialog.dismiss()
                    }
                }
            })
        }
    }

    /**
     * 找回密码
     */
    private fun findPwd() {
        if (!(RegularUtils.validatePhoneNumber(etUserName.text.toString()) || RegularUtils.isPassword(etUserPwd.text.toString()))) {
            ToastUtil.showCenterToast("手机号或密码格式错误")
        } else {
            val map: MutableMap<String, String> = TreeMap()
            map["mobile"] = etUserName.text.toString()
            map["code"] = etUserCode.text.toString()
            map["password"] = MD5Utlis.md5(etUserPwd.text.toString())
            loadingDialog.setTitleText("修改密码中...")
            loadingDialog.show()
            HttpUtils.getData(map, Constants.FIND_PWD, object : HttpUtils.RequestCallback {
                override fun onSuccess(response: String) {
                    try {
                        val str = GsonUtils.parseObject(response, DataBean::class.java).msg
                        Log.e("重置密码", "$response          str=$str")
                        if (str == "OK") {
                            runOnUiThread {
                                ToastUtil.showCenterToast("修改成功")
                                loadingDialog.dismiss()
                                finish()
                            }
                            SPUtil.getInstance().putString(Constants.USER_NAME, etUserName.text.toString())
                            SPUtil.getInstance().putString(Constants.USER_PWD, etUserPwd.text.toString())
                            SPUtil.getInstance().putBoolean(Constants.IS_REMEMBERUSER, true)
                        } else {
                            ToastUtil.showCenterToast(GsonUtils.parseObject(response, DataBean::class.java).msg)
                        }
                    } catch (e: java.lang.Exception) {
                        runOnUiThread {
                            ToastUtil.showCenterToast("修改失败")
                        }
                    }
                    runOnUiThread {
                        dismissLoading()
                    }
                }

                override fun onFailure(msg: String, e: java.lang.Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("无法连接到服务器")
                        loadingDialog.dismiss()
                    }
                }
            })
        }
    }

    inner class TimeCount(millisInFuture: Long, countDownInterval: Long) : CountDownTimer(millisInFuture, countDownInterval) {
        override fun onFinish() {
            if (codeBtn != null){
                codeBtn.text = "重新获取"
                codeBtn.isClickable = true
            }
        }

        override fun onTick(p0: Long) {
            if(codeBtn != null){
                codeBtn.isClickable = false
                codeBtn.text = "${p0/1000}s"
            }
        }

    }

}