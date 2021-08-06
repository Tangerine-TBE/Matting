package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import com.abc.matting.R
import com.abc.matting.ui.activity.PayActivity
import kotlinx.android.synthetic.main.dialog_camera_vip_tip.*

class CameraVipTipDialog(val mContext: Context): Dialog(mContext, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_camera_vip_tip)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT

        confirm.setOnClickListener {
            mContext.startActivity(Intent(mContext,PayActivity::class.java))
        }
        cancel.setOnClickListener {
            dismiss()
        }
    }
}