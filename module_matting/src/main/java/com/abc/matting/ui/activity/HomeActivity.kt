package com.abc.matting.ui.activity

import android.content.Intent
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.viewpager.widget.ViewPager
import com.abc.matting.Constants
import com.abc.matting.R
import com.abc.matting.bean.PictureBean
import com.abc.matting.ui.dialog.MainVipDialog
import com.abc.matting.ui.fragment.EffectFragment
import com.abc.matting.ui.fragment.MainFragment
import com.abc.matting.ui.fragment.SettingFragment
import com.abc.matting.utils.LoginUtils
import com.abc.matting.utils.Utils.getReal
import com.feisukj.ad.BackDialog
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.baseclass.BaseFragment
import com.feisukj.base.util.DateUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.SPUtil
import com.feisukj.base.util.ToastUtil
import com.luck.picture.lib.PictureSelector
import com.luck.picture.lib.config.PictureMimeType
import com.luck.picture.lib.tools.PictureFileUtils
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import java.util.*

class HomeActivity : BaseActivity() {
    companion object{
        const val GO = 200
        const val BACK = 201
    }

    val mainFragment : MainFragment by lazy {
        MainFragment()
    }
    val effectFragment : EffectFragment by lazy {
        EffectFragment()
    }
    val settingFragment : SettingFragment by lazy {
        SettingFragment()
    }
    val fragments : MutableList<BaseFragment> = arrayListOf()//arrayOf(mainFragment,effectFragment,settingFragment)
    val titles: MutableList<String> = arrayListOf()//arrayOf("首页","人脸特效","个人")
    val icons: MutableList<Int> = arrayListOf()//arrayOf(R.drawable.selector_tab_main,R.drawable.selector_tab_effect,R.drawable.selector_tab_setting)
    private val mList: MutableList<PictureBean> = arrayListOf()

    override fun getLayoutId(): Int = R.layout.activity_home

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(true).init()

        if (BaseConstant.channel == "_xiaomi"){
            fragments.addAll(listOf(mainFragment,settingFragment))
            titles.addAll(listOf("首页","个人"))
            icons.addAll(listOf(R.drawable.selector_tab_main,R.drawable.selector_tab_setting))
        }else{
            fragments.addAll(listOf(mainFragment,effectFragment,settingFragment))
            titles.addAll(listOf("首页","人脸特效","个人"))
            icons.addAll(listOf(R.drawable.selector_tab_main,R.drawable.selector_tab_effect,R.drawable.selector_tab_setting))
        }

        //进入软件更新用户信息
        if (SPUtil.getInstance().getBoolean(Constants.IS_LOGIN,false)){
            if(SPUtil.getInstance().getBoolean(Constants.IS_LOGIN_THIRD,false)){
                LogUtils.e("首页第三方登录")
                LoginUtils.otherLogin()
            }else{
                LogUtils.e("首页手机登录")
                LoginUtils.loginByMobile()
            }
        }

        //判断是否需要重置倒计时开始时间
        if (SPUtil.getInstance().getLong(
                Constants.DOWN_COUNT_START_TIME,
                0L
            ) <= (System.currentTimeMillis() - Constants.DOWN_COUNT)
        ){
            SPUtil.getInstance().putLong(Constants.DOWN_COUNT_START_TIME, System.currentTimeMillis())
            MainVipDialog(this){
                startActivity(Intent(this,PayActivity::class.java))
            }.show()
        }

        view_pager.adapter =object : FragmentStatePagerAdapter(supportFragmentManager){
            override fun getCount(): Int {
                return fragments.size
            }

            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

        }
        tabLayout.setupWithViewPager(view_pager)
        for (i in 0 until tabLayout.tabCount){
            val tab = tabLayout.getTabAt(i)
            val view = LayoutInflater.from(this).inflate(R.layout.item_main_tab,tabLayout,false)
            view.findViewById<ImageView>(R.id.image).setImageResource(icons[i])
            view.findViewById<TextView>(R.id.text).text = titles[i]
            tab?.customView = view
        }

        mainFragment.toEffect = {
            if (fragments.indexOf(effectFragment) != -1){
                view_pager.currentItem = fragments.indexOf(effectFragment)
            }
        }

        view_pager.offscreenPageLimit = fragments.size

        view_pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int,
            ) {

            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    fragments.indexOf(settingFragment) -> {
                        mImmersionBar.statusBarColor(R.color.setting_top).init()
                    }
                    fragments.indexOf(mainFragment) -> {
                        mImmersionBar.statusBarColor(R.color.theme_bg).init()
                        mainFragment.startAnim()
                    }
                    fragments.indexOf(effectFragment) -> {
                        mImmersionBar.statusBarColor(R.color.theme_bg).init()
                        effectFragment.startAnim()
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

        })

//        if ((System.currentTimeMillis() - SPUtil.getInstance().getLong(Constants.SEE_VIDEO_TIME,0L))>1000*60*60*24){
//            SPUtil.getInstance()
//                .putBoolean("natural",false)
//                .putBoolean("car",false)
//                .putBoolean("game",false)
//                .putBoolean("beauty",false)
//                .putBoolean("star",false)
//                .putBoolean("sports",false)
//                .putBoolean("animal",false)
//                .putBoolean("mov",false)
//                .putBoolean("artistic",false)
//                .putBoolean("anime",false)
//                .putBoolean("text",false)
//                .putBoolean("couples",false)
//                .putBoolean("contracted",false)
//        }
    }

    private fun getData(){
        mList.clear()
        val filePath = BaseConstant.savePath
        val files = File(filePath)
        if (!files.exists())
            files.mkdirs()
        val fileList = files.listFiles()
        fileList?.forEach {
            if (checkIsImageFile(it.name)){
                mList.add(PictureBean(it.name,it.path,it.lastModified(),
                    DateUtils.getFormatDate(it.lastModified(),"yyyy-MM-dd")))
            }
        }
        mList.sortByDescending {
            it.lastTime
        }
    }

    /**
     * 判断是否是图片
     * */
    private fun checkIsImageFile(fileName: String): Boolean{
        try {
            val str = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(Locale.ROOT)
            return str == ".jpg"||str == ".jpeg"||str == ".png"
        }catch (e : Exception){
            e.toString()
        }
        return false
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            MainFragment.CROP_CODE -> {
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,CropActivity::class.java)
                intent.putExtra(CropActivity.IMG_PATH,path)
                startActivityForResult(intent, GO)
            }
            MainFragment.ADD_TEXT_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,AddTextActivity::class.java)
                intent.putExtra(AddTextActivity.IMG_URI_KEY,path)
                startActivityForResult(intent, GO)
            }
            MainFragment.SCALE_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,ScaleActivity::class.java)
                intent.putExtra(ScaleActivity.IMG_URI_KEY,path)
                startActivityForResult(intent, GO)
            }
            MainFragment.STICKERS_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,StickerActivity::class.java)
                intent.putExtra(StickerActivity.IMG_URI_KEY,path)
                startActivityForResult(intent, GO)
            }
            MainFragment.FILTER_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,FilterActivity::class.java)
                intent.putExtra(FilterActivity.IMG_PATH,path)
                startActivity(intent)
            }
            MainFragment.ADJUST_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,AdjustActivity::class.java)
                intent.putExtra(AdjustActivity.IMG_PATH,path)
                startActivity(intent)
            }
            MainFragment.DOODLE_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,DoodleActivity::class.java)
                intent.putExtra(DoodleActivity.IMG_PATH,path)
                startActivityForResult(intent,GO)
            }
            MainFragment.BEAUTY_CODE ->{
                val path = getReal(PictureSelector.obtainMultipleResult(data))
                if (path == "")
                    return
                if (File(path).length()>Constants.MAX_PIC_FILE){
                    ToastUtil.showToast("图片太大了，选择小一点的图片吧")
                    return
                }
                val intent = Intent(this,BeautyActivity::class.java)
                intent.putExtra(BeautyActivity.IMG_PATH_KEY,path)
                startActivity(intent)
            }
            GO ->{
                if (resultCode == BACK){
                    getData()
                    mainFragment.setData(mList)
                    val intent = Intent(this,DetailsActivity::class.java)
                    intent.putExtra(DetailsActivity.PATH_KEY,mList[0].filePath)
                    startActivity(intent)
                }
            }
        }

    }

    override fun onResume() {
        super.onResume()
        PictureFileUtils.deleteCacheDirFile(this, PictureMimeType.ofImage())
        getData()
        mainFragment.setData(mList)
    }

    override fun onBackPressed() {
        BackDialog(this).show()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}