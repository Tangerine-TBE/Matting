package com.abc.matting.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.abc.matting.R
import com.bumptech.glide.Glide
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.ToastUtil
import kotlinx.android.synthetic.main.activity_cutout_over.*
import java.io.File

class CutoutOverActivity : BaseActivity() {

    companion object{
        const val IMG_PATH_KEY = "img_path_key"
    }

    private var path = ""

    override fun getLayoutId(): Int = R.layout.activity_cutout_over

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(true).init()

        path = intent.getStringExtra(IMG_PATH_KEY)?:""
        if (path == ""){
            ToastUtil.showCenterToast("打开图片失败")
            finish()
            return
        }
        Glide.with(this).load(path).into(image)
        savePath.text = path

        barBack.setOnClickListener {
            finish()
        }
        goMain.setOnClickListener {
            finish()
        }
        goBeauty.setOnClickListener {
            val intent = Intent(this,BeautyActivity::class.java)
            intent.putExtra(BeautyActivity.IMG_PATH_KEY,path)
            startActivity(intent)
            finish()
        }
        share.setOnClickListener {
            shareFileToAll(path)
        }
    }

    private fun shareFileToAll(path: String){
        try {
            val intent = Intent()
            intent.action = Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_SUBJECT, "选择分享")
            var uri : Uri? = null
            uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
                FileProvider.getUriForFile(mContext,mContext.packageName+".fileprovider",
                    File(path)
                )
            }else{
                Uri.fromFile(File(path))
            }
            intent.putExtra(Intent.EXTRA_STREAM,uri)
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            intent.type = "image/*"
            mContext.startActivity(Intent.createChooser(intent,"分享"))
        }catch (e : Exception){
            e.printStackTrace()
            ToastUtil.showCenterToast("分享失败")
        }
    }
}