package com.abc.matting.ui.activity

import android.Manifest
import android.app.Activity
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import com.abc.matting.Constants
import com.abc.matting.R
import com.alibaba.sdk.android.oss.*
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask
import com.alibaba.sdk.android.oss.model.PutObjectRequest
import com.alibaba.sdk.android.oss.model.PutObjectResult
import com.bumptech.glide.Glide
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.baseclass.ForegroundObserver
import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.*
import com.feisukj.base.util.maputils.MapUtils
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import id.zelory.compressor.Compressor
import id.zelory.compressor.constraint.destination
import id.zelory.compressor.constraint.format
import id.zelory.compressor.constraint.size
import kotlinx.android.synthetic.main.activity_feed_back.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

class FeedBackActivity : BaseActivity() {

    private val REQUEST_CODE_GALLERY_1 = 100
    private val REQUEST_CODE_GALLERY_2 = 200
    private var imageUrl1 : String = ""
    private var imageUrl2 : String = ""
    private var endpoint = "oss-cn-shenzhen.aliyuncs.com"
    private val credentialProvider: OSSCredentialProvider by lazy {
        OSSStsTokenCredentialProvider("LTAI5tPpidbvQ5KKiWH8tPA8",
            "X14R2fvX37crpA11dcLu93YMaDv2os",
            "")
    }
    private val conf = ClientConfiguration()
    private lateinit var oss: OSS
    private val cacheRootPath by lazy {
        cacheDir.absolutePath+File.separator+"upload"
    }
    private val options = BitmapFactory.Options()

    override fun getLayoutId(): Int = R.layout.activity_feed_back

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()

        conf.connectionTimeout = 15 * 1000 // 连接超时，默认15秒。
        conf.socketTimeout = 15 * 1000 // socket超时，默认15秒。
        conf.maxConcurrentRequest = 5 // 最大并发请求书，默认5个。
        conf.maxErrorRetry = 2 // 失败后最大重试次数，默认2次。

        oss = OSSClient(applicationContext, endpoint, credentialProvider, conf)

        val file = File(cacheRootPath)
        if (!file.exists())
            file.mkdirs()

        options.inPreferredConfig = Bitmap.Config.ALPHA_8
        options.inSampleSize = 10

        barBack.setOnClickListener {
            finish()
        }
        btn.setOnClickListener {
            FeedBack()
        }
        image1.setOnClickListener {
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE){
                ForegroundObserver.hideAd = true
                gallery(REQUEST_CODE_GALLERY_1)
            }

        }
        image2.setOnClickListener {
            askPermission(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE){
                ForegroundObserver.hideAd = true
                gallery(REQUEST_CODE_GALLERY_2)
            }
        }

        body.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                text_num_tip.text = "${p0?.length}/255字"
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }

    private fun gallery(code: Int) {
        val intent =
            Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 以startActivityForResult的方式启动一个activity用来获取返回的结果
        startActivityForResult(intent, code)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            when(requestCode){
                REQUEST_CODE_GALLERY_1 -> {
                    val uri = data?.data
                    uploadImage(uri, 1)
                }
                REQUEST_CODE_GALLERY_2 -> {
                    val uri = data?.data
                    uploadImage(uri, 2)
                }
            }
        }
    }

    /**
     * 上传图片
     * @param url
     * @param imagePath 图片路径
     * @return 新图片的路径
     * @throws IOException
     * @throws JSONException
     */
    @Throws(IOException::class, JSONException::class)
    fun uploadImage(imageUri: Uri?, id: Int) {
        loadingDialog.show()
        val path = getRealFilePath(this, imageUri)
        if(path.isNullOrEmpty()){
            toast("获取图片出错")
            loadingDialog.dismiss()
            return
        }
        val file = File(path)
        if (!file.exists()){
            toast("获取图片出错")
            loadingDialog.dismiss()
            return
        }
        if (BitmapFactory.decodeFile(path,options) == null){
            toast("获取图片出错")
            loadingDialog.dismiss()
            return
        }
        if (id == 1)
            Glide.with(this).load(imageUri).into(image1)
        else
            Glide.with(this).load(imageUri).into(image2)
        zipImg(path,id)
    }

    private fun upload(path: String,id: Int){
        val objectKey = getAliObjectKey(path)
        val put = PutObjectRequest(
            "feedback-img-public",
            objectKey,
            path
        )
        // 异步上传时可以设置进度回调。
        put.progressCallback =
            OSSProgressCallback { request, currentSize, totalSize ->
                LogUtils.d(
                    "OSS","PutObject   currentSize: $currentSize totalSize: $totalSize"
                )
            }
        val task: OSSAsyncTask<*> = oss.asyncPutObject(
            put,
            object : OSSCompletedCallback<PutObjectRequest?, PutObjectResult> {
                override fun onSuccess(request: PutObjectRequest?, result: PutObjectResult) {
                    LogUtils.d("OSS","PutObject   UploadSuccess")
                    LogUtils.d("OSS","ETag：${result.eTag}")
                    LogUtils.d("OSS","RequestId：${result.requestId}")
                    runOnUiThread {
                        toast("上传成功")
                        if (id == 1){
                            imageUrl1 = "https://feedback-img-public.oss-cn-shenzhen.aliyuncs.com/$objectKey"
                            LogUtils.e("---------------------${imageUrl1}")
                        }else{
                            imageUrl2 = "https://feedback-img-public.oss-cn-shenzhen.aliyuncs.com/$objectKey"
                            LogUtils.e("---------------------${imageUrl2}")
                        }
                        loadingDialog.dismiss()
                    }
                }

                override fun onFailure(
                    request: PutObjectRequest?,
                    clientExcepion: ClientException,
                    serviceException: ServiceException
                ) {
                    runOnUiThread {
                        loadingDialog.dismiss()
                        toast("上传失败")
                    }
                    // 请求异常。
                    if (clientExcepion != null) {
                        // 客户端异常，例如网络异常等。
                        clientExcepion.printStackTrace()
                    }
                    if (serviceException != null) {
                        // 服务端异常。
                        LogUtils.e("OSS","ErrorCode:${serviceException.errorCode}" )
                        LogUtils.e("OSS","RequestId:${serviceException.requestId}" )
                        LogUtils.e("OSS","HostId:${serviceException.hostId}")
                        LogUtils.e("OSS","RawMessage:${serviceException.rawMessage}")
                    }
                }
            })
// 取消上传任务。
// task.cancel();
// 等待上传任务完成。
// task.waitUntilFinished();
    }

    private fun zipImg(path: String,id: Int){
        val file = File(path)
        if (file.length()<1024*300)
            upload(path,id)
        val newPath = GlobalScope.launch {
            val fileName = file.name
            val end = fileName.lastIndexOf(".")
            val type = if (end == -1){
                ".jpg"
            }else{
                fileName.substring(end)
            }
            val newFile = Compressor.compress(this@FeedBackActivity,file){
                destination(File(cacheRootPath,"${System.currentTimeMillis()}$type"))
                size(1024*300)
                when (type.toLowerCase(Locale.ROOT)){
                    ".png" -> format(Bitmap.CompressFormat.PNG)
                    ".webp" -> format(Bitmap.CompressFormat.WEBP)
                    else -> format(Bitmap.CompressFormat.JPEG)
                }
            }
            if (newFile.exists()) upload(newFile.absolutePath,id) else upload(path,id)
        }
    }

    /**
     * 获取绝对路径
     * */
    private fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme: String? = uri.scheme
        var data: String? = null
        if (scheme == null)
            data = uri.path
        else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor: Cursor? = context.contentResolver
                    .query(uri, arrayOf<String>(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    private fun getAliObjectKey(path: String):String{
        val nowTime = System.currentTimeMillis()
        val strTime = SimpleDateFormat("yyyy/MM").format(nowTime)
        val fileName = path.substring(path.lastIndexOf(File.separator)+1)
        val fileType = fileName.substring(fileName.lastIndexOf(".")+1)
        return strTime+File.separator+nowTime+"."+fileType
    }

    private fun askPermission(vararg permission: String, method: () -> Unit) {
        XXPermissions.with(this)
            .permission(Permission.MANAGE_EXTERNAL_STORAGE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                    if (all) {
                        method.invoke()
                    }
                }

                override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                    if (never) {
                        XXPermissions.startPermissionActivity(this@FeedBackActivity, permissions)
                    }
                }

            })
    }

    private fun FeedBack(){
        if (body.text.isEmpty()){
            ToastUtil.showCenterToast("意见不可为空")
            return
        }
        var user_id_ = getUserId()
        if (user_id_==0)
            user_id_ = 111
        val map1: MutableMap<String, String> = HashMap()
        map1["user_id"] = "$user_id_"
        map1["content"] = body.text.toString()
        map1["wx_qq"] = "${qq.text}--${email.text}"
        map1["user_system"] = "1"
        map1["user_package"] = BaseConstant.packageName
        map1["package_chn"] = "天王星抠图"
        val map2: MutableMap<String, String> = HashMap()
        if (imageUrl1 != "")
            map2["img_one"] = imageUrl1
        if (imageUrl2 != "")
            map2["img_two"] = imageUrl2
        val map = setMapValues(map1, map2)
        //创建okHttpClient对象
        val client = OkHttpClient()
        val builder = FormBody.Builder()
        map.forEach {
            builder.add(it.key, it.value)
            LogUtils.e("------------${it.key}:${it.value}")
        }
        val resBody: RequestBody = builder.build()
        val request = Request.Builder()
                .url(Constants.BaseUrl + "/manysmall/public/addvice")
                .post(resBody)
                .build()
        val call = client.newCall(request)
        call.enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    ToastUtil.showCenterToast("无法连接服务器，请稍后重试")
                }
            }

            override fun onResponse(call: Call, response: Response) {
                try {
                    val str = response.body()?.string()
                    LogUtils.e("----------------$str")
                    if (!str.isNullOrEmpty()) {
                        val resp: JSONObject = JSONObject(str)
                        val code = resp.getString("code")
                        val msg = resp.getString("msg")
                        runOnUiThread {
                            body.setText("")
                            qq.setText("")
                            email.setText("")
                            imageUrl1 = ""
                            imageUrl2 = ""
                            image1.setImageResource(R.drawable.ic_feedback_image_add)
                            image2.setImageResource(R.drawable.ic_feedback_image_add)
                            ToastUtil.showCenterToast(msg)
                        }
                    } else {
                        runOnUiThread {
                            ToastUtil.showCenterToast("连接服务器出错")
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    runOnUiThread {
                        ToastUtil.showCenterToast("连接服务器出错")
                    }
                }
            }

        })
    }

    /**
     * @param userInfo 必填参数
     * @param map1 非必填参数
     * */
    private fun setMapValues(userInfo: MutableMap<String, String>, map1: MutableMap<String, String>):MutableMap<String, String>{
        //获取时间戳
        val currentTimeMillis = System.currentTimeMillis() / 1000
        val md5 = MD5Utlis.md5(Constants.TOKEN_VALUE + currentTimeMillis + Constants.NONCE_VALUE + "public/addvice" + MapUtils.sortMapByValue2(userInfo))
        val map: MutableMap<String, String> = HashMap()
        map[Constants.TIMESTAMP] = "$currentTimeMillis"
        map[Constants.SIGNATURE] = md5
        map.putAll(userInfo)
        map.putAll(map1)
        return map
    }

    private fun deleteFile(file: File){
        if (file.isFile)
            file.delete()
        else{
            file.listFiles()?.forEach {
                deleteFile(it)
            }
        }
    }

    override fun onDestroy() {
        GlobalScope.launch {
            val file = File(cacheRootPath)
            if ((file.listFiles()?.size ?: 0) > 0){
                deleteFile(file)
            }
        }
        super.onDestroy()
    }

    class UpImageBean {
        var code: String? = null
        var msg: String? = null
        var data: Data? = null

        inner class Data {
            var img_url: String? = null

        }
    }

    private fun getUserId(): Int {
        if (SPUtil.getInstance().getString(Constants.USER_BEAN) == null || SPUtil.getInstance().getString(Constants.USER_BEAN).isEmpty()) return 0
        try {
            val bean: UserBean = GsonUtils.parseObject(SPUtil.getInstance().getString(Constants.USER_BEAN), UserBean::class.java)
            return bean.data.id
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return 0
    }
}