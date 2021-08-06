package com.abc.matting.ui.activity

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.*
import android.hardware.camera2.params.OutputConfiguration
import android.hardware.camera2.params.SessionConfiguration
import android.media.ImageReader
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.HandlerThread
import android.util.SparseIntArray
import android.view.Surface
import android.view.TextureView
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.ui.dialog.CameraVipTipDialog
import com.abc.matting.utils.*
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.DeviceUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.lg
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.sr.cejuyiczclds.utils.getCloselyPreSize
import kotlinx.android.synthetic.main.activity_comic.*
import kotlinx.android.synthetic.main.activity_comic.album
import kotlinx.android.synthetic.main.activity_comic.over
import kotlinx.android.synthetic.main.activity_comic.shutter
import kotlinx.android.synthetic.main.activity_comic.test
import kotlinx.android.synthetic.main.activity_comic.textureView
import kotlinx.android.synthetic.main.activity_comic.transition
import kotlinx.android.synthetic.main.activity_old.*
import org.json.JSONObject

class ComicActivity : BaseActivity() {
    companion object {

        const val ALBUM_CODE = 1110

        private var ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }
    }

    private var mCameraManager: CameraManager? = null//摄像头管理器
    private var childHandler: Handler? = null
    private var mainHandler: Handler? = null
    private var mCameraID: String? = null//摄像头Id 0 为后  1 为前
    private var mImageReader: ImageReader? = null
    private var mCameraCaptureSession: CameraCaptureSession? = null
    private var mCameraDevice: CameraDevice? = null
    private var hasCapture = false
    private lateinit var surface: Surface
    private lateinit var surfaceTextureListener: TextureView.SurfaceTextureListener
    private var surfaceTextureAvailable=false
    private var bitmap: Bitmap? = null
    private var canInitCamera = true

    override fun getLayoutId(): Int = R.layout.activity_comic

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        mCameraID = "" + CameraCharacteristics.LENS_FACING_BACK//前摄像头
        shutter.setOnClickListener {
            if (GetDataUtils.isVip()){
                takePicture()
                invisible(it)
                visible(over)
            }else{
                CameraVipTipDialog(this).show()
            }

        }

        over.setOnClickListener {
            mCameraDevice?.close()
            mCameraCaptureSession?.close()
            bitmap = textureView?.bitmap
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

        transition.setOnClickListener {
            if (GetDataUtils.isVip()){
                if (textureView.visibility == View.VISIBLE)
                    mCameraID =
                        if (mCameraID == "" + CameraCharacteristics.LENS_FACING_FRONT)
                            "" + CameraCharacteristics.LENS_FACING_BACK
                        else
                            "" + CameraCharacteristics.LENS_FACING_FRONT
                LogUtils.e("-------------------$mCameraID")
                mCameraDevice?.close()
                mCameraCaptureSession?.close()
                invisible(over,test)
                visible(shutter,textureView)
                initCamera2()

            }else{
                CameraVipTipDialog(this).show()
            }

        }

        album.setOnClickListener {
            if (GetDataUtils.isVip()){
                mCameraDevice?.close()
                mCameraCaptureSession?.close()
                canInitCamera = false
                PermissionUtils.askStorageAndCameraPermission(this){
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

        textureView.surfaceTextureListener=object : TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(
                surface: SurfaceTexture,
                width: Int,
                height: Int
            ) {

            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {

            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture, width: Int, height: Int) {
                surfaceTextureAvailable=true
                initCamera2()
            }

        }
    }

    override fun onResume() {
        super.onResume()
        if (surfaceTextureAvailable&&canInitCamera){
            initCamera2()
        }
    }

    override fun onPause() {
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
        super.onPause()
    }

    /**
     * 初始化Camera2
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private fun initCamera2() {
        invisible(over,test)
        visible(shutter,album,transition)
        canInitCamera = true
        val handlerThread = HandlerThread("Camera2")
        handlerThread.start()
        childHandler = Handler(handlerThread.looper)
        mainHandler = Handler(mainLooper)
        mImageReader = ImageReader.newInstance(textureView.width, textureView.height, ImageFormat.JPEG, 1)
        mImageReader?.setOnImageAvailableListener(ImageReader.OnImageAvailableListener { reader ->
            //可以在这里处理拍照得到的临时照片 例如，写入本地
            mCameraDevice?.close()
            textureView?.visibility = View.GONE
//            iv_show?.visibility = View.VISIBLE
            // 拿到拍照照片数据
            val image = reader.acquireNextImage()
            val buffer = image.planes[0].buffer
            val bytes = ByteArray(buffer.remaining())
            buffer.get(bytes)//由缓冲区存入字节数组
            val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            test.setImageBitmap(bitmap)
//            if (bitmap != null) {
//                iv_show?.setImageBitmap(bitmap)
//            }
        }, mainHandler)
        //获取摄像头管理
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return
            }
            //打开摄像头
            mCameraManager?.openCamera(mCameraID!!, stateCallback, mainHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 摄像头创建监听
     */
    private val stateCallback = @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    object : CameraDevice.StateCallback() {
        override fun onOpened(camera: CameraDevice) {
            mCameraDevice = camera
            //开启预览
            lg("开启预览")
            takePreview()
        }

        override fun onDisconnected(camera: CameraDevice) {
            if (null != mCameraDevice) {
                lg("摄像onDisconnected")
                mCameraDevice?.close()
                mCameraDevice = null
            }
        }

        override fun onError(camera: CameraDevice, error: Int) {
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePreview() {
        try {
            // 创建预览需要的CaptureRequest.Builder
            val previewRequestBuilder = mCameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
            val surfaceTexture=textureView.surfaceTexture
            if (mCameraID==null) return
            val outputSizes=mCameraManager?.getCameraCharacteristics(mCameraID!!)?.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)?.getOutputSizes(
                SurfaceTexture::class.java)?.toList()?:return
            val size= getCloselyPreSize(true,
                DeviceUtils.getScreenWidth(this),
                DeviceUtils.getScreenHeight(
                    this),
                outputSizes)
            if (size!=null&&surfaceTexture!=null)
                surfaceTexture.setDefaultBufferSize(size.width, size.height)
            surface= Surface(surfaceTexture)
            previewRequestBuilder?.addTarget(surface)
            // 创建CameraCaptureSession，该对象负责管理处理预览请求和拍照请求
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                mCameraDevice?.createCaptureSession(SessionConfiguration(SessionConfiguration.SESSION_REGULAR,
                    arrayListOf(OutputConfiguration(surface),
                        OutputConfiguration(mImageReader?.surface!!)),this.mainExecutor,object : CameraCaptureSession.StateCallback() {
                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }

                        override fun onConfigured(session: CameraCaptureSession) {
                            if (null == mCameraDevice) return
                            // 当摄像头已经准备好时，开始显示预览
                            mCameraCaptureSession = session

                            try {
                                // 自动对焦
                                previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // 打开闪光灯
                                previewRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE,
                                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                                // 显示预览
                                val previewRequest = previewRequestBuilder?.build()
                                if (previewRequest != null)
                                    mCameraCaptureSession?.setRepeatingRequest(previewRequest,
                                        null,
                                        childHandler)
                            } catch (e: CameraAccessException) {
                            }
                        }

                    }))
            }else{
                mCameraDevice?.createCaptureSession(listOf(surface, mImageReader?.surface),
                    object : CameraCaptureSession.StateCallback() {
                        override fun onConfigureFailed(session: CameraCaptureSession) {

                        }

                        override fun onConfigured(session: CameraCaptureSession) {
                            if (null == mCameraDevice) return
                            // 当摄像头已经准备好时，开始显示预览
                            mCameraCaptureSession = session

                            try {
                                // 自动对焦
                                previewRequestBuilder?.set(CaptureRequest.CONTROL_AF_MODE,
                                    CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
                                // 打开闪光灯
                                previewRequestBuilder?.set(CaptureRequest.CONTROL_AE_MODE,
                                    CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
                                // 显示预览
                                val previewRequest = previewRequestBuilder?.build()
                                if (previewRequest != null)
                                    mCameraCaptureSession?.setRepeatingRequest(previewRequest,
                                        null,
                                        childHandler)
                            } catch (e: CameraAccessException) {
                            }
                        }

                    },
                    childHandler)
            }

        } catch (e: CameraAccessException) {
        }
    }

    /**
     * 拍照
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun takePicture() {
        if (mCameraDevice == null) return
        // 创建拍照需要的CaptureRequest.Builder
        val captureRequestBuilder: CaptureRequest.Builder
        try {
            captureRequestBuilder = mCameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
            // 将imageReader的surface作为CaptureRequest.Builder的目标
            captureRequestBuilder.addTarget(surface)
            // 自动对焦
            captureRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE)
            // 自动曝光
            captureRequestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
                CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH)
            // 获取手机方向
//            val rotation = windowManager.defaultDisplay.rotation
            val rotation = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R)
                windowManager.defaultDisplay.rotation
            else
                this.display?.rotation?:0
            // 根据设备方向计算设置照片的方向
            captureRequestBuilder.set(CaptureRequest.JPEG_ORIENTATION, ORIENTATIONS.get(rotation))
            //拍照
            val mCaptureRequest = captureRequestBuilder.build()

            mCameraCaptureSession?.stopRepeating()
            mCameraCaptureSession?.abortCaptures()
            mCameraCaptureSession?.capture(mCaptureRequest, null, childHandler)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            ALBUM_CODE -> {
                val path = Utils.getReal(PictureSelector.obtainMultipleResult(data))
                if (path == ""){
                    if (test.visibility!=View.VISIBLE)
                        initCamera2()
                    return
                }
                bitmap = BitmapFactory.decodeFile(path)
                invisible(textureView,shutter)
                visible(over,test)
                test.setImageBitmap(bitmap)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mCameraDevice?.close()
        mCameraCaptureSession?.close()
    }

}