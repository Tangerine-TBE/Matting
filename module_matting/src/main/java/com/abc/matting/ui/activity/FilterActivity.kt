package com.abc.matting.ui.activity

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.adapter.FilterAdapter
import com.abc.matting.bean.FilterBean
import com.abc.matting.utils.GetDataUtils
import com.abc.matting.utils.PermissionUtils
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.BitmapUtils
import com.feisukj.base.util.ToastUtil
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_filter.*
import java.io.File

class FilterActivity : BaseActivity(), FilterAdapter.FilterCallback {
    companion object{
        const val IMG_PATH = "img_page"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private var uri = ""
    private var mList: Array<FilterBean> = arrayOf()
    private var adapter: FilterAdapter = FilterAdapter()

    override fun getLayoutId(): Int = R.layout.activity_filter

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(true).init()

        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""

        mList = Resources.getCameraFilter()
        adapter.setData(mList)
        adapter.setCallBack(this)
        adapter.setNeedVip(cachePath == "" && cacheFileName == "")
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.adapter = adapter

        uri = intent.getStringExtra(AdjustActivity.IMG_PATH)?:""
        if (uri == ""){
            ToastUtil.showCenterToast("加载图片失败，请重试")
            finish()
        }
        gpuImageView.setImage(Uri.fromFile(File(uri)))
        gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        gpuImageView.setBackgroundColor(255f,255f,255f)

        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askStorageAndCameraPermission(this){
                if (cachePath != "" && cacheFileName != ""){
                    val rootFile = File(cachePath)
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    loadingDialog.setTitleText("更改中...")
                    loadingDialog.show()
                    Thread{
                        BitmapUtils.saveImageToGallery(gpuImageView.gpuImage.bitmapWithFilterApplied,BaseConstant.cachePath,cacheFileName,false)
                        runOnUiThread {
                            loadingDialog.dismiss()
                            val intent = Intent()
                            intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                            setResult(BeautyActivity.BACK,intent)
                            finish()
                        }
                    }.start()
                }else{
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val path = BaseConstant.savePath+"/"+fileName
                    loadingDialog.setTitleText("保存中...")
                    loadingDialog.show()
                    Thread{
                        BitmapUtils.saveImageToGallery(gpuImageView.gpuImage.bitmapWithFilterApplied,BaseConstant.savePath,fileName,true)
                        runOnUiThread {
                            loadingDialog.dismiss()
                            val intent = Intent(this, DetailsActivity::class.java)
                            intent.putExtra(DetailsActivity.PATH_KEY, path)
                            startActivity(intent)
                            finish()
                        }
                    }.start()
//                    gpuImageView.saveToPictures(BaseConstant.savePath,fileName){
//                        loadingDialog.dismiss()
//                        val intent = Intent(this,DetailsActivity::class.java)
//                        intent.putExtra(DetailsActivity.PATH_KEY,path)
//                        startActivity(intent)
//                        finish()
//                    }
                }
            }
        }

        barBack.setOnClickListener {
            finish()
        }

        no.setOnClickListener {
            finish()
        }
    }

    override fun clickFilterItem(position: Int) {
        if (mList[position].needVip&&!GetDataUtils.isVip()&&cachePath == "" && cacheFileName == ""){
            startActivity(Intent(this,PayActivity::class.java))
        }else{
            gpuImageView.filter = mList[position].filterType
            gpuImageView.requestRender()
        }
    }

}