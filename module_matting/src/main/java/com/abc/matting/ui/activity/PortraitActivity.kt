package com.abc.matting.ui.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.text.Layout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.adapter.PortraitBackgroundAdapter
import com.abc.matting.adapter.PortraitBackgroundGroupAdapter
import com.abc.matting.adapter.PortraitTextColorAdapter
import com.abc.matting.bean.BackgroundBean
import com.abc.matting.bean.BackgroundGroupBean
import com.abc.matting.ui.dialog.EditTextDialog
import com.abc.matting.ui.dialog.NoSaveDialog
import com.abc.matting.ui.dialog.PortraitTipDialog
import com.abc.matting.utils.*
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.BitmapUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.ToastUtil
import com.hjq.permissions.Permission
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import com.xiaopo.flying.sticker.*
import kotlinx.android.synthetic.main.activity_portrait.*
import org.json.JSONObject
import java.io.File

class PortraitActivity : BaseActivity(),
    PortraitBackgroundGroupAdapter.PortraitBackgroundGroupCallback,
    PortraitBackgroundAdapter.PortraitBackgroundCallback,
    PortraitTextColorAdapter.PortraitTextColorCallback, StickerView.OnStickerOperationListener {

    companion object {
        const val CUTOUT_CODE = 101
        const val BG_CODE = 102

        const val WORK_TYPE_KEY = "work_type_key"
        const val TYPE_PORTRAIT = "portrait"
        const val TYPE_INTELLIGENT = "intelligent"
    }

    private lateinit var groupList: Array<BackgroundGroupBean>
    private lateinit var naturalList: Array<BackgroundBean>
    private lateinit var carList: Array<BackgroundBean>
    private lateinit var beautyList: Array<BackgroundBean>
    private lateinit var starList: Array<BackgroundBean>
    private lateinit var gameList: Array<BackgroundBean>
    private lateinit var sportList: Array<BackgroundBean>
    private lateinit var animalList: Array<BackgroundBean>
    private lateinit var movList: Array<BackgroundBean>
    private lateinit var artisticList: Array<BackgroundBean>
    private lateinit var animeList: Array<BackgroundBean>
    private lateinit var textList: Array<BackgroundBean>
    private lateinit var couplesList: Array<BackgroundBean>
    private lateinit var contractedList: Array<BackgroundBean>

    private lateinit var colorList: Array<Int>

    private val backgroundGroupAdapter = PortraitBackgroundGroupAdapter()
    private val backgroundAdapter = PortraitBackgroundAdapter()
    private val colorAdapter = PortraitTextColorAdapter()

    private var isBack = false
    private var canSetBg = true

    private var type = ""
    //选中的sticker
    private var selectSticker : Sticker? = null

    override fun getLayoutId(): Int = R.layout.activity_portrait

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(true).init()

        type = intent.getStringExtra(WORK_TYPE_KEY) ?: TYPE_PORTRAIT

        initList()
        initClick()

        backgroundGroupAdapter.setData(groupList)
        backgroundGroupAdapter.setCallback(this)
        backgroundAdapter.setCallback(this)

        colorAdapter.setData(colorList)
        colorAdapter.setCallback(this)

        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = backgroundGroupAdapter
        colorRecycle.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL, false)
        colorRecycle.adapter = colorAdapter

        stickerView.onStickerOperationListener = this
        val deleteIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this,R.drawable.ic_sticker_close),
            BitmapStickerIcon.LEFT_TOP)
        deleteIcon.iconEvent = DeleteIconEvent()
        val zoomIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this,R.drawable.ic_sticker_rotate),
            BitmapStickerIcon.RIGHT_BOTOM)
        zoomIcon.iconEvent = ZoomIconEvent()
        val editIcon = BitmapStickerIcon(
            ContextCompat.getDrawable(this,R.drawable.ic_sticker_edit),
            BitmapStickerIcon.RIGHT_TOP)
        editIcon.iconEvent = FlipHorizontallyEvent()
        stickerView.icons = arrayListOf(deleteIcon,zoomIcon,editIcon)
        stickerView.isLocked = false
        stickerView.isConstrained = true

        PermissionUtils.askPermission(this,Permission.MANAGE_EXTERNAL_STORAGE) {
            PictureSelector.create(this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isPreviewImage(false)
                .isCamera(false)
                .setPictureUIStyle(Resources.getWhiteStyle())
                .selectionMode(PictureConfig.SINGLE)
                .forResult(CUTOUT_CODE)
        }
        if (type == TYPE_PORTRAIT)
            ToastUtil.showCenterToast("请选择带有人像的图片")
    }

    private fun initList() {
        groupList = Resources.getBackgroundGroup()
        naturalList = Resources.getNaturalBackground()
        carList = Resources.getCarBackground()
        beautyList = Resources.getBeautyBackground()
        starList = Resources.getStarBackground()
        sportList = Resources.getSportsBackground()
        gameList = Resources.getGameBackground()
        animalList = Resources.getAnimalBackground()
        movList = Resources.getMovBackground()
        artisticList = Resources.getArtisticBackground()
        animeList = Resources.getAnimeBackground()
        textList = Resources.getTextBackground()
        couplesList = Resources.getCouplesBackground()
        contractedList = Resources.getContractedBackground()

        colorList = Resources.getTextColorList()
    }

    private fun initClick() {
        background.setOnClickListener {
            visible(arrow_background,bgRootView)
            invisible(arrow_move, arrow_eraser, arrow_text)
            gone(colorRecycle)
        }

        move.setOnClickListener {
            visible(arrow_move)
            invisible(arrow_background, arrow_eraser, arrow_text)
        }

        eraser.setOnClickListener {
            visible(arrow_eraser)
            invisible(arrow_background, arrow_move, arrow_text)
        }

        text.setOnClickListener {
            visible(arrow_text,colorRecycle)
            invisible(arrow_background, arrow_move, arrow_eraser)
            gone(bgRootView)
            val sticker = TextSticker(this)
            sticker.text = "双击输入文字"
            sticker.setTextColor(Color.BLACK)
            sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)
            sticker.resizeText()
            stickerView.addSticker(sticker)
        }

        noBg.setOnClickListener {
            if (isBack) {
                isBack = !isBack
                recycler.adapter = backgroundGroupAdapter
                image_big.setImageResource(R.drawable.shape_portrait_item_no_bg)
                image_small.setImageResource(R.drawable.ic_portrait_item_no_bg)
                name.text = "无背景"
            } else {
                srcImg.setImageBitmap(BitmapUtils.getBitmap(this,R.drawable.shape_portrait_transparent_bg,centerView.width,centerView.height))
            }
        }

        save.setOnClickListener {
            //免费使用一次，后面需要VIP
            val isFirst = SPUtil.getInstance().getBoolean("firstCutout",true)
            if (isFirst || GetDataUtils.isVip()){
                PermissionUtils.askPermission(this, Permission.MANAGE_EXTERNAL_STORAGE){
                    loadingDialog.show()
                    val rootFile = File(BaseConstant.savePath)
                    val savePath = BaseConstant.savePath
                    val saveFileName = "${System.currentTimeMillis()}.jpg"
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    Thread{
                        stickerView.save(savePath,saveFileName,true)
                        SPUtil.getInstance().putBoolean("firstCutout",false)
                        runOnUiThread {
                            val intent = Intent(this,CutoutOverActivity::class.java)
                            intent.putExtra(CutoutOverActivity.IMG_PATH_KEY,"${savePath}/${saveFileName}")
                            startActivity(intent)
                            loadingDialog.dismiss()
                            finish()
                        }
                    }.start()
                }
            }else{
                startActivity(Intent(this,PayActivity::class.java))
            }

        }

        barBack.setOnClickListener {
            NoSaveDialog(this,{
                finish()
            },{

            }) .setTitleText("抠图尚未保存，是否确定退出编辑").setConfirmText("退出").setCancelText("再想想").show()
        }

        help.setOnClickListener {
            PortraitTipDialog(this).show()
        }
    }

    private fun setList(list: Array<BackgroundBean>) {
        isBack = true

        backgroundAdapter.setData(list)
        recycler.adapter = backgroundAdapter

        image_big.setImageResource(R.drawable.shape_white_bg_radius_4dp)
        image_small.setImageResource(R.drawable.ic_portrait_item_back)
        name.text = "返回"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CUTOUT_CODE -> {
                val path = Utils.getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "") {
                    finish()
                    return
                }
                if (path.isEmpty()) {
                    ToastUtil.showCenterToast("载入图片出错")
                    finish()
                    return
                }
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    finish()
                    return
                }
                val bitmap = getImage(path)
                if (bitmap == null){
                    ToastUtil.showToast("载入图片出错")
                    finish()
                    return
                }
                loadingDialog.show()
                Thread {
                    var imageBase64 = ""
                    try {
                        val response = if (type == TYPE_PORTRAIT) {
                            if (EffectUtils.analyzeTheFace(Base64Utils.bitmapToBase64(bitmap)))
                                EffectUtils.portraitCutout(Base64Utils.bitmapToBase64(bitmap))
                            else{
                                loadingDialog.dismiss()
                                return@Thread
                            }

                        } else
                            EffectUtils.portraitCutout(Base64Utils.bitmapToBase64(bitmap))
//                            EffectUtils.intelligentCutout(path)
                        LogUtils.e("---------------url = $response")
                        val obj = JSONObject(response)
                        imageBase64 = if (type == TYPE_PORTRAIT){
                            obj.getString("ResultImage")
                        }  else{
                            obj.getString("ResultImage")
//                            val data = obj.getJSONObject("data")
//                            data.getString("imageBase64")
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    if (imageBase64 == "") {
                        runOnUiThread {
                            loadingDialog.dismiss()
                            finish()
                        }
                    } else {
                        val img = BitmapDrawable(resources, Base64Utils.base64ToBitmap(imageBase64))
                        runOnUiThread {
                            if (canSetBg) {
                                canSetBg = false
                                srcImg.setImageBitmap(
                                    BitmapUtils.getBitmap(
                                        this,
                                        R.drawable.shape_portrait_transparent_bg,
                                        centerView.width,
                                        centerView.height
                                    )
                                )
                            }
                            stickerView.addSticker(DrawableSticker(img))
                            loadingDialog.dismiss()
                        }
                    }
                }.start()
            }
            BG_CODE -> {
                val path = Utils.getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                val bitmap = adjust(BitmapFactory.decodeFile(path),path)
                if (bitmap == null){
                    ToastUtil.showCenterToast("出错了，请重试")
                    return
                }
                srcImg.setImageBitmap(bitmap)
            }
        }
    }

    /**
     * 调整图片大小
     * */
    private fun adjust(bitmap: Bitmap): Bitmap?{
        val bitW = bitmap.width.toFloat()
        val bitH = bitmap.height.toFloat()
        val cenW = centerView.width.toFloat()
        val cenH = centerView.height.toFloat()
        val scale = if (bitW > bitH){
            cenW / bitW
        }else{
            cenH / bitH
        }
        return BitmapUtils.bitMapScale(bitmap,scale)
    }

    /**
     * 调整图片大小
     * */
    private fun adjust(bitmap: Bitmap,path: String): Bitmap?{
        val bitW = bitmap.width.toFloat()
        val bitH = bitmap.height.toFloat()
        val cenW = centerView.width.toFloat()
        val cenH = centerView.height.toFloat()
        val scale = if (bitW > bitH){
            cenW / bitW
        }else{
            cenH / bitH
        }
        return BitmapUtils.getBitmap(path,(bitW * scale).toInt(), (bitH * scale).toInt())
    }

    /**
     * 获取上传的图片
     * */
    private fun getImage(path: String): Bitmap? {
        val option = BitmapFactory.Options()
        option.inJustDecodeBounds = true
         BitmapFactory.decodeFile(path,option)
        if (option.outWidth == 0 || option.outHeight == 0 )
            return null
        if (option.outWidth < 1800 && option.outHeight < 1800)
            return BitmapFactory.decodeFile(path)
        else {
            val bitmap = if (option.outWidth > option.outHeight) {
                val s = 1800f / option.outWidth
                val h = (option.outHeight * s).toInt()
                val w = (option.outWidth * s).toInt()
                BitmapUtils.getBitmap(path, w, h)
            } else {
                val s = 1800f / option.outHeight
                val h = (option.outHeight * s).toInt()
                val w = (option.outWidth * s).toInt()
                BitmapUtils.getBitmap(path, w, h)
            }

            return bitmap
        }
    }

    override fun clickBackgroundGroupItem(position: Int) {
        when (position) {
            0 -> {
                //相册
                PermissionUtils.askPermission(this,Permission.MANAGE_EXTERNAL_STORAGE) {
                    PictureSelector.create(this)
                        .openGallery(PictureMimeType.ofImage())
                        .imageEngine(GlideEngine.createGlideEngine())
                        .isPreviewImage(false)
                        .isCamera(false)
                        .setPictureUIStyle(Resources.getWhiteStyle())
                        .selectionMode(PictureConfig.SINGLE)
                        .forResult(BG_CODE)
                }
            }
            1 -> {
                //自然
                setList(naturalList)
            }
            2 -> {
                //汽车
                setList(carList)
            }
            3 -> {
                //游戏
                setList(gameList)
            }
            4 -> {
                //美女
                setList(beautyList)
            }
            5 -> {
                //明星
                setList(starList)
            }
            6 -> {
                //运动
                setList(sportList)
            }
            7 -> {
                //动物
                setList(animalList)
            }
            8 -> {
                //影视
                setList(movList)
            }
            9 -> {
                //简约
                setList(contractedList)
            }
            10 -> {
                //动漫
                setList(animeList)
            }
            11 -> {
                //文字
                setList(textList)
            }
            12 -> {
                //情侣
                setList(couplesList)
            }
            13 -> {
                //意境
                setList(artisticList)
            }

        }
    }

    override fun clickBackgroundItem(item: BackgroundBean) {
        if (item.imgSrc == null){
            ToastUtil.showCenterToast("出错了，请重试")
            return
        }
        val bitmap = adjust(BitmapUtils.getBitmapFromAssetsFile(item.imgSrc))
        if (bitmap == null){
            ToastUtil.showCenterToast("出错了，请重试")
            return
        }
        srcImg.setImageBitmap(bitmap)
    }

    override fun selectTextColor(position: Int) {
        if (selectSticker != null && selectSticker is TextSticker){
            (selectSticker as TextSticker).setTextColor(colorList[position])
            stickerView.invalidate()
        }
    }

    override fun onStickerAdded(sticker: Sticker) {
        selectSticker = sticker
    }

    override fun onStickerClicked(sticker: Sticker) {
        selectSticker = sticker
        visible(frame)
        if (sticker is TextSticker){
            gone(bgRootView,arrow_background)
            visible(colorRecycle,arrow_text)
        }else{
            visible(bgRootView,arrow_background)
            gone(colorRecycle,arrow_text)
        }
    }

    override fun onStickerDeleted(sticker: Sticker) {
        visible(frame)
    }

    override fun onStickerDragFinished(sticker: Sticker) {
        visible(frame)
        selectSticker = sticker
    }

    override fun onStickerTouchedDown(sticker: Sticker) {
        gone(frame)
    }

    override fun onStickerZoomFinished(sticker: Sticker) {
        visible(frame)
        selectSticker = sticker
    }

    override fun onStickerFlipped(sticker: Sticker) {
        visible(frame)
        selectSticker = sticker
    }

    override fun onStickerDoubleTapped(sticker: Sticker) {
        if (sticker is TextSticker){
            EditTextDialog(this,object : EditTextDialog.EditTextCallback{
                override fun editText(string: String) {
                    (sticker as TextSticker).apply {
                        this.text = string
                        this.resizeText()
                    }
                    stickerView.invalidate()
                }

            }).show()
        }
    }

    override fun onBackPressed() {
        NoSaveDialog(this,{
            finish()
        },{}) .setTitleText("抠图尚未保存，是否确定推出编辑").setConfirmText("退出").setCancelText("再想想").show()
    }
}