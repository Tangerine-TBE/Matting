package com.abc.matting.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.content.FileProvider
import com.abc.matting.R
import com.bumptech.glide.Glide
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.ToastUtil
import kotlinx.android.synthetic.main.activity_details.*
import java.io.File

class DetailsActivity : BaseActivity() {
    companion object{
        const val PATH_KEY = "path_key"
    }

    private var path = ""

    override fun getLayoutId(): Int = R.layout.activity_details

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init()

        path = intent.getStringExtra(PATH_KEY)?:""
        if (path.isEmpty()){
            ToastUtil.showCenterToast("加载图片失败")
            finish()
        }
        Glide.with(this).load(path).into(image)
//        image.setImageBitmap(BitmapFactory.decodeFile(path))
        savePath.text = path

        barBack.setOnClickListener {
            finish()
        }
        goMain.setOnClickListener {
            setResult(MoreActivity.BACK)
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