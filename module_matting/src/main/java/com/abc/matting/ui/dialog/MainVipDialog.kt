package com.abc.matting.ui.dialog

import android.animation.ValueAnimator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import com.abc.matting.Constants
import com.abc.matting.R
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.TimeUtils
import kotlinx.android.synthetic.main.dialog_main_vip.*

class MainVipDialog(context: Context,val toDo : (()->Unit)) : Dialog(context, R.style.MyDialog) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_main_vip)
        val w = window?.attributes
        w?.width = WindowManager.LayoutParams.WRAP_CONTENT
        w?.height = WindowManager.LayoutParams.WRAP_CONTENT
        w?.gravity = Gravity.CENTER

        initView()
    }

    private fun initView(){
        downCount()

        close.setOnClickListener {
            dismiss()
        }

        btn.setOnClickListener {
            toDo.invoke()
            dismiss()
        }
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
            }
            valueAnimator.start()
        }
    }
}