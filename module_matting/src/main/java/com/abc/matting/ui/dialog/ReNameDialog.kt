package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.abc.matting.R
import com.abc.matting.bean.PictureBean
import kotlinx.android.synthetic.main.dialog_rename.*

class ReNameDialog(mContext: Context) : Dialog(mContext,R.style.MyDialog) {

    private var callback: ReNameCallback? = null
    private var fileBean: PictureBean? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_rename)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        var fileName = fileBean?.fileName
        var fileType = fileName?.substring(fileName.lastIndexOf("."))
        fileName = fileName?.substring(0,fileName.lastIndexOf("."))
        edit.setText(fileName)
        cancel.setOnClickListener {
            dismiss()
        }
        confirm.setOnClickListener {
            callback?.rename(fileBean!!,edit.text.toString()+fileType)
        }
    }

    fun setBean(pictureBean: PictureBean){
        this.fileBean = pictureBean
    }

    fun setCallback(callback: ReNameCallback){
        this.callback = callback
    }

    interface ReNameCallback{
        fun rename(bean: PictureBean,name: String)
    }
}