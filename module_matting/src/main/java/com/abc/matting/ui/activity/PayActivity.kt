package com.abc.matting.ui.activity

import android.animation.ValueAnimator
import android.content.Intent
import android.view.animation.LinearInterpolator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.adapter.PayAdapter
import com.abc.matting.adapter.PrivilegeAdapter
import com.abc.matting.bean.DataBean
import com.abc.matting.bean.PayBean
import com.abc.matting.ui.dialog.PayExitDialog
import com.abc.matting.utils.GetDataUtils
import com.abc.matting.utils.HttpUtils
import com.abc.matting.utils.PayUtils
import com.abc.matting.utils.Utils
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.*
import com.feisukj.base.util.LogUtils.e
import com.feisukj.base.widget.loaddialog.LoadingDialog.clickListener
import com.tencent.mm.opensdk.utils.Log
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.activity_pay.*
import java.util.*

class PayActivity : BaseActivity(), PayAdapter.PriceCallback {

    companion object{
        const val ALI_PAY = 0
        const val WX_PAY = 1
    }
    private var payAdapter = PayAdapter()
    private var privilegeAdapter = PrivilegeAdapter()
    private var mList: Array<PayBean> = arrayOf()
    private var showDialog = 0
    private var isPaying = false
    private var paySelectType = 0
    set(value) {
        field = value
        if (field == ALI_PAY){
            iv_ali_check.setImageResource(R.drawable.ic_select_pay_y)
            iv_wechat_check.setImageResource(R.drawable.ic_select_pay_n)
        }else{
            iv_ali_check.setImageResource(R.drawable.ic_select_pay_n)
            iv_wechat_check.setImageResource(R.drawable.ic_select_pay_y)
        }
    }
    private var selectItem = 0
    private val exitDialog: PayExitDialog by lazy {
        PayExitDialog(this,toDo = {
            paySelectType = it
            if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN, false)) {
                if (!GetDataUtils.isVip()) buyVip() else ToastUtil.showCenterToast("您已是VIP")
            } else {
                this.startActivity(Intent(this, LoginActivity::class.java))
            }
        },{
            finish()
        })
    }

    override fun getLayoutId(): Int = R.layout.activity_pay

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.white).statusBarDarkFont(true).init()

        mList = Resources.getVipPrice()
        payAdapter.setData(mList)
        payAdapter.setCallback(this)
        payRecycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        payRecycler.adapter = payAdapter

        privilegeAdapter.setData(Resources.getVipPrivilege())
        privilegeRecycler.layoutManager = GridLayoutManager(this, 3)
        privilegeRecycler.adapter = privilegeAdapter

        tv_price.text = "${Utils.moneyFormat(mList[selectItem].money)}元"

        loadingDialog.setClickListener(object : clickListener {
            override fun click() {
                showDialog = 0
                loadingDialog.dismiss()
            }
        })

        initEvent()
    }

    private fun initEvent(){
        barBack.setOnClickListener {
            if (remainTime() == 0L)
                finish()
            else
                exitDialog.show()
        }
        ali.setOnClickListener {
            paySelectType = ALI_PAY
            tv_payType.text = "支付宝支付"
        }
        wechat.setOnClickListener {
            paySelectType = WX_PAY
            tv_payType.text = "微信支付"
        }

        payBtn.setOnClickListener {
            if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN, false)) {
                if (!GetDataUtils.isVip()) buyVip() else ToastUtil.showCenterToast("您已是VIP")
            } else {
                this.startActivity(Intent(this, LoginActivity::class.java))
            }
        }
        downCount()
    }

    /**
     * 计算倒计时剩余时间
     * */
    private fun remainTime():Long{
        val startTime = SPUtil.getInstance().getLong(Constants.DOWN_COUNT_START_TIME)
        val remain = startTime + Constants.DOWN_COUNT - System.currentTimeMillis()
        return if (remain<0) 0 else remain
    }

    private fun downCount(){
        val time = remainTime().toInt()
        if (time == 0){
            hours.text = "00"
            minute.text = "00"
            second.text = "00"
            mList.forEach {
                it.money = it.oldMoney
            }
            payAdapter.notifyDataSetChanged()
        }else{
            val valueAnimator = ValueAnimator.ofInt(time,0)
            valueAnimator.duration = time.toLong()
            valueAnimator.interpolator = LinearInterpolator()
            valueAnimator.addUpdateListener { animation ->
                val t = animation?.animatedValue as Int
                val str = TimeUtils.millisecondToHHmmss(t.toLong())
                val timeList = str.split(":")
                hours.text = timeList[0]
                minute.text = timeList[1]
                second.text = timeList[2]
//                if (t < 1000){
//                    mList.forEach {
//                        it.money = it.oldMoney
//                    }
//                    payAdapter.notifyDataSetChanged()
//                }
            }
            valueAnimator.start()
        }
    }

    private fun buyVip() {
        if (exitDialog.isShowing)
            exitDialog.dismiss()
        isPaying = true
        showDialog = 0
        Log.e("支付测试----->", "开始执行充值事件")
        var bean: PayBean = PayBean()
        var payType = ""
        //遍历数组,找到被选中的对象
        bean = mList[selectItem]
        if (paySelectType == ALI_PAY) {
            payType = Constants.PAY_TYPE_ALI
            if (!PackageUtils.isAppExist(this, Constants.PACKAGE_NAME_ALI)) {
                ToastUtil.showCenterToast("支付宝未安装")
                return
            }
        } else {
            payType = Constants.PAY_TYPE_WX
            if (!PackageUtils.isAppExist(this, Constants.PACKAGE_NAME_WX)) {
                ToastUtil.showCenterToast("微信未安装")
                return
            }
        }
        loadingDialog.show()
        loadingDialog.isVisibility()
        MobclickAgent.onEvent(this,BaseConstant.launch_payment)
        PayUtils.payByWXOrALI(this, web, bean, payType)
    }

    override fun clickPriceItem(position: Int) {
        tv_price.text = "${Utils.moneyFormat(mList[position].money)}元"
        selectItem = position
    }

    /**
     * 进入购买界面showDialog为0
     * 当点击购买后，从支付界面回来showDialog为1
     */
    override fun onResume() {
        super.onResume()
        Log.e("ispaying", "" + isPaying)
        if (isPaying) {
            showDialog++
            e("showDialog-------->$showDialog")
            if (showDialog == 1) {
                Timer().schedule(object : TimerTask() {
                    override fun run() {
                        if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN_THIRD)) {
                            otherLogin()
                        } else {
                            login()
                        }
                    }
                }, 3000)
                isPaying = false
                showDialog = 0
            }
        }
        if (SPUtil.getInstance().getBoolean(Constants.IS_VIP)) {
            finish()
        }
    }

    private fun otherLogin() {
        val mMap: MutableMap<String, String> = TreeMap()
        mMap["openId"] = SPUtil.getInstance().getString(Constants.OPENID)
        mMap["type"] = SPUtil.getInstance().getString(Constants.LOGIN_TYPE)
        HttpUtils.getData(mMap, Constants.LOGIN_THIRD, object : HttpUtils.RequestCallback {
            override fun onSuccess(response: String) {
                e("支付后--------->$response")
                try {
                    val str: String
                    str = try {
                        GsonUtils.parseObject(response, UserBean::class.java).msg
                    } catch (e: Exception) {
                        GsonUtils.parseObject(response, DataBean::class.java).msg
                    }
                    if (str == "OK") {
                        SPUtil.getInstance().putString(Constants.USER_BEAN, response)
                        SPUtil.getInstance().putBoolean(Constants.IS_LOGIN_THIRD, true)
                        if (GetDataUtils.isVip()) {
                            runOnUiThread {
                                MobclickAgent.onEvent(this@PayActivity,BaseConstant.pay_success)
                                ToastUtil.showCenterToast("支付成功")
                                finish()
                            }

                        } else {
                            runOnUiThread {
                                MobclickAgent.onEvent(this@PayActivity,BaseConstant.pay_faild)
                                ToastUtil.showCenterToast("支付失败")
                                loadingDialog.dismiss()
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("校验失败，请重新登录")
                    }
                }
            }

            override fun onFailure(msg: String?, e: Exception?) {
                runOnUiThread {
                    ToastUtil.showCenterToast("校验失败，请重新登录")
                }
            }
        })
    }


    private fun login() {
        val map: MutableMap<String, String> = TreeMap()
        map["mobile"] = SPUtil.getInstance().getString(Constants.USER_NAME)
        map["password"] = MD5Utlis.md5(SPUtil.getInstance().getString(Constants.USER_PWD))
        HttpUtils.getData(map, Constants.LOGIN, object : HttpUtils.RequestCallback {
            override fun onSuccess(response: String) {
                e("支付后--------->$response")
                try {
                    val str: String = try {
                        GsonUtils.parseObject(response, UserBean::class.java).msg
                    } catch (e: Exception) {
                        GsonUtils.parseObject(response, DataBean::class.java).msg
                    }
                    if (str == "OK") {
                        SPUtil.getInstance().putString(Constants.USER_BEAN, response)
                        if (GetDataUtils.isVip()) {
                            runOnUiThread {
                                MobclickAgent.onEvent(this@PayActivity,BaseConstant.pay_success)
                                ToastUtil.showCenterToast("支付成功")
                                finish()
                            }
                        } else {
                            runOnUiThread {
                                MobclickAgent.onEvent(this@PayActivity,BaseConstant.pay_faild)
                                ToastUtil.showCenterToast("支付失败")
                            }
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        ToastUtil.showCenterToast("校验失败，请重新登录")
                    }
                }
            }

            override fun onFailure(msg: String?, e: Exception?) {
                runOnUiThread {
                    ToastUtil.showCenterToast("校验失败，请重新登录")
                }
            }
        })
    }

    override fun onBackPressed() {
        if (remainTime() == 0L)
            finish()
        else
            exitDialog.show()
    }

    override fun onDestroy() {
        if (exitDialog.isShowing)
            exitDialog.dismiss()
        super.onDestroy()
    }
}