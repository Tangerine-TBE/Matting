package com.abc.matting.ui.activity

import android.content.Intent
import android.os.Build
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.view.animation.ScaleAnimation
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.bean.DataBean
import com.abc.matting.utils.BaseUiListener
import com.abc.matting.utils.HttpUtils
import com.feisukj.base.AgreementContentActivity
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.*
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.tencent.tauth.Tencent
import kotlinx.android.synthetic.main.activity_login.*
import java.util.*

class LoginActivity : BaseActivity() {

    private var api: IWXAPI? = null
    private var mTencent: Tencent? = null
    private var mIUiListener: BaseUiListener? = null
    private var isagreen = false

    private var otherLoginFlag = false
    override fun getLayoutId(): Int = R.layout.activity_login

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        Tencent.setIsPermissionGranted(true,Build.MODEL)
        mTencent = Tencent.createInstance(Constants.QQ_APP_ID, applicationContext)
        regToWx()
        barBack.setOnClickListener {
            finish()
        }
        register.setOnClickListener {
            startActivity(Intent(this,RegisteredOrFindActivity::class.java))
        }
        loginBtn.setOnClickListener {
            KeyBoardUtlis.autoDismissKeyBoards(etUserName,etUserPwd)
            if (isagreen)
                login()
            else{
                val anim = ScaleAnimation(1f,1.5f,1f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                anim.duration = 800
                anim.repeatCount = 1
                anim.interpolator = LinearInterpolator()
                anim.repeatMode = Animation.REVERSE
                agreen.startAnimation(anim)
                ToastUtil.showCenterToast("请先同意下方用户协议和隐私政策")
            }
        }
        loginQQ.setOnClickListener {
            if (isagreen)
                loginByQQ()
            else{
                val anim = ScaleAnimation(1f,1.5f,1f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                anim.duration = 800
                anim.repeatCount = 1
                anim.interpolator = LinearInterpolator()
                anim.repeatMode = Animation.REVERSE
                agreen.startAnimation(anim)
                ToastUtil.showCenterToast("请先同意下方用户协议和隐私政策")
            }
        }
        loginWX.setOnClickListener {
            if (isagreen)
                loginBYWX()
            else{
                val anim = ScaleAnimation(1f,1.5f,1f,1.5f,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
                anim.duration = 800
                anim.repeatCount = 1
                anim.interpolator = LinearInterpolator()
                anim.repeatMode = Animation.REVERSE
                agreen.startAnimation(anim)
                ToastUtil.showCenterToast("请先同意下方用户协议和隐私政策")
            }
        }

        agreen.setOnClickListener {
            isagreen = !isagreen
            if (isagreen)
                agreen.setImageResource(R.drawable.ic_login_agree_y)
            else
                agreen.setImageResource(R.drawable.ic_login_agree_n)
        }

        register.setOnClickListener {
            val intent = Intent(this,RegisteredOrFindActivity::class.java)
            intent.putExtra(RegisteredOrFindActivity.WORK_TYPE,RegisteredOrFindActivity.TYPE_ADD)
            startActivity(intent)
        }

        reset.setOnClickListener {
            val intent = Intent(this,RegisteredOrFindActivity::class.java)
            intent.putExtra(RegisteredOrFindActivity.WORK_TYPE,RegisteredOrFindActivity.TYPE_FIND)
            startActivity(intent)
        }

        agreement.setOnClickListener {
            val intent = Intent(this, AgreementContentActivity::class.java)
            intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_FUWU)
            startActivity(intent)
        }

        privacy.setOnClickListener {
            val intent = Intent(this, AgreementContentActivity::class.java)
            intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_YINSI)
            startActivity(intent)
        }
    }

    private fun login(){
        if (!(RegularUtils.validatePhoneNumber(etUserName.text.toString())&& RegularUtils.isPassword(etUserPwd.text.toString()))){
            ToastUtil.showCenterToast("账号或密码格式错误")
        }else{
            val map: MutableMap<String, String> = TreeMap()
            map["mobile"] = etUserName.text.toString()
            map["password"] = MD5Utlis.md5(etUserPwd.text.toString())
            loadingDialog.setTitleText("请稍后...")
            loadingDialog.show()
            HttpUtils.getData(map, Constants.LOGIN,object : HttpUtils.RequestCallback{
                override fun onSuccess(response: String?) {
                    LogUtils.e("手机登录------------>${response}")
                    try{
                        val msg= try {
                            GsonUtils.parseObject(response, UserBean::class.java).msg
                        } catch (e: Exception) {
                            ""
                        }
                        if (msg == "OK") {
                            //登录成功，保存用户数据
                            SPUtil.getInstance().putBoolean(Constants.IS_LOGIN, true)
                                .putBoolean(Constants.IS_REMEMBERUSER, true)
                                .putBoolean(Constants.IS_REMEMBERPWD, true)
                                .putString(Constants.USER_NAME, etUserName.text.toString())
                                .putString(Constants.USER_PWD, etUserPwd.text.toString())
                                .putString(Constants.USER_BEAN, response)
                            runOnUiThread {
                                loadingDialog.dismiss()
                                finish()
                            }
                        } else {
                            //登录失败，清除用户数据
                            runOnUiThread {
                                ToastUtil.showCenterToast(GsonUtils.parseObject(response, DataBean::class.java).msg)
                                loadingDialog.dismiss()
                            }
                            SPUtil.getInstance().putBoolean(Constants.IS_LOGIN, false)
                                .putBoolean(Constants.IS_REMEMBERUSER, false)
                                .putBoolean(Constants.IS_REMEMBERPWD, false)
                                .putString(Constants.USER_NAME, "")
                                .putString(Constants.USER_PWD, "")
                        }
                    }catch (e : Exception){
                        runOnUiThread {
                            ToastUtil.showCenterToast("登录失败")
                            loadingDialog.dismiss()
                        }
                    }
                }

                override fun onFailure(msg: String?, e: Exception?) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("无法连接到服务器")
                        loadingDialog.dismiss()
                    }
                }

            })
        }
    }

    //微信登录
    private fun loginBYWX() {
        if (!api!!.isWXAppInstalled) {
            ToastUtil.showCenterToast("您的设备未安装微信客户端")
        } else {
            val req = SendAuth.Req()
            req.scope = "snsapi_userinfo"
            req.state = "wechat_sdk_demo_test"
            api!!.sendReq(req)
            loadingDialog.show()
            otherLoginFlag = true
        }
    }

    //QQ登录
    private fun loginByQQ() {
        /**通过这句代码，SDK实现了QQ的登录，这个方法有三个参数，第一个参数是context上下文，第二个参数SCOPO 是一个String类型的字符串，表示一些权限
         * 官方文档中的说明：应用需要获得哪些API的权限，由“，”分隔。例如：SCOPE = “get_user_info,add_t”；所有权限用“all”
         * 第三个参数，是一个事件监听器，IUiListener接口的实例，这里用的是该接口的实现类  */
        mIUiListener = BaseUiListener(applicationContext)
        //all表示获取所有权限
        mTencent!!.login(this, "all", mIUiListener)
        loadingDialog.show()
        otherLoginFlag = true
    }

    override fun onResume() {
        super.onResume()
        if (SPUtil.getInstance().getBoolean(Constants.IS_REMEMBERUSER, false)) {
            etUserName.setText(SPUtil.getInstance().getString(Constants.USER_NAME))
            if (SPUtil.getInstance().getBoolean(Constants.IS_REMEMBERPWD, false)) {
                etUserPwd.setText(SPUtil.getInstance().getString(Constants.USER_PWD))
                login()
            }
        }
        if (otherLoginFlag) {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    LogUtils.e("第三方登录返回时情况------------->" + SPUtil.getInstance()
                        .getBoolean(Constants.IS_LOGIN_THIRD, false))
                    if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN_THIRD, false)) {
//                    loadingDialog.dismiss();
                        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN, true)
                        SPUtil.getInstance().putString(Constants.USER_NAME, "")
                        runOnUiThread {
                            finish()
                            dismissLoading()
                        }
                        LogUtils.e("------------------------------------------------------------第三方登录成功--------------------------------------------------------")
                    }else{
                        runOnUiThread {
                            dismissLoading()
                        }
                    }
                }
            }, 3000)
        }
    }


    //注册应用
    private fun regToWx() {
        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID, false)
        api!!.registerApp(Constants.WX_APP_ID)
    }

    /**
     * 在调用Login的Activity或者Fragment中重写onActivityResult方法
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == com.tencent.connect.common.Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}