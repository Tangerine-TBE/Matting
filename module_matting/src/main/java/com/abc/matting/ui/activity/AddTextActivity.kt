package com.abc.matting.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Rect
import android.text.Layout
import android.view.MotionEvent
import android.view.View
import android.widget.SeekBar
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.matting.R
import com.abc.matting.Resources
import com.abc.matting.adapter.TextAdapter
import com.abc.matting.bean.StickerBean
import com.abc.matting.ui.dialog.EditTextDialog
import com.abc.matting.ui.dialog.TipDialog
import com.abc.matting.utils.PermissionUtils
import com.abc.matting.view.ColorPickerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.ToastUtil
import com.hjq.permissions.Permission
import com.xiaopo.flying.sticker.*
import kotlinx.android.synthetic.main.activity_add_text.*
import java.io.File

class AddTextActivity : BaseActivity(), TextAdapter.TextCallback, ColorPickerView.OnColorPickerChangeListener, StickerView.OnStickerOperationListener, SeekBar.OnSeekBarChangeListener {
    companion object{
        const val IMG_URI_KEY = "img_uri_key"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private val adapter: TextAdapter = TextAdapter()
    private var textList: Array<StickerBean> = arrayOf()

    //选中的sticker
    private var selectSticker : Sticker? = null
    private var mColor : Int = Color.BLACK

    override fun getLayoutId(): Int = R.layout.activity_add_text

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(false).init()

        val uri = intent.getStringExtra(IMG_URI_KEY)?:""
        if (uri == ""){
            ToastUtil.showCenterToast("载入图片失败")
            return
        }
        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""
        Glide.with(this)
            .load(uri)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(srcImg)

        textList = Resources.getTextSticker()
        adapter.setData(textList)
        adapter.setCallback(this)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        val deleteIcon = BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.ic_sticker_close),
            BitmapStickerIcon.LEFT_TOP)
        deleteIcon.iconEvent = DeleteIconEvent()
        val zoomIcon = BitmapStickerIcon(ContextCompat.getDrawable(this,R.drawable.ic_sticker_rotate),
            BitmapStickerIcon.RIGHT_BOTOM)
        zoomIcon.iconEvent = ZoomIconEvent()
        stickerView.icons = arrayListOf(deleteIcon,zoomIcon)
        stickerView.isLocked = false
        stickerView.isConstrained = true

        stickerView.onStickerOperationListener = this

        reset.setOnClickListener {
            val dialog = TipDialog(this)
            dialog.setTitle("是否清除所有文字")
            dialog.setConfirm {
                stickerView.removeAllStickers()
            }
            dialog.show()
        }

        show.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        stickerView.setShowSticker(false)
                    }
                    MotionEvent.ACTION_UP ->{
                        stickerView.setShowSticker(true)
                    }
                }
                return true
            }

        })

        barBack.setOnClickListener {
            finish()
        }

        text_style.setOnClickListener {
            text_style.setImageResource(R.drawable.ic_text_style_y)
            text_color.setImageResource(R.drawable.ic_text_color_n)
            visible(recycler)
            gone(colorPicker)
        }
        text_color.setOnClickListener {
            text_style.setImageResource(R.drawable.ic_text_style_n)
            text_color.setImageResource(R.drawable.ic_text_color_y)
            visible(colorPicker)
            gone(recycler)
        }
        no.setOnClickListener {
            finish()
        }
        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askPermission(this,Permission.MANAGE_EXTERNAL_STORAGE){
                if (cachePath != "" && cacheFileName != ""){
                    val rootFile = File(cachePath)
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    loadingDialog.setTitleText("更改中...")
                    loadingDialog.show()
                    Thread{
                        stickerView.save(cachePath,cacheFileName,false)
                        runOnUiThread {
                            val intent = Intent()
                            intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                            setResult(BeautyActivity.BACK,intent)
                            loadingDialog.dismiss()
                            finish()
                        }
                    }.start()
                }else{
                    loadingDialog.setTitleText("保存中...")
                    loadingDialog.show()
                    val rootFile = File(BaseConstant.savePath)
                    val savePath = BaseConstant.savePath
                    val saveFileName = "${System.currentTimeMillis()}.jpg"
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    Thread{
                        stickerView.save(savePath,saveFileName,true)
                        runOnUiThread {
                            loadingDialog.dismiss()
                            setResult(HomeActivity.BACK)
                            finish()
                        }
                    }.start()
                }

            }
        }

        colorPicker.setOnColorPickerChangeListener(this)
        seekBar.setOnSeekBarChangeListener(this)
    }

    override fun clickTextItem(position: Int,stickerBean: StickerBean) {
        val sticker:TextSticker = when(stickerBean.id){
            0 -> {
                TextSticker(this)
            }
            1 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.2f,0.2f,0.2f,0.2f)
                textSticker
            }
            2 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.15f,0.1f,0.15f,0.1f)
                textSticker
            }
            3 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.15f,0.15f,0.15f,0.15f)
                textSticker
            }
            4 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.08f,0.08f,0.08f,0.08f)
                textSticker
            }
            5 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.15f,0.08f,0.15f,0.28f)
                textSticker
            }
            6 -> {
                val textSticker = TextSticker(this)
                val drawable = ContextCompat.getDrawable(this,stickerBean.resId) ?: return
                textSticker.setDrawable(drawable,0.1f,0.08f,0.1f,0.08f)
                textSticker
            }

            else -> TextSticker(this,ContextCompat.getDrawable(this,stickerBean.resId))
        }

//        sticker.drawable = ContextCompat.getDrawable(
//            applicationContext,
//            R.drawable.sticker_transparent_background
//        )!!
        sticker.text = "双击输入文字"
        sticker.setTextColor(Color.BLACK)
        sticker.setTextAlign(Layout.Alignment.ALIGN_CENTER)
        sticker.resizeText()
        stickerView.addSticker(sticker)
    }



    /**
     * 颜色选择器接口
     * */
    override fun onColorChanged(picker: ColorPickerView?, color: Int) {
        mColor = color
        if (selectSticker != null){
            (selectSticker as TextSticker).setTextColor(color)
            stickerView.invalidate()
        }

    }

    override fun onStartTrackingTouch(picker: ColorPickerView?) {

    }

    override fun onStopTrackingTouch(picker: ColorPickerView?) {

    }

    /**
     * stickerView接口
     * */
    override fun onStickerAdded(sticker: Sticker) {
        selectSticker = sticker
    }

    override fun onStickerClicked(sticker: Sticker) {
        selectSticker = sticker
        seekBar.progress = (sticker as TextSticker).alpha
    }

    override fun onStickerDeleted(sticker: Sticker) {
        if (sticker == selectSticker)
            selectSticker = null
    }

    override fun onStickerDragFinished(sticker: Sticker) {

    }

    override fun onStickerTouchedDown(sticker: Sticker) {

    }

    override fun onStickerZoomFinished(sticker: Sticker) {

    }

    override fun onStickerFlipped(sticker: Sticker) {

    }

    override fun onStickerDoubleTapped(sticker: Sticker) {
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

    /**
     * seekbar 接口
     * */
    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
        if (selectSticker == null)
                return
        (selectSticker as TextSticker).alpha = progress
        stickerView.invalidate()
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {

    }

}