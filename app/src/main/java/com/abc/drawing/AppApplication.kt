package com.abc.drawing

import com.abc.matting.ui.activity.AdjustActivity
import com.abc.matting.ui.activity.HomeActivity
import com.feisukj.ad.SplashActivityAD
import com.feisukj.base.ActivityEntrance
import com.feisukj.base.BaseApplication

class AppApplication:BaseApplication() {
    override fun onCreate() {
        super.onCreate()
        ActivityEntrance.HomeActivity.cls = HomeActivity::class.java
        ActivityEntrance.SplashActivityAD.cls=SplashActivityAD::class.java
        ActivityEntrance.SplashActivity.cls=ActivityEntrance.SplashActivity::class.java

    }
}