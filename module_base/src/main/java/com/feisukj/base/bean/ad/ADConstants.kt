package com.feisukj.base.bean.ad

import com.feisukj.base.util.SPUtil

object ADConstants {
    /************************页面命名 存储广告显示配置 */
    const val START_PAGE = "start_page"
    const val EXIT_PAGE = "exit_page"
    const val MAKEGOLDCOIN_PAGE = "makegoldcoin_page"


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
    val kTouTiaoAppKey by lazy { SPUtil.getInstance().getString("kTouTiaoAppKey","5142936")?:"5142936" }
    val kTouTiaoKaiPing by lazy { SPUtil.getInstance().getString("kTouTiaoKaiPing","887433482")?:"887433482" }
    val kTouTiaoBannerKey by lazy { SPUtil.getInstance().getString("kTouTiaoBannerKey","945819318")?:"945819318" }
    val kTouTiaoChaPingKey by lazy { SPUtil.getInstance().getString("kTouTiaoChaPingKey","945819319")?:"945819319" }
    val kTouTiaoJiLiKey by lazy { SPUtil.getInstance().getString("kTouTiaoJiLiKey","945819326")?:"945819326" }
    val kTouTiaoSeniorKey by lazy { SPUtil.getInstance().getString("kTouTiaoSeniorKey","945819314")?:"945819314" }
    val kTouTiaoSmallSeniorKey by lazy { SPUtil.getInstance().getString("kTouTiaoSmallSeniorKey","945819352")?:"945819352" }

    val kGDTMobSDKAppKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKAppKey","1111492886")?:"1111492886" }
    val kGDTMobSDKChaPingKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKChaPingKey","6031261000694345")?:"6031261000694345" }
    val kGDTMobSDKKaiPingKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKKaiPingKey","5001268070298029")?:"5001268070298029" }
    val kGDTMobSDKBannerKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKBannerKey","6031961000695362")?:"6031961000695362" }
    val kGDTMobSDKNativeKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKNativeKey","6041869060392524")?:"6041869060392524" }
    val kGDTMobSDKJiLiKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKJiLiKey","9001262080395199")?:"9001262080395199" }
    val kGDTMobSDKSmallNativeKey by lazy { SPUtil.getInstance().getString("kGDTMobSDKSmallNativeKey","5071761050698172")?:"5071761050698172" }
}