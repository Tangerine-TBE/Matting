package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.abc.matting.R
import com.abc.matting.ui.activity.PayActivity
import kotlinx.android.synthetic.main.dialog_pay_exit.*

class PayExitDialog(context: Context,val toDo: ((Int)->Unit),val closeToDo: (()->Unit)): Dialog(context, R.style.MyDialog) {

    private var payType = 0
    set(value) {
        field = value
        when(payType){
            PayActivity.WX_PAY -> {
                iv_wx.setImageResource(R.drawable.ic_vip_vip_dialog_select_y)
                iv_ali.setImageResource(R.drawable.ic_vip_vip_dialog_select_n)
            }
            PayActivity.ALI_PAY -> {
                iv_wx.setImageResource(R.drawable.ic_vip_vip_dialog_select_n)
                iv_ali.setImageResource(R.drawable.ic_vip_vip_dialog_select_y)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_pay_exit)
        val a = window?.attributes
        a?.width = WindowManager.LayoutParams.WRAP_CONTENT
        a?.height = WindowManager.LayoutParams.WRAP_CONTENT
        a?.gravity = Gravity.CENTER

        payType = PayActivity.WX_PAY
        initView()
    }

    private fun initView(){
        close.setOnClickListener {
            dismiss()
            closeToDo.invoke()
        }
        wxPay.setOnClickListener{
            payType = PayActivity.WX_PAY
        }
        aliPay.setOnClickListener {
            payType = PayActivity.ALI_PAY
        }
        btn.setOnClickListener {
            toDo.invoke(payType)
        }
    }
}