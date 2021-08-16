package com.feisukj.ad

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.feisukj.ad.manager.AdController
import com.feisukj.ad.manager.TTAdManagerHolder
import com.feisukj.base.*
import com.feisukj.base.api.AdService
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.bean.ad.AdsConfig
import com.feisukj.base.permission.DefaultRationale
import com.feisukj.base.retrofitnet.HttpUtils
import com.feisukj.base.util.PackageUtils
import com.feisukj.base.util.SPUtil
import com.google.gson.Gson
import com.gyf.immersionbar.ImmersionBar
import com.qq.e.comm.managers.GDTADManager
import com.umeng.commonsdk.UMConfigure
import com.yanzhenjie.permission.AndPermission
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
class SplashActivity : AppCompatActivity() {

    private var builder: AdController? = null
    private var canJump = false
    private var channel: String? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        ImmersionBar.with(this).statusBarColor(android.R.color.transparent).statusBarDarkFont(false).init()
        compositeDisposable = CompositeDisposable()
        BaseApplication.isFromStart = true
        channel = PackageUtils.getAppMetaData(this, "CHANNEL")
        if (!BaseConstant.isAgreement()){
            var dialog = DisplayLaunchTipDialog()
            supportFragmentManager.beginTransaction().add(dialog,null).commitAllowingStateLoss()
            dialog.setDisplayLaunchTipListener(object :
                    DisplayLaunchTipDialog.DisplayLaunchTipListener {
                override fun yes() {
                    SPUtil.getInstance().putBoolean(AgreementActivity.key, true)  //是否展示用户服务协议和隐私政策弹窗
                            .putLong("time", System.currentTimeMillis())
                    //友盟初始化配置
                    if (!BuildConfig.DEBUG) {
                        UMConfigure.init(BaseApplication.application, "60fe3217328eac0d2eb6538c", BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
                    }

                    GDTADManager.getInstance().initWith(BaseApplication.application, ADConstants.kGDTMobSDKAppKey)

                    TTAdManagerHolder.init(BaseApplication.application)
                    dialog.dismiss()
                    getAdConfig()
                    askPermissions()
                }

                override fun no() {
                    dialog.dismiss()
                    finish()
                }
            })
        }else{
            getAdConfig()

            //友盟初始化配置
            if (!BuildConfig.DEBUG){
                UMConfigure.init(BaseApplication.application, "60fe3217328eac0d2eb6538c", BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
            }

            GDTADManager.getInstance().initWith(BaseApplication.application, ADConstants.kGDTMobSDKAppKey)

            TTAdManagerHolder.init(BaseApplication.application)
            askPermissions()
        }
    }

    private fun getAdConfig(){
        HttpUtils.setServiceForFeisuConfig(AdService::class.java).getADConfig(channel = channel!!,version = BaseConstant.versionName)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : Observer<AdsConfig> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.addAll(d)
                    }

                    override fun onNext(t: AdsConfig) {
                        val gson = Gson()
                        SPUtil.getInstance().putString(ADConstants.START_PAGE, gson.toJson(t.data?.start_page))
                        SPUtil.getInstance().putString(ADConstants.MAKEGOLDCOIN_PAGE, gson.toJson(t.data?.makegoldcoin_page))
                        SPUtil.getInstance().putString(ADConstants.EXIT_PAGE, gson.toJson(t.data?.exit_page))

                        t.data?.Advertisement?.also {
                            SPUtil.getInstance().putString("kTouTiaoAppKey",it.kTouTiaoAppKey)
                                    .putString("kTouTiaoKaiPing",it.kTouTiaoKaiPing)
                                    .putString("kTouTiaoBannerKey",it.kTouTiaoBannerKey)
                                    .putString("kTouTiaoChaPingKey",it.kTouTiaoChaPingKey)
                                    .putString("kTouTiaoSeniorKey",it.kTouTiaoSeniorKey)
                                    .putString("kTouTiaoJiLiKey",it.kTouTiaoJiLiKey)
                                    .putString("kTouTiaoSmallSeniorKey",it.kTouTiaoSmallSeniorKey)

                                    .putString("kGDTMobSDKAppKey",it.kGDTMobSDKAppKey)
                                    .putString("kGDTMobSDKChaPingKey",it.kGDTMobSDKChaPingKey)
                                    .putString("kGDTMobSDKKaiPingKey",it.kGDTMobSDKKaiPingKey)
                                    .putString("kGDTMobSDKBannerKey",it.kGDTMobSDKBannerKey)
                                    .putString("kGDTMobSDKNativeKey",it.kGDTMobSDKNativeKey)
                                    .putString("kGDTMobSDKJiLiKey",it.kGDTMobSDKJiLiKey)
                                    .putString("kGDTMobSDKSmallNativeKey",it.kGDTMobSDKSmallNativeKey)
                        }
                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    private fun askPermissions() {
//        val userPermission=if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.Q){
//            listOf(
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//        }else{
//            listOf(
//                    Manifest.permission.READ_PHONE_STATE,
//                    Manifest.permission.READ_EXTERNAL_STORAGE,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//        }
//        AndPermission.with(this)
//            .runtime()
//                .permission(userPermission.toTypedArray())
//                .onGranted {
//                    builder = AdController.Builder(this,ADConstants.START_PAGE)
//                            .setContainer(splash_container)
//                            .create()
//                    builder?.show()
//                }
//                .onDenied {
//                    builder = AdController.Builder(this,ADConstants.START_PAGE)
//                            .setContainer(splash_container)
//                            .create()
//                    builder?.show()
////                    checkIn()
//                }
//                .rationale(DefaultRationale())
//                .start()
        builder = AdController.Builder(this,ADConstants.START_PAGE)
            .setContainer(splash_container)
            .create()
        builder?.show()
    }

    override fun onPause() {
        super.onPause()
        canJump = false
    }


    override fun onResume() {
        super.onResume()
        if (canJump) {
            next()
        }
        canJump = true
    }

    fun checkIn() {
//        if (BaseConstant.isAgreement()) {
            startActivity(Intent(this,ActivityEntrance.HomeActivity.cls))
            BaseApplication.isFromStart = false
            finish()
//        }else{
//            startActivity(Intent(this, AgreementActivity::class.java))
//            finish()
//        }
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private operator fun next() {
        if (canJump) {
            checkIn()
        } else {
            canJump = true
        }
    }

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费  */

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        builder?.destroy()
        compositeDisposable.dispose()
    }
}
