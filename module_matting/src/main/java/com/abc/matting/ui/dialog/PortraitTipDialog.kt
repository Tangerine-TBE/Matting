package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.abc.matting.R
import kotlinx.android.synthetic.main.dialog_portrait_tip.*

class PortraitTipDialog(mContext: Context): Dialog(mContext, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_portrait_tip)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        yes.setOnClickListener {
            dismiss()
        }
    }
}