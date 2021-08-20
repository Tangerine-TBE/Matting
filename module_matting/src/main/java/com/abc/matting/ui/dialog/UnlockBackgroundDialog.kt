package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.abc.matting.R
import kotlinx.android.synthetic.main.dialog_unlock_background.*

class UnlockBackgroundDialog(private val mContext: Context, private val buyVipUnit:(() ->Unit), private val seeVideoUnit:(() ->Unit)?): Dialog(mContext, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_unlock_background)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT

        seeVideo.setOnClickListener {
            seeVideoUnit?.invoke()
            dismiss()
        }
        buyVip.setOnClickListener {
            buyVipUnit.invoke()
            dismiss()
        }
    }
}