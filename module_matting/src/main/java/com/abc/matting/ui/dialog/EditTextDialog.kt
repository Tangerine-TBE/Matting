package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.abc.matting.R
import kotlinx.android.synthetic.main.dialog_edit_text.*

class EditTextDialog(val mContext: Context,val callback: EditTextDialog.EditTextCallback): Dialog(mContext) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_edit_text)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        la?.gravity = Gravity.CENTER
        initView()
    }

    private fun initView(){
        cancel.setOnClickListener {
            dismiss()
        }
        confirm.setOnClickListener {
            callback.editText(edit_View.text.toString())
            dismiss()
        }
    }

    interface EditTextCallback{
        fun editText(string: String)
    }
}