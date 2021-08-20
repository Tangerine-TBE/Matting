package com.abc.matting.ui.activity

import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.bean.DataBean
import com.abc.matting.ui.dialog.TipDialog
import com.abc.matting.utils.HttpUtils
import com.feisukj.base.AgreementContentActivity
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.GsonUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.ToastUtil
import kotlinx.android.synthetic.main.activity_about_us.*
import java.util.*

class AboutUsActivity : BaseActivity() {

    override fun getLayoutId(): Int = R.layout.activity_about_us

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        version.text = "V${BaseConstant.versionName}"
        email.text = "客服邮箱：${AgreementContentActivity.email}"

        delete.setOnClickListener {
            if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN,false)){
                deleteUser()
            }else{
                ToastUtil.showCenterToast("未登录")
            }
        }
        loginOut.setOnClickListener {
            if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN,false)){
                exitLogin()
            }else{
                ToastUtil.showCenterToast("未登录")
            }
        }
    }

    /**
     * 删除所有用户信息
     */
    private fun deleteDate() {
        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN, false)
        SPUtil.getInstance().putBoolean(Constants.IS_CODE, false)
        SPUtil.getInstance().putBoolean(Constants.IS_REMEMBERPWD, false)
        SPUtil.getInstance().putBoolean(Constants.IS_REMEMBERUSER, false)
        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN_THIRD, false)
        SPUtil.getInstance().putBoolean(Constants.IS_VIP, false)
        //                SPUtil.getInstance().putBoolean("launchTip",true) //是否展示用户服务协议和隐私政策弹窗
        SPUtil.getInstance().putString(Constants.OPENID, "")
        SPUtil.getInstance().putString(Constants.LOGIN_TYPE, "")
        SPUtil.getInstance().putString(Constants.USER_BEAN, "")
        SPUtil.getInstance().putString(Constants.USER_NAME, "")
        SPUtil.getInstance().putString(Constants.USER_PWD, "")
        SPUtil.getInstance().putString(Constants.OTHER_NAME, "")
    }

    /**
     * 退出登录
     * */
    private fun exitLogin(){
        val dialog = TipDialog(this)
        dialog.setTitle("确定要退出登录吗？")
        dialog.setConfirm {
            deleteDate()
        }
        dialog.show()
    }

    /**
     * 注销用户
     * */
    private fun deleteUser() {
        val dialog = TipDialog(this)
        dialog.setTitle("注销后所有数据将清空,是否确认注销?")
        dialog.setConfirm {
            var id = ""
            try {
                id = "" + GsonUtils.parseObject(SPUtil.getInstance().getString(Constants.USER_BEAN), UserBean::class.java).data.id
            } catch (e: Exception) {
            }
            val map: MutableMap<String, String> = TreeMap()
            map["id"] = id
            loadingDialog.setTitleText("注销中...")
            loadingDialog.show()
            HttpUtils.getData(map, Constants.DELETE_USER, object : HttpUtils.RequestCallback {
                override fun onSuccess(response: String) {
                    LogUtils.e("注销------------->$response")
                    try {
                        if (GsonUtils.parseObject(response, DataBean::class.java).msg.equals("OK")) {
                            deleteDate()
                            runOnUiThread {
                                ToastUtil.showCenterToast("注销成功")
                                loadingDialog.dismiss()
                            }
                        }
                    }catch (e : java.lang.Exception){
                        runOnUiThread {
                            ToastUtil.showCenterToast("注销失败")
                            loadingDialog.dismiss()
                        }
                    }
                }

                override fun onFailure(msg: String?, e: Exception?) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("注销失败")
                        loadingDialog.dismiss()
                    }
                }
            })
        }
        dialog.show()
    }
}