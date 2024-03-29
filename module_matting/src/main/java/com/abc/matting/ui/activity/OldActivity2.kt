package com.abc.matting.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraCharacteristics
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.ui.dialog.CameraVipTipDialog
import com.abc.matting.ui.dialog.SelectAgeDialog
import com.abc.matting.ui.dialog.SelectSexDialog
import com.abc.matting.utils.*
import com.abc.matting.utils.camera.Camera2Loader
import com.abc.matting.utils.camera.CameraLoader
import com.abc.matting.utils.camera.doOnLayout
import com.feisukj.base.BaseApplication
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.ToastUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.activity_old2.*
import org.json.JSONObject

class OldActivity2 : BaseActivity(), SelectAgeDialog.SelectAgeCallback,
    SelectSexDialog.SelectSexCallback {

    companion object {

        const val typeKey = "typeKey"
        const val TYPE_OLD = "old"
        const val TYPE_YOUNG = "young"
        const val TYPE_COMIC = "comic"
        const val TYPE_SEX = "sex"

        const val ALBUM_CODE = 1110
    }

    private var bitmap: Bitmap? = null
    private var needCamera = true
    private var type = ""
    private lateinit var ageDialog: SelectAgeDialog
    private lateinit var sexDialog: SelectSexDialog
    private val cameraLoader: CameraLoader by lazy {
        Camera2Loader(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_old2

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()

        type = intent.getStringExtra(typeKey)?: TYPE_OLD
        barTitle.text = when(type){
            TYPE_OLD -> "变老相机"
            TYPE_YOUNG -> "童颜相机"
            TYPE_SEX -> "性别转换"
            TYPE_COMIC -> "一键漫画脸"
            else -> ""
        }
        ageDialog = SelectAgeDialog(this)
        ageDialog.setCallback(this)
        ageDialog.setType(type)

        sexDialog = SelectSexDialog(this)
        sexDialog.setCallback(this)

        cameraLoader.setOnPreviewFrameListener { data, width, height ->
            gpuImageView.updatePreviewFrame(data, width, height)
        }
        gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)
        initClick()
    }

    private fun initClick(){

        barBack.setOnClickListener {
            finish()
        }

        shutter.setOnClickListener {
            if (GetDataUtils.isVip()){
                bitmap = gpuImageView.capture()
                cameraLoader.onPause()
                img.setImageBitmap(bitmap)
                invisible(shutter,album,transition)
                visible(img,over,remake)
            }else{
                CameraVipTipDialog(this).show()
            }

        }

        remake.setOnClickListener {
            visible(shutter,album,transition)
            invisible(img,over,remake)
            gpuImageView.doOnLayout {
                cameraLoader.onResume(it.width, it.height)
            }
        }

        transition.setOnClickListener {
            if (GetDataUtils.isVip()){
                cameraLoader.switchCamera()
                gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
                if (cameraLoader.getCameraFacing() != CameraCharacteristics.LENS_FACING_BACK)
                    gpuImageView.scaleX = -1f
                else
                    gpuImageView.scaleX = 1f
            }else{
                CameraVipTipDialog(this).show()
            }

        }

        album.setOnClickListener {
            if (GetDataUtils.isVip()){
                needCamera = false
                PermissionUtils.askStorageAndCameraPermission(this){
                    cameraLoader.onPause()
                    PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .isPreviewImage(true)
//                    .freeStyleCropEnabled(true)
                        .setPictureUIStyle(Resources.getWhiteStyle())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(ALBUM_CODE)
                }
            }else{
                CameraVipTipDialog(this).show()
            }
        }

        over.setOnClickListener {
            when(type){
                TYPE_OLD,TYPE_YOUNG -> ageDialog.show()
                TYPE_COMIC -> comic()
                TYPE_SEX -> sexDialog.show()
            }
        }
    }

    private fun getRotation(orientation: Int): Rotation {
        return when (orientation) {
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    override fun onResume() {
        super.onResume()
        if (needCamera){
            gpuImageView.doOnLayout {
                try {
                    BaseApplication.handler.postDelayed({
                        cameraLoader.onResume(it.width, it.height)
                    },500)
                }catch (e: Exception){
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ALBUM_CODE -> {
                val path = Utils.getReal(PictureSelector.obtainMultipleResult(data))
                if (path == ""){
                    visible(shutter,album,transition)
                    invisible(img,over,remake)
                    needCamera = true
                    gpuImageView.doOnLayout {
                        cameraLoader.onResume(it.width,it.height)
                    }
                    return
                }
                bitmap = BitmapFactory.decodeFile(path)
                invisible(shutter,album,transition)
                visible(img,over,remake)
                img.setImageBitmap(bitmap)
            }
        }
    }

    override fun startToAge(age: Int) {
        loadingDialog.show()
        Thread{
            var url = ""
            try {
                val response = EffectUtils.toOld(age.toLong(), Base64Utils.bitmapToBase64(bitmap))
                LogUtils.e("---------------url = $response")
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            }catch (e : Exception){
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()){

                    return@runOnUiThread
                }
                val title = if (type == TYPE_OLD) "变老相机" else "童颜相机"
                val intent = Intent(this,EffectEndActivity::class.java)
                intent.putExtra(EffectEndActivity.titleKey,title)
                intent.putExtra(EffectEndActivity.imgUrlKey,url)
                startActivity(intent)
                finish()
            }
        }.start()
    }

    private fun comic(){
        loadingDialog.show()
        Thread{
            var url = ""
            try {
                val response = EffectUtils.toComic(Base64Utils.bitmapToBase64(bitmap))
                LogUtils.e("---------------url = $response")
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            }catch (e : Exception){
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()){
                    return@runOnUiThread
                }
                val title = "一键漫画脸"
                val intent = Intent(this,EffectEndActivity::class.java)
                intent.putExtra(EffectEndActivity.titleKey,title)
                intent.putExtra(EffectEndActivity.imgUrlKey,url)
                startActivity(intent)
                finish()
            }
        }.start()
    }

    override fun selectSex(type: Long) {
        loadingDialog.show()
        Thread{
            var url = ""
            try {
                val response = EffectUtils.toSex(type,Base64Utils.bitmapToBase64(bitmap))
                LogUtils.e("---------------url = $response")
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            }catch (e : Exception){
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()){
                    ToastUtil.showCenterToast("转换出错")
                    return@runOnUiThread
                }
                val intent = Intent(this,EffectEndActivity::class.java)
                intent.putExtra(EffectEndActivity.titleKey,"性别转换")
                intent.putExtra(EffectEndActivity.imgUrlKey,url)
                startActivity(intent)
                finish()
            }
        }.start()
    }
}