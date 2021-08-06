package com.abc.matting.ui.fragment

import android.content.Intent
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.ui.activity.AboutUsActivity
import com.abc.matting.ui.activity.FeedBackActivity
import com.abc.matting.ui.activity.LoginActivity
import com.abc.matting.ui.activity.PayActivity
import com.abc.matting.ui.dialog.TipDialog
import com.abc.matting.utils.GetDataUtils
import com.abc.matting.utils.PermissionUtils
import com.feisukj.base.AgreementContentActivity
import com.feisukj.base.baseclass.BaseFragment
import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.GsonUtils
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.ToastUtil
import kotlinx.android.synthetic.main.fragment_setting.*

class SettingFragment : BaseFragment() {
    override fun getLayoutId(): Int = R.layout.fragment_setting

    override fun initView() {
        super.initView()
        userInfo.setOnClickListener {
            if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN, false)){
                exitLogin()
            }else{
                startActivity(Intent(mActivity, LoginActivity::class.java))
            }
        }

        iv_vip.setOnClickListener {
            if (!GetDataUtils.isVip())
                startActivity(Intent(mActivity, PayActivity::class.java))
        }

        aboutUs.setOnClickListener {
            startActivity(Intent(mActivity, AboutUsActivity::class.java))
        }

        feedback.setOnClickListener {
            startActivity(Intent(mActivity, FeedBackActivity::class.java))
        }

        agreement.setOnClickListener {
            val intent = Intent(mActivity, AgreementContentActivity::class.java)
            intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_FUWU)
            startActivity(intent)
        }

        privacy.setOnClickListener {
            val intent = Intent(mActivity, AgreementContentActivity::class.java)
            intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_YINSI)
            startActivity(intent)
        }

        set.setOnClickListener {
            PermissionUtils.gotoPermission(mActivity)
        }
    }

    override fun onResume() {
        super.onResume()
        if (!SPUtil.getInstance().getBoolean(Constants.IS_LOGIN, false)){
            //未登录
            login_tip.text = "登录/注册"
            tv_vip_title.text = "开通VIP享尊贵特权"
            tv_vip_body.text = "超多特权任君享受，早买早享受"
        }else{
            //登录
            val userBeanStr = SPUtil.getInstance().getString(Constants.USER_BEAN, "")
            if (userBeanStr.isNullOrEmpty()){
                ToastUtil.showCenterToast("数据出错，请重新登录")
                return
            }
            val bean = GsonUtils.parseObject(userBeanStr, UserBean::class.java)
            if (bean == null){
                ToastUtil.showCenterToast("数据出错，请重新登录")
                return
            }
            login_tip.text = "用户${bean.data.id}"
            if (bean.data.vip>0){
                //VIP用户
                tv_vip_title.text = "您已经成为VIP会员了"
                tv_vip_body.text = if (bean.data.vip != 13) "有效期：${bean.data.vipexpiretime}" else "有效期：永久"
            }else{
                //普通用户
                tv_vip_title.text = "开通VIP享尊贵特权"
                tv_vip_body.text = "超多特权任君享受，早买早享受"
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
        val dialog = TipDialog(mActivity)
        dialog.setTitle("确定要退出登录吗？")
        dialog.setConfirm {
            deleteDate()
            login_tip.text = "登录/注册"
        }
        dialog.show()
    }
}