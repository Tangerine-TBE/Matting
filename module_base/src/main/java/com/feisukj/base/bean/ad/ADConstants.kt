package com.feisukj.base.bean.ad

import com.feisukj.base.util.SPUtil

object ADConstants {
    /************************页面命名 存储广告显示配置 */
    const val START_PAGE = "start_page"
    const val EXIT_PAGE = "exit_page"
    const val BACKGROUND_PAGE = "background_page"


    /************************页面命名*************************/

    /************************页面命名 */
    /************************开屏广告 */
    const val AD_APP_LOAD_TIME = "ad_app_load_time" //App启动时间

    const val AD_APP_BACKGROUND_TIME = "ad_app_background_time" //App退到后台时间

    const val AD_SPREAD_PERIOD = "ad_spread_period" //开屏后台设置的时间间隔

    const val AD_SPLASH_STATUS = "ad_splash_status" //开屏开关

    /************************开屏广告*************************/

    /************************开屏广告 */
    /************************插屏广告 */
    const val AD_INSERT_SHOW_PERIOD = "ad_insert_change_period" //插屏广告显示间隔

    const val AD_INSERT_LAST_SHOW = "ad_insert_last_origin" //插屏广告上展示时间

    /************************插屏广告*************************/
    /************************插屏广告 */
    /**
     * 是否开启了页面banner定时器
     */
    const val AD_BANNER_IS_TIMER = "ad_banner_is_timer"

    const val AD_BANNER_LAST_CHANGE = "AD_BANNER_LAST_CHANGE"

    /************************原生广告 */
    const val AD_NATIVE_SHOW_PERIOD = "ad_native_change_period" //原生广告显示间隔

    const val AD_NATIVE_LAST_SHOW = "ad_native_last_origin" //原生广告上ci展示时间

    /************************原生广告 */
    val kTouTiaoAppKey by lazy { SPUtil.getInstance().getString("kTouTiaoAppKey","5207232")?:"5207232" }
    val kTouTiaoKaiPing by lazy { SPUtil.getInstance().getString("kTouTiaoKaiPing","887541567")?:"887541567" }
    val kTouTiaoBannerKey by lazy { SPUtil.getInstance().getString("kTouTiaoBannerKey","946558273")?:"946558273" }
    val kTouTiaoChaPingKey by lazy { SPUtil.getInstance().getString("kTouTiaoChaPingKey","946558274")?:"946558274" }
    val kTouTiaoJiLiKey by lazy { SPUtil.getInstance().getString("kTouTiaoJiLiKey","946558275")?:"946558275" }
    val kTouTiaoSeniorKey by lazy { SPUtil.getInstance().getString("kTouTiaoSeniorKey","946558272")?:"946558272" }
    val ktouTiaoFullscreenvideoKey by lazy { SPUtil.getInstance().getString("ktouTiaoFullscreenvideoKey","946558276")?:"946558276" }

    val kGDTMobSDKAppKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKAppKey","1200028760")?:"1200028760" }
    val kGDTMobSDKChaPingKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKChaPingKey","7042622198217197")?:"7042622198217197" }
    val kGDTMobSDKKaiPingKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKKaiPingKey","3002822148516113")?:"3002822148516113" }
    val kGDTMobSDKBannerKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKBannerKey","2062728188517126")?:"2062728188517126" }
    val kGDTMobSDKNativeKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKNativeKey","6072925118019134")?:"6072925118019134" }
    val kGDTMobSDKJiLiKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKJiLiKey","9082025128710195")?:"9082025128710195" }
    val kGDTMobSDKNativeSmallKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKNativeSmallKey","5071761050698172")?:"5071761050698172" }
}