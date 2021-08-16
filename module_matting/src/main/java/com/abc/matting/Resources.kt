package com.abc.matting

import android.graphics.Color
import android.graphics.PointF
import com.abc.matting.bean.*
import com.feisukj.base.BaseApplication
import com.feisukj.base.util.PackageUtils
import com.luck.picture.lib.style.PictureSelectorUIStyle
import jp.co.cyberagent.android.gpuimage.filter.*

object Resources {

    const val tencentcloudapi_SecretId = "AKID5AEILG5lKl4Vox32scDblhS1LSL7qRp1"
    const val tencentcloudapi_SecretKey = "sbn089yb0B8A6pMgmfP3Vpy67ze8QUL3"
    const val pic_up_app_key = "465b2ad2c3e34057bb5636e98728418d"

    fun getWhiteStyle(): PictureSelectorUIStyle? {
        val uiStyle = PictureSelectorUIStyle()
        uiStyle.picture_statusBarBackgroundColor = Color.parseColor("#ffffff")
        uiStyle.picture_container_backgroundColor = Color.parseColor("#ffffff")

        uiStyle.picture_statusBarChangeTextColor = true
        uiStyle.picture_switchSelectTotalStyle = false

        uiStyle.picture_navBarColor = Color.parseColor("#393a3e")

        uiStyle.picture_check_style = R.drawable.selector_image_checkbox

        uiStyle.picture_top_leftBack = R.drawable.ic_arrow_back_black_24dp
        uiStyle.picture_top_titleRightTextColor = intArrayOf(
            Color.parseColor("#09C1CE"),
            Color.parseColor("#09C1CE")
        )
        uiStyle.picture_top_titleRightTextSize = 14
        uiStyle.picture_top_titleTextSize = 18
        uiStyle.picture_top_titleArrowUpDrawable = R.drawable.ic_red_arrow_top
        uiStyle.picture_top_titleArrowDownDrawable = R.drawable.ic_red_arrow_bottom
        uiStyle.picture_top_titleTextColor = Color.parseColor("#141414")
        uiStyle.picture_top_titleBarBackgroundColor = Color.parseColor("#ffffff")

        uiStyle.picture_album_textSize = 16
        uiStyle.picture_album_backgroundStyle = R.drawable.picture_item_select_bg
        uiStyle.picture_album_textColor = Color.parseColor("#4d4d4d")
        uiStyle.picture_album_checkDotStyle = R.drawable.ic_merge_select_y

        uiStyle.picture_bottom_previewTextSize = 14
//        uiStyle.picture_bottom_previewTextColor = intArrayOf(
//            Color.parseColor("#9b9b9b"),
//            Color.parseColor("#09C1CE")
//        )
        uiStyle.picture_bottom_previewTextColor = intArrayOf(
            Color.parseColor("#ffffff"),
            Color.parseColor("#ffffff")
        )

        uiStyle.picture_bottom_completeRedDotTextSize = 12
        uiStyle.picture_bottom_completeTextSize = 14
        uiStyle.picture_bottom_completeRedDotTextColor =
            Color.parseColor("#FFFFFF")
        uiStyle.picture_bottom_completeRedDotBackground = R.drawable.picture_num_oval
        uiStyle.picture_bottom_completeTextColor = intArrayOf(
            Color.parseColor("#9b9b9b"),
            Color.parseColor("#F56491")
        )
        uiStyle.picture_bottom_barBackgroundColor = Color.parseColor("#ffffff")


        uiStyle.picture_adapter_item_camera_backgroundColor =
            Color.parseColor("#999999")
        uiStyle.picture_adapter_item_camera_textColor = Color.parseColor("#FFFFFF")
        uiStyle.picture_adapter_item_camera_textSize = 14
        uiStyle.picture_adapter_item_camera_textTopDrawable = com.luck.picture.lib.R.drawable.picture_icon_camera

        uiStyle.picture_adapter_item_textSize = 12
        uiStyle.picture_adapter_item_textColor = Color.parseColor("#FFFFFF")
        uiStyle.picture_adapter_item_video_textLeftDrawable = com.luck.picture.lib.R.drawable.picture_icon_video
        uiStyle.picture_adapter_item_audio_textLeftDrawable = com.luck.picture.lib.R.drawable.picture_icon_audio

        uiStyle.picture_bottom_originalPictureTextSize = 14
        uiStyle.picture_bottom_originalPictureCheckStyle =
            com.luck.picture.lib.R.drawable.picture_original_wechat_checkbox
        uiStyle.picture_bottom_originalPictureTextColor =
            Color.parseColor("#FFFFFF")
        uiStyle.picture_bottom_previewNormalText = com.luck.picture.lib.R.string.picture_preview
        uiStyle.picture_bottom_originalPictureText = com.luck.picture.lib.R.string.picture_original_image
        uiStyle.picture_bottom_completeDefaultText = com.luck.picture.lib.R.string.picture_please_select
        uiStyle.picture_bottom_completeNormalText = com.luck.picture.lib.R.string.picture_completed
        uiStyle.picture_adapter_item_camera_text = com.luck.picture.lib.R.string.picture_take_picture
        uiStyle.picture_top_titleRightDefaultText = com.luck.picture.lib.R.string.picture_cancel
        uiStyle.picture_top_titleRightNormalText = com.luck.picture.lib.R.string.picture_cancel
        uiStyle.picture_bottom_previewDefaultText = com.luck.picture.lib.R.string.picture_preview

        return uiStyle
    }

    fun getFreeSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(1,R.drawable.ic_free_sticker_1, R.drawable.ic_free_sticker_1, false),
            StickerBean(2,R.drawable.ic_free_sticker_2, R.drawable.ic_free_sticker_2, false),
            StickerBean(3,R.drawable.ic_free_sticker_3, R.drawable.ic_free_sticker_3, false),
            StickerBean(4,R.drawable.ic_free_sticker_4, R.drawable.ic_free_sticker_4, false),
            StickerBean(5,R.drawable.ic_free_sticker_5, R.drawable.ic_free_sticker_5, false),
            StickerBean(6,R.drawable.ic_free_sticker_6, R.drawable.ic_free_sticker_6, false),
            StickerBean(7,R.drawable.ic_free_sticker_7, R.drawable.ic_free_sticker_7, false),
            StickerBean(8,R.drawable.ic_free_sticker_8, R.drawable.ic_free_sticker_8, false),
            StickerBean(9,R.drawable.ic_free_sticker_9, R.drawable.ic_free_sticker_9, false)
        )
    }

    fun getVipSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(1,R.drawable.ic_vip_sticker_1, R.drawable.ic_vip_sticker_1, true),
            StickerBean(2,R.drawable.ic_vip_sticker_2, R.drawable.ic_vip_sticker_2, true),
            StickerBean(3,R.drawable.ic_vip_sticker_3, R.drawable.ic_vip_sticker_3, true),
            StickerBean(4,R.drawable.ic_vip_sticker_4, R.drawable.ic_vip_sticker_4, true),
//            StickerBean(R.drawable.ic_vip_sticker_5, R.drawable.ic_vip_sticker_5, true),
            StickerBean(5,R.drawable.ic_vip_sticker_6, R.drawable.ic_vip_sticker_6, true),
            StickerBean(6,R.drawable.ic_vip_sticker_7, R.drawable.ic_vip_sticker_7, true)
        )
    }

    fun getTextSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(0,R.drawable.ic_text_style_small_0,R.drawable.ic_text_style_small_0,false),
//            StickerBean(1,R.drawable.ic_text_style_small_1,R.drawable.ic_text_style_big_1,false),
            StickerBean(2,R.drawable.ic_text_style_small_2,R.drawable.ic_text_style_big_2,false),
            StickerBean(3,R.drawable.ic_text_style_small_3,R.drawable.ic_text_style_big_3,false),
            StickerBean(4,R.drawable.ic_text_style_small_4,R.drawable.ic_text_style_big_4,false),
            StickerBean(5,R.drawable.ic_text_style_small_5,R.drawable.ic_text_style_big_5,false),
            StickerBean(6,R.drawable.ic_text_style_small_6,R.drawable.ic_text_style_big_6,false)
        )
    }

    fun getCameraFilter():Array<FilterBean>{
        return arrayOf(
            FilterBean("filter/brightness.png", GPUImageBrightnessFilter(0.0f), "原图", false),
            FilterBean("filter/grayscale.png", GPUImageGrayscaleFilter(), "灰度", false),
            FilterBean("filter/colorInvert.png", GPUImageColorInvertFilter(), "颠倒", false),
            FilterBean("filter/zoomBlur.png", GPUImageZoomBlurFilter(), "变焦模糊", false),
            FilterBean("filter/swirl.png", GPUImageSwirlFilter(0.5f, 0.2f, PointF(0.5f, 0.5f)), "扭曲", false),
            FilterBean("filter/sketch.png", GPUImageSketchFilter(), "草图", true),
            FilterBean("filter/toon.png", GPUImageToonFilter(), "Toon", true),
            FilterBean("filter/bulgeDistortion.png", GPUImageBulgeDistortionFilter(), "凸出", true)
        )
    }

    /**
     * 获取VIP价格
     * */
    private const val VIP_13: Double = 78.88
    private const val VIP_1 = 18.88
    private const val VIP_6 = 38.88
    private const val VIP_12 = 48.88

    private const val RATIO = 1.2
    fun getVipPrice():Array<PayBean>{
        return arrayOf(
            PayBean("VIP13",
                "永久卡",
                VIP_13,
                VIP_13 * RATIO,
                true,
                PackageUtils.getAppName(BaseApplication.application) + "永久VIP",
                PackageUtils.getAppName(
                    BaseApplication.application) + "永久VIP"),
            PayBean("VIP1",
                "一个月",
                VIP_1,
                VIP_1 * RATIO,
                false,
                PackageUtils.getAppName(BaseApplication.application) + "一个月VIP",
                PackageUtils.getAppName(
                    BaseApplication.application) + "一个月VIP"),
            PayBean("VIP6",
                "三个月",
                VIP_6,
                VIP_6 * RATIO,
                false,
                PackageUtils.getAppName(BaseApplication.application) + "半年VIP",
                PackageUtils.getAppName(
                    BaseApplication.application) + "半年VIP"),
            PayBean("VIP12",
                "一年",
                VIP_12,
                VIP_12 * RATIO,
                false,
                PackageUtils.getAppName(BaseApplication.application) + "一年VIP",
                PackageUtils.getAppName(
                    BaseApplication.application) + "一年VIP")
        )
    }

    /**
     * 获取VIP特权
     * */
    fun getVipPrivilege():Array<PrivilegeBean>{
        return arrayOf(
            PrivilegeBean(R.drawable.ic_vip_privilege_portrait, "人像抠图", "智能去除人像背景"),
            PrivilegeBean(R.drawable.ic_vip_privilege_smart, "智能抠图", "一键去除任意图片背景"),
            PrivilegeBean(R.drawable.ic_vip_privilege_filter, "滤镜特效", "滤镜美颜效果更佳"),
            PrivilegeBean(R.drawable.ic_vip_privilege_bg, "高清背景", "高清背景全部可用"),
//            PrivilegeBean(R.drawable.ic_vip_privilege_photo_frame, "百变相框", "所有相框全部可用"),
            PrivilegeBean(R.drawable.ic_vip_privilege_adjust, "色调调整", "色调亮度任意调节"),
            PrivilegeBean(R.drawable.ic_vip_privilege_ad, "移除广告", "讨厌的广告去无踪"),
            PrivilegeBean(R.drawable.ic_vip_privilege_stickers, "精美帖纸", "精美帖纸全部可用")
        )
    }

    /**
     * 获取调整列表
     * */
    fun getAdjust():Array<AdjustBean>{
        return arrayOf(
            AdjustBean("亮度",R.drawable.ic_adjust_ld_n,R.drawable.ic_adjust_ld_y),
            AdjustBean("对比度",R.drawable.ic_adjust_dbd_n,R.drawable.ic_adjust_dbd_y),
            AdjustBean("色调",R.drawable.ic_adjust_sd_n,R.drawable.ic_adjust_sd_y),
            AdjustBean("锐度",R.drawable.ic_adjust_rd_n,R.drawable.ic_adjust_rd_y),
            AdjustBean("曝光",R.drawable.ic_adjust_bg_n,R.drawable.ic_adjust_bg_y),
            AdjustBean("白平衡",R.drawable.ic_adjust_bph_n,R.drawable.ic_adjust_bph_y),
//            AdjustBean("阴影",R.drawable.ic_adjust_yy_n,R.drawable.ic_adjust_yy_y),
//            AdjustBean("高光",R.drawable.ic_adjust_gg_n,R.drawable.ic_adjust_gg_y),
            AdjustBean("饱和度",R.drawable.ic_adjust_bhd_n,R.drawable.ic_adjust_bhd_y),
//            AdjustBean("雾化",R.drawable.ic_adjust_wh_n,R.drawable.ic_adjust_wh_y)
        )
    }

    /**
     * 获取背景分组
     * */
    fun getBackgroundGroup(): Array<BackgroundGroupBean>{
        return arrayOf(
            BackgroundGroupBean("相册","beijing/group/album.png",R.drawable.tt_ad_logo_small,false),
            BackgroundGroupBean("自然","beijing/group/natural.png",R.drawable.tt_ad_logo_small,false),
            BackgroundGroupBean("汽车","beijing/group/car.png",R.drawable.tt_ad_logo_small,false),
            BackgroundGroupBean("游戏","beijing/group/game.png",R.drawable.tt_ad_logo_small,false),
            BackgroundGroupBean("美女","beijing/group/beauty.png",R.drawable.tt_ad_logo_small,false),
            BackgroundGroupBean("明星","beijing/group/star.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("体育","beijing/group/sport.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("动物萌宠","beijing/group/anima.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("影视","beijing/group/mov.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("简约抽象","beijing/group/contracted.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("动漫","beijing/group/anime.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("文字","beijing/group/text.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("情侣","beijing/group/couples.png",R.drawable.tt_ad_logo_small,true),
            BackgroundGroupBean("意境","beijing/group/artistic.png",R.drawable.tt_ad_logo_small,true)
        )
    }

    /**自然风光*/
    fun getNaturalBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/natural_small/natural_1.jpg","beijing/natural/natural_1.jpg",false),
            BackgroundBean("beijing/natural_small/natural_2.jpg","beijing/natural/natural_2.jpeg",false),
            BackgroundBean("beijing/natural_small/natural_3.jpg","beijing/natural/natural_3.jpg",false),
            BackgroundBean("beijing/natural_small/natural_4.jpg","beijing/natural/natural_4.jpg",false),
            BackgroundBean("beijing/natural_small/natural_5.jpg","beijing/natural/natural_5.jpg",false),
            BackgroundBean("beijing/natural_small/natural_6.jpg","beijing/natural/natural_6.jpeg",false),
            BackgroundBean("beijing/natural_small/natural_7.jpg","beijing/natural/natural_7.jpg",false),
            BackgroundBean("beijing/natural_small/natural_8.jpg","beijing/natural/natural_8.jpg",false),
            BackgroundBean("beijing/natural_small/natural_9.jpg","beijing/natural/natural_9.jfif",false),
            BackgroundBean("beijing/natural_small/natural_10.jpg","beijing/natural/natural_10.jpeg",false),
            BackgroundBean("beijing/natural_small/natural_11.jpg","beijing/natural/natural_11.jpeg",false),
            BackgroundBean("beijing/natural_small/natural_12.jpg","beijing/natural/natural_12.jpg",false),
            BackgroundBean("beijing/natural_small/natural_13.jpg","beijing/natural/natural_13.jpg",false),
            BackgroundBean("beijing/natural_small/natural_14.jpg","beijing/natural/natural_14.jpeg",false),
            BackgroundBean("beijing/natural_small/natural_15.jpg","beijing/natural/natural_15.jpg",false),
            BackgroundBean("beijing/natural_small/natural_16.jpg","beijing/natural/natural_16.jpg",false),
            BackgroundBean("beijing/natural_small/natural_17.jpg","beijing/natural/natural_17.jpg",false),
            BackgroundBean("beijing/natural_small/natural_18.jpg","beijing/natural/natural_18.jpg",false),
            BackgroundBean("beijing/natural_small/natural_19.jpg","beijing/natural/natural_19.jpg",false),
            BackgroundBean("beijing/natural_small/natural_20.jpg","beijing/natural/natural_20.jpg",false)
        )
    }

    /**汽车*/
    fun getCarBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/car_small/car_1.jpg","beijing/car/car_1.jpg",false),
            BackgroundBean("beijing/car_small/car_2.jpg","beijing/car/car_2.jpg",false),
            BackgroundBean("beijing/car_small/car_3.jpg","beijing/car/car_3.jpg",false),
            BackgroundBean("beijing/car_small/car_4.jpg","beijing/car/car_4.jpg",false),
            BackgroundBean("beijing/car_small/car_5.jpg","beijing/car/car_5.jpg",false),
            BackgroundBean("beijing/car_small/car_6.jpg","beijing/car/car_6.jpg",false),
            BackgroundBean("beijing/car_small/car_7.jpg","beijing/car/car_7.jpg",false),
            BackgroundBean("beijing/car_small/car_8.jpg","beijing/car/car_8.jpg",false),
            BackgroundBean("beijing/car_small/car_9.jpg","beijing/car/car_9.jpg",false),
            BackgroundBean("beijing/car_small/car_10.jpg","beijing/car/car_10.jpg",false),
            BackgroundBean("beijing/car_small/car_11.jpg","beijing/car/car_11.jpg",false),
            BackgroundBean("beijing/car_small/car_12.jpg","beijing/car/car_12.jpg",false),
            BackgroundBean("beijing/car_small/car_13.jpg","beijing/car/car_13.jpg",false),
            BackgroundBean("beijing/car_small/car_14.jpg","beijing/car/car_14.jpg",false),
            BackgroundBean("beijing/car_small/car_15.jpg","beijing/car/car_15.jpg",false),
            BackgroundBean("beijing/car_small/car_16.jpg","beijing/car/car_16.jpg",false),
            BackgroundBean("beijing/car_small/car_17.jpg","beijing/car/car_17.jpg",false),
            BackgroundBean("beijing/car_small/car_18.jpg","beijing/car/car_18.jpg",false),
            BackgroundBean("beijing/car_small/car_19.jpg","beijing/car/car_19.jpg",false),
            BackgroundBean("beijing/car_small/car_20.jpg","beijing/car/car_20.jpg",false),
            BackgroundBean("beijing/car_small/car_21.jpg","beijing/car/car_21.jfif",false),
            BackgroundBean("beijing/car_small/car_22.jpg","beijing/car/car_22.jpg",false),
            BackgroundBean("beijing/car_small/car_23.jpg","beijing/car/car_23.jpg",false),
            BackgroundBean("beijing/car_small/car_24.jpg","beijing/car/car_24.jpg",false),
            BackgroundBean("beijing/car_small/car_25.jpg","beijing/car/car_25.jpg",false)
        )
    }

    /**游戏*/
    fun getGameBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/game_small/game_1.jpg","beijing/game/game_1.jpg",false),
            BackgroundBean("beijing/game_small/game_2.jpg","beijing/game/game_2.jpeg",false),
            BackgroundBean("beijing/game_small/game_3.jpg","beijing/game/game_3.jpeg",false),
            BackgroundBean("beijing/game_small/game_4.jpg","beijing/game/game_4.jpeg",false),
            BackgroundBean("beijing/game_small/game_5.jpg","beijing/game/game_5.jpeg",false),
            BackgroundBean("beijing/game_small/game_6.jpg","beijing/game/game_6.jpg",false),
            BackgroundBean("beijing/game_small/game_7.jpg","beijing/game/game_7.jpeg",false),
            BackgroundBean("beijing/game_small/game_8.jpg","beijing/game/game_8.jpeg",false),
            BackgroundBean("beijing/game_small/game_9.jpg","beijing/game/game_9.jpeg",false),
            BackgroundBean("beijing/game_small/game_10.jpg","beijing/game/game_10.jpeg",false),
            BackgroundBean("beijing/game_small/game_11.jpg","beijing/game/game_11.jpg",false)
        )
    }

    /**美女*/
    fun getBeautyBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/beauty_small/beauty_1.jpg","beijing/beauty/beauty_1.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_2.jpg","beijing/beauty/beauty_2.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_3.jpg","beijing/beauty/beauty_3.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_4.jpg","beijing/beauty/beauty_4.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_5.jpg","beijing/beauty/beauty_5.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_6.jpg","beijing/beauty/beauty_6.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_7.jpg","beijing/beauty/beauty_7.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_8.jpg","beijing/beauty/beauty_8.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_9.jpg","beijing/beauty/beauty_9.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_10.jpg","beijing/beauty/beauty_10.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_11.jpg","beijing/beauty/beauty_11.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_12.jpg","beijing/beauty/beauty_12.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_13.jpg","beijing/beauty/beauty_13.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_14.jpg","beijing/beauty/beauty_14.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_15.jpg","beijing/beauty/beauty_15.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_16.jpg","beijing/beauty/beauty_16.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_17.jpg","beijing/beauty/beauty_17.jpg",false),
            BackgroundBean("beijing/beauty_small/beauty_18.jpg","beijing/beauty/beauty_18.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_19.jpg","beijing/beauty/beauty_19.jpeg",false),
            BackgroundBean("beijing/beauty_small/beauty_20.jpg","beijing/beauty/beauty_20.jpeg",false)
        )
    }

    /**明星*/
    fun getStarBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/star_small/star_1.jpg","beijing/star/star_1.jpg",true),
            BackgroundBean("beijing/star_small/star_2.png","beijing/star/star_2.png",true),
            BackgroundBean("beijing/star_small/star_3.jpg","beijing/star/star_3.jpg",true),
            BackgroundBean("beijing/star_small/star_4.jpg","beijing/star/star_4.jpg",true),
            BackgroundBean("beijing/star_small/star_5.jpg","beijing/star/star_5.jpg",true),
            BackgroundBean("beijing/star_small/star_6.jpg","beijing/star/star_6.jpeg",true),
            BackgroundBean("beijing/star_small/star_7.jpg","beijing/star/star_7.jpeg",true),
            BackgroundBean("beijing/star_small/star_8.jpg","beijing/star/star_8.jpg",true),
            BackgroundBean("beijing/star_small/star_9.jpg","beijing/star/star_9.jpg",true),
            BackgroundBean("beijing/star_small/star_10.png","beijing/star/star_10.png",true),
            BackgroundBean("beijing/star_small/star_11.jpg","beijing/star/star_11.jpg",true),
            BackgroundBean("beijing/star_small/star_12.jpg","beijing/star/star_12.jpeg",true),
            BackgroundBean("beijing/star_small/star_13.jpg","beijing/star/star_13.jpg",true),
            BackgroundBean("beijing/star_small/star_14.jpg","beijing/star/star_14.jpg",true),
            BackgroundBean("beijing/star_small/star_15.jpg","beijing/star/star_15.jpg",true),
            BackgroundBean("beijing/star_small/star_16.jpg","beijing/star/star_16.jpg",true),
            BackgroundBean("beijing/star_small/star_17.jpg","beijing/star/star_17.jpeg",true),
            BackgroundBean("beijing/star_small/star_18.jpg","beijing/star/star_18.jpeg",true),
            BackgroundBean("beijing/star_small/star_19.jpg","beijing/star/star_19.jpg",true),
            BackgroundBean("beijing/star_small/star_20.png","beijing/star/star_20.jpg",true),
            BackgroundBean("beijing/star_small/star_21.jpg","beijing/star/star_21.jpg",true),
            BackgroundBean("beijing/star_small/star_22.jpg","beijing/star/star_22.jpg",true),
            BackgroundBean("beijing/star_small/star_23.jpg","beijing/star/star_23.jpeg",true),
            BackgroundBean("beijing/star_small/star_24.jpg","beijing/star/star_24.jpg",true),
            BackgroundBean("beijing/star_small/star_25.jpg","beijing/star/star_25.jpg",true)
        )
    }

    /**体育运动*/
    fun getSportsBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/sports_small/sports_1.jpg", "beijing/sports/sports_1.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_2.jpg", "beijing/sports/sports_2.jpg", true),
            BackgroundBean("beijing/sports_small/sports_3.jpg", "beijing/sports/sports_3.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_4.jpg", "beijing/sports/sports_4.jpg", true),
            BackgroundBean("beijing/sports_small/sports_5.jpg", "beijing/sports/sports_5.jpg", true),
            BackgroundBean("beijing/sports_small/sports_6.jpg", "beijing/sports/sports_6.jpg", true),
            BackgroundBean("beijing/sports_small/sports_7.jpg", "beijing/sports/sports_7.jpg", true),
            BackgroundBean("beijing/sports_small/sports_8.jpg", "beijing/sports/sports_8.jpg", true),
            BackgroundBean("beijing/sports_small/sports_9.jpg", "beijing/sports/sports_9.jpg", true),
            BackgroundBean("beijing/sports_small/sports_10.jpg", "beijing/sports/sports_10.jpg", true),
            BackgroundBean("beijing/sports_small/sports_11.jpg", "beijing/sports/sports_11.jpg", true),
            BackgroundBean("beijing/sports_small/sports_12.jpg", "beijing/sports/sports_12.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_13.jpg", "beijing/sports/sports_13.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_14.jpg", "beijing/sports/sports_14.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_15.jpg", "beijing/sports/sports_15.jpg", true),
            BackgroundBean("beijing/sports_small/sports_16.jpg", "beijing/sports/sports_16.jpg", true),
            BackgroundBean("beijing/sports_small/sports_17.jpg", "beijing/sports/sports_17.jpg", true),
            BackgroundBean("beijing/sports_small/sports_18.jpg", "beijing/sports/sports_18.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_19.jpg", "beijing/sports/sports_19.jpeg", true),
            BackgroundBean("beijing/sports_small/sports_20.jpg", "beijing/sports/sports_20.jpg", true),
            BackgroundBean("beijing/sports_small/sports_21.jpg", "beijing/sports/sports_21.jpg", true),
            BackgroundBean("beijing/sports_small/sports_22.jpg", "beijing/sports/sports_22.jpg", true),
            BackgroundBean("beijing/sports_small/sports_23.jpg", "beijing/sports/sports_23.jpg", true),
            BackgroundBean("beijing/sports_small/sports_24.jpg", "beijing/sports/sports_24.jpg", true),
            BackgroundBean("beijing/sports_small/sports_25.jpg", "beijing/sports/sports_25.jpg", true)
        )
    }

    /**动物萌宠*/
    fun getAnimalBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/animal_small/animal_1.jpg", "beijing/animal/animal_1.jpg", true),
            BackgroundBean("beijing/animal_small/animal_2.jpg", "beijing/animal/animal_2.jpg", true),
            BackgroundBean("beijing/animal_small/animal_3.jpg", "beijing/animal/animal_3.jpg", true),
            BackgroundBean("beijing/animal_small/animal_4.jpg", "beijing/animal/animal_4.jpg", true),
            BackgroundBean("beijing/animal_small/animal_5.jpg", "beijing/animal/animal_5.jpg", true),
            BackgroundBean("beijing/animal_small/animal_6.jpg", "beijing/animal/animal_6.jpg", true),
            BackgroundBean("beijing/animal_small/animal_7.jpg", "beijing/animal/animal_7.jpg", true),
            BackgroundBean("beijing/animal_small/animal_8.jpg", "beijing/animal/animal_8.jpg", true),
            BackgroundBean("beijing/animal_small/animal_9.jpg", "beijing/animal/animal_9.jpg", true),
            BackgroundBean("beijing/animal_small/animal_10.jpg", "beijing/animal/animal_10.jpg", true),
            BackgroundBean("beijing/animal_small/animal_11.jpg", "beijing/animal/animal_11.jpg", true),
            BackgroundBean("beijing/animal_small/animal_12.jpg", "beijing/animal/animal_12.jpg", true),
            BackgroundBean("beijing/animal_small/animal_13.jpg", "beijing/animal/animal_13.jpg", true),
            BackgroundBean("beijing/animal_small/animal_14.jpg", "beijing/animal/animal_14.jpg", true),
            BackgroundBean("beijing/animal_small/animal_15.jpg", "beijing/animal/animal_15.jpg", true),
            BackgroundBean("beijing/animal_small/animal_16.jpg", "beijing/animal/animal_16.jpg", true),
            BackgroundBean("beijing/animal_small/animal_17.jpg", "beijing/animal/animal_17.jpg", true),
            BackgroundBean("beijing/animal_small/animal_18.jpg", "beijing/animal/animal_18.jpg", true),
            BackgroundBean("beijing/animal_small/animal_19.jpg", "beijing/animal/animal_19.jpg", true)
        )
    }

    /**影视*/
    fun getMovBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/mov_small/mov_1.jpg", "beijing/mov/mov_1.jpg", true),
            BackgroundBean("beijing/mov_small/mov_2.jpg", "beijing/mov/mov_2.jpg", true),
            BackgroundBean("beijing/mov_small/mov_3.jpg", "beijing/mov/mov_3.jpg", true),
            BackgroundBean("beijing/mov_small/mov_4.jpg", "beijing/mov/mov_4.jpg", true),
            BackgroundBean("beijing/mov_small/mov_5.jpg", "beijing/mov/mov_5.jpg", true),
            BackgroundBean("beijing/mov_small/mov_6.jpg", "beijing/mov/mov_6.jpg", true),
            BackgroundBean("beijing/mov_small/mov_7.jpg", "beijing/mov/mov_7.jpg", true),
            BackgroundBean("beijing/mov_small/mov_8.jpg", "beijing/mov/mov_8.jpg", true),
            BackgroundBean("beijing/mov_small/mov_9.png", "beijing/mov/mov_9.png", true),
            BackgroundBean("beijing/mov_small/mov_10.jpg", "beijing/mov/mov_10.jpg", true),
            BackgroundBean("beijing/mov_small/mov_11.jpg", "beijing/mov/mov_11.jpg", true),
            BackgroundBean("beijing/mov_small/mov_12.jpg", "beijing/mov/mov_12.jpg", true),
            BackgroundBean("beijing/mov_small/mov_13.jpg", "beijing/mov/mov_13.jpg", true),
            BackgroundBean("beijing/mov_small/mov_14.jpg", "beijing/mov/mov_14.jpg", true),
            BackgroundBean("beijing/mov_small/mov_15.jpg", "beijing/mov/mov_15.jpg", true),
            BackgroundBean("beijing/mov_small/mov_16.jpg", "beijing/mov/mov_16.jpg", true),
            BackgroundBean("beijing/mov_small/mov_17.jpg", "beijing/mov/mov_17.jpg", true),
            BackgroundBean("beijing/mov_small/mov_18.png", "beijing/mov/mov_18.png", true),
            BackgroundBean("beijing/mov_small/mov_19.jpg", "beijing/mov/mov_19.jpg", true),
            BackgroundBean("beijing/mov_small/mov_20.jpg", "beijing/mov/mov_20.jpg", true),
            BackgroundBean("beijing/mov_small/mov_21.jpg", "beijing/mov/mov_21.jpg", true),
            BackgroundBean("beijing/mov_small/mov_22.png", "beijing/mov/mov_22.png", true),
            BackgroundBean("beijing/mov_small/mov_23.jpg", "beijing/mov/mov_23.jpg", true)
        )
    }

    /**意境*/
    fun getArtisticBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/artistic_small/artistic_1.jpg", "beijing/artistic/artistic_1.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_2.jpg", "beijing/artistic/artistic_2.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_3.jpg", "beijing/artistic/artistic_3.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_4.jpg", "beijing/artistic/artistic_4.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_5.jpg", "beijing/artistic/artistic_5.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_6.jpg", "beijing/artistic/artistic_6.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_7.jpg", "beijing/artistic/artistic_7.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_8.jpg", "beijing/artistic/artistic_8.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_9.jpg", "beijing/artistic/artistic_9.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_10.jpg", "beijing/artistic/artistic_10.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_11.jpg", "beijing/artistic/artistic_11.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_12.jpg", "beijing/artistic/artistic_12.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_13.jpg", "beijing/artistic/artistic_13.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_14.jpg", "beijing/artistic/artistic_14.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_15.jpg", "beijing/artistic/artistic_15.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_16.jpg", "beijing/artistic/artistic_16.jpg", true),
            BackgroundBean("beijing/artistic_small/artistic_17.jpg", "beijing/artistic/artistic_17.jpg", true)
        )
    }

    /**动漫*/
    fun getAnimeBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/anime_small/anime_1.jpg", "beijing/anime/anime_1.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_2.jpg", "beijing/anime/anime_2.jpg", true),
            BackgroundBean("beijing/anime_small/anime_3.jpg", "beijing/anime/anime_3.jpg", true),
            BackgroundBean("beijing/anime_small/anime_4.jpg", "beijing/anime/anime_4.jpg", true),
            BackgroundBean("beijing/anime_small/anime_5.jpg", "beijing/anime/anime_5.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_6.jpg", "beijing/anime/anime_6.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_7.jpg", "beijing/anime/anime_7.jpg", true),
            BackgroundBean("beijing/anime_small/anime_8.jpg", "beijing/anime/anime_8.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_9.jpg", "beijing/anime/anime_9.jpg", true),
            BackgroundBean("beijing/anime_small/anime_10.jpg", "beijing/anime/anime_10.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_11.jpg", "beijing/anime/anime_11.jpg", true),
            BackgroundBean("beijing/anime_small/anime_12.jpg", "beijing/anime/anime_12.jpg", true),
            BackgroundBean("beijing/anime_small/anime_13.jpg", "beijing/anime/anime_13.jpg", true),
            BackgroundBean("beijing/anime_small/anime_14.jpg", "beijing/anime/anime_14.jpg", true),
            BackgroundBean("beijing/anime_small/anime_15.jpg", "beijing/anime/anime_15.jpg", true),
            BackgroundBean("beijing/anime_small/anime_16.jpg", "beijing/anime/anime_16.jpg", true),
            BackgroundBean("beijing/anime_small/anime_17.jpg", "beijing/anime/anime_17.jpg", true),
            BackgroundBean("beijing/anime_small/anime_18.jpg", "beijing/anime/anime_18.jpg", true),
            BackgroundBean("beijing/anime_small/anime_19.jpg", "beijing/anime/anime_19.jpeg", true),
            BackgroundBean("beijing/anime_small/anime_20.jpg", "beijing/anime/anime_20.jpg", true),
            BackgroundBean("beijing/anime_small/anime_21.jpg", "beijing/anime/anime_21.jpg", true),
            BackgroundBean("beijing/anime_small/anime_22.jpg", "beijing/anime/anime_22.jpg", true),
            BackgroundBean("beijing/anime_small/anime_23.jpg", "beijing/anime/anime_23.jpg", true),
            BackgroundBean("beijing/anime_small/anime_24.jpg", "beijing/anime/anime_24.jpg", true),
            BackgroundBean("beijing/anime_small/anime_25.jpg", "beijing/anime/anime_25.jpg", true),
            BackgroundBean("beijing/anime_small/anime_26.jpg", "beijing/anime/anime_26.jpg", true),
            BackgroundBean("beijing/anime_small/anime_27.jpg", "beijing/anime/anime_27.jpeg", true)
        )
    }

    /**文字*/
    fun getTextBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/text_small/text_1.jpg", "beijing/text/text_1.jpg", true),
            BackgroundBean("beijing/text_small/text_2.jpg", "beijing/text/text_2.jpg", true),
            BackgroundBean("beijing/text_small/text_3.jpg", "beijing/text/text_3.jpg", true),
            BackgroundBean("beijing/text_small/text_4.jpg", "beijing/text/text_4.jpg", true),
            BackgroundBean("beijing/text_small/text_5.jpg", "beijing/text/text_5.jpg", true),
            BackgroundBean("beijing/text_small/text_6.jpg", "beijing/text/text_6.jpeg", true),
            BackgroundBean("beijing/text_small/text_7.jpg", "beijing/text/text_7.jpg", true),
            BackgroundBean("beijing/text_small/text_8.jpg", "beijing/text/text_8.jpeg", true),
            BackgroundBean("beijing/text_small/text_9.jpg", "beijing/text/text_9.jpeg", true),
            BackgroundBean("beijing/text_small/text_10.jpg", "beijing/text/text_10.jpg", true),
            BackgroundBean("beijing/text_small/text_11.jpg", "beijing/text/text_11.jpg", true),
            BackgroundBean("beijing/text_small/text_12.jpg", "beijing/text/text_12.jpg", true),
            BackgroundBean("beijing/text_small/text_13.jpg", "beijing/text/text_13.jpg", true),
            BackgroundBean("beijing/text_small/text_14.jpg", "beijing/text/text_14.jpg", true),
            BackgroundBean("beijing/text_small/text_15.jpg", "beijing/text/text_15.jpg", true),
            BackgroundBean("beijing/text_small/text_16.jpg", "beijing/text/text_16.jpg", true),
            BackgroundBean("beijing/text_small/text_17.jpg", "beijing/text/text_17.jpg", true),
            BackgroundBean("beijing/text_small/text_18.jpg", "beijing/text/text_18.jpg", true),
            BackgroundBean("beijing/text_small/text_19.jpg", "beijing/text/text_19.jpg", true),
            BackgroundBean("beijing/text_small/text_20.jpg", "beijing/text/text_20.jpeg", true),
            BackgroundBean("beijing/text_small/text_21.jpg", "beijing/text/text_21.jpg", true),
            BackgroundBean("beijing/text_small/text_22.jpg", "beijing/text/text_22.jpg", true),
            BackgroundBean("beijing/text_small/text_23.jpg", "beijing/text/text_23.jpeg", true),
            BackgroundBean("beijing/text_small/text_24.jpg", "beijing/text/text_24.jpg", true),
            BackgroundBean("beijing/text_small/text_25.jpg", "beijing/text/text_25.jpeg", true),
            BackgroundBean("beijing/text_small/text_26.jpg", "beijing/text/text_26.jpg", true)
        )
    }

    /**情侣*/
    fun getCouplesBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/couples_small/couples_1.jpg", "beijing/couples/couples_1.jpg", true),
            BackgroundBean("beijing/couples_small/couples_2.jpg", "beijing/couples/couples_2.jpg", true),
            BackgroundBean("beijing/couples_small/couples_3.jpg", "beijing/couples/couples_3.jpg", true),
            BackgroundBean("beijing/couples_small/couples_4.jpg", "beijing/couples/couples_4.jpg", true),
            BackgroundBean("beijing/couples_small/couples_5.jpg", "beijing/couples/couples_5.jpg", true),
            BackgroundBean("beijing/couples_small/couples_6.jpg", "beijing/couples/couples_6.jpg", true),
            BackgroundBean("beijing/couples_small/couples_7.jpg", "beijing/couples/couples_7.jpg", true),
            BackgroundBean("beijing/couples_small/couples_8.jpg", "beijing/couples/couples_8.jpg", true),
            BackgroundBean("beijing/couples_small/couples_9.jpg", "beijing/couples/couples_9.jpg", true),
            BackgroundBean("beijing/couples_small/couples_10.jpg", "beijing/couples/couples_10.jpg", true),
            BackgroundBean("beijing/couples_small/couples_11.jpg", "beijing/couples/couples_11.jpg", true),
            BackgroundBean("beijing/couples_small/couples_12.jpg", "beijing/couples/couples_12.jpg", true),
            BackgroundBean("beijing/couples_small/couples_13.jpg", "beijing/couples/couples_13.jpg", true),
            BackgroundBean("beijing/couples_small/couples_14.jpg", "beijing/couples/couples_14.jpg", true),
            BackgroundBean("beijing/couples_small/couples_15.jpg", "beijing/couples/couples_15.jpg", true),
            BackgroundBean("beijing/couples_small/couples_16.jpg", "beijing/couples/couples_16.jpg", true),
            BackgroundBean("beijing/couples_small/couples_17.jpg", "beijing/couples/couples_17.jpg", true),
            BackgroundBean("beijing/couples_small/couples_18.jpg", "beijing/couples/couples_18.jpg", true),
            BackgroundBean("beijing/couples_small/couples_19.jpg", "beijing/couples/couples_19.jpg", true),
            BackgroundBean("beijing/couples_small/couples_20.jpg", "beijing/couples/couples_20.jpg", true),
            BackgroundBean("beijing/couples_small/couples_21.jpg", "beijing/couples/couples_21.jpg", true)
        )
    }

    /**简约*/
    fun getContractedBackground(): Array<BackgroundBean>{
        return arrayOf(
            BackgroundBean("beijing/contracted_small/contracted_1.jpg", "beijing/contracted/contracted_1.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_2.jpg", "beijing/contracted/contracted_2.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_3.jpg", "beijing/contracted/contracted_3.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_4.jpg", "beijing/contracted/contracted_4.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_5.jpg", "beijing/contracted/contracted_5.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_6.jpg", "beijing/contracted/contracted_6.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_7.jpg", "beijing/contracted/contracted_7.jpg", true),
            BackgroundBean("beijing/contracted_small/contracted_8.jpg", "beijing/contracted/contracted_8.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_9.jpg", "beijing/contracted/contracted_9.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_10.jpg", "beijing/contracted/contracted_10.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_11.jpg", "beijing/contracted/contracted_11.jpg", true),
            BackgroundBean("beijing/contracted_small/contracted_12.jpg", "beijing/contracted/contracted_12.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_13.jpg", "beijing/contracted/contracted_13.jpg", true),
            BackgroundBean("beijing/contracted_small/contracted_14.jpg", "beijing/contracted/contracted_14.jpeg", true),
            BackgroundBean("beijing/contracted_small/contracted_15.jpg", "beijing/contracted/contracted_15.jpeg", true)
        )
    }


    fun getTextColorList(): Array<Int>{
        return arrayOf(
            Color.parseColor("#ff0000"),
            Color.parseColor("#ff4000"),
            Color.parseColor("#ff8000"),
            Color.parseColor("#ffbf00"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#bfff00"),
            Color.parseColor("#80ff00"),
            Color.parseColor("#40ff00"),
            Color.parseColor("#00ff00"),
            Color.parseColor("#00ff40"),
            Color.parseColor("#00ff80"),
            Color.parseColor("#00ffbf"),
            Color.parseColor("#00ffff"),
            Color.parseColor("#00bfff"),
            Color.parseColor("#0080ff"),
            Color.parseColor("#0040ff"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#4000ff"),
            Color.parseColor("#8000ff"),
            Color.parseColor("#bf00ff"),
            Color.parseColor("#ff00ff"),
            Color.parseColor("#ff00bf"),
            Color.parseColor("#ff0080"),
            Color.parseColor("#ff0040")
        )
    }

}