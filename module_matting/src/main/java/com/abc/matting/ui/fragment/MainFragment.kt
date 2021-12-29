package com.abc.matting.ui.fragment

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.recyclerview.widget.GridLayoutManager
import com.abc.matting.R
import com.abc.matting.Resources.getWhiteStyle
import com.abc.matting.adapter.MainPicAdapter
import com.abc.matting.bean.PictureBean
import com.abc.matting.ui.activity.*
import com.abc.matting.utils.GlideEngine
import com.abc.matting.utils.PermissionUtils
import com.abc.matting.utils.PermissionUtils.askPermission
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseFragment
import com.feisukj.base.util.ToastUtil
import com.hjq.permissions.Permission
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureConfig
import com.luck.picture.lib.config.PictureMimeType
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : BaseFragment(), MainPicAdapter.MainPicCallback {

    companion object{
        const val ADD_TEXT_CODE = 111
        const val SCALE_CODE = 112
        const val STICKERS_CODE = 113
        const val CROP_CODE = 114
        const val FILTER_CODE = 115
        const val ADJUST_CODE = 116
        const val DOODLE_CODE = 117
        const val PORTRAIT_MATTING_CODE = 118
        const val BEAUTY_CODE = 119
    }
    var toEffect:(()->Unit)? = null
    private var adapter: MainPicAdapter = MainPicAdapter()
    private var mList: MutableList<PictureBean> = arrayListOf()
    private lateinit var anim: AnimationDrawable

    override fun getLayoutId(): Int = R.layout.fragment_main

    override fun initView() {
        super.initView()
        anim = iv_anim.background as AnimationDrawable
        adapter.setCallback(this)
        recycler.layoutManager = GridLayoutManager(context,4)
        recycler.adapter = adapter

        if (BaseConstant.channel == "_xiaomi"){
            gone(iv_anim)
            visible(iv_banner)
        }else{
            gone(iv_banner)
            visible(iv_anim)
        }

        initClickEven()
    }

    fun setData(list: MutableList<PictureBean>){
        this.mList = list
        adapter.setData(list)
    }

    private fun initClickEven(){
        iv_anim.setOnClickListener {
            askPermission(mActivity,Permission.CAMERA){
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_COMIC)
                startActivity(intent)
            }
        }

        iv_banner.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                val intent = Intent(mActivity,PortraitActivity::class.java)
                intent.putExtra(PortraitActivity.WORK_TYPE_KEY,PortraitActivity.TYPE_PORTRAIT)
                startActivity(intent)
            }
        }

        //人像抠图
        portrait_matting.setOnClickListener {
//            askStoragePermission(mActivity) {
//                PictureSelector.create(context as Activity)
//                    .openGallery(PictureMimeType.ofImage())
//                    .imageEngine(GlideEngine.createGlideEngine())
//                    .isPreviewImage(true)
////                    .freeStyleCropEnabled(true)
//                    .setPictureUIStyle(getWhiteStyle())
//                    .selectionMode(PictureConfig.SINGLE)
//                    .forResult(PORTRAIT_MATTING_CODE)
//            }
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                val intent = Intent(mActivity,PortraitActivity::class.java)
                intent.putExtra(PortraitActivity.WORK_TYPE_KEY,PortraitActivity.TYPE_PORTRAIT)
                startActivity(intent)
            }
        }

        //人脸特效
        face.setOnClickListener {
            toEffect?.invoke()
        }

        //智能抠图
        view1.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                val intent = Intent(mActivity,PortraitActivity::class.java)
                intent.putExtra(PortraitActivity.WORK_TYPE_KEY,PortraitActivity.TYPE_INTELLIGENT)
                startActivity(intent)
            }
        }

        //手动抠图
        view2.setOnClickListener {
            ToastUtil.showCenterToast("施工中...")
        }

        //美化图片
        view3.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(BEAUTY_CODE)
            }
        }

        //裁剪
        tailoring.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(CROP_CODE)
            }
//            startActivity(Intent(context,CropActivity::class.java))
        }

        //滤镜
        filter.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(FILTER_CODE)
            }
        }
        //调整
        adjust.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(ADJUST_CODE)
            }
        }

        //添加文字
        addText.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(ADD_TEXT_CODE)
            }

        }

        //放大缩小
        scale.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(SCALE_CODE)
            }
        }

        //贴纸
        stickers.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(STICKERS_CODE)
            }
        }

        //涂鸦
        doodle.setOnClickListener {
            askPermission(mActivity,Permission.MANAGE_EXTERNAL_STORAGE){
                PictureSelector.create(context as Activity)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .isCamera(false)
//                    .freeStyleCropEnabled(true)
                    .setPictureUIStyle(getWhiteStyle())
                    .selectionMode(PictureConfig.SINGLE)
                    .forResult(DOODLE_CODE)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (adapter.itemCount == 0)
            visible(noData)
        else
            gone(noData)
    }

    fun startAnim(){
        anim.stop()
        anim.start()
    }

    override fun clickPicItem(position: Int) {
//        val intent = Intent(mActivity,DetailsActivity::class.java)
//        intent.putExtra(DetailsActivity.PATH_KEY,mList[position].filePath)
//        startActivity(intent)
        startActivity(Intent(context, MoreActivity::class.java))
    }

    override fun clickMore() {
        startActivity(Intent(context, MoreActivity::class.java))
    }

}