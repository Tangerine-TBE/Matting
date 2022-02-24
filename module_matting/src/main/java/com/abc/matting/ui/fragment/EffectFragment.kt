package com.abc.matting.ui.fragment

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import com.abc.matting.R
import com.abc.matting.ui.activity.*
import com.abc.matting.utils.PermissionUtils
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseFragment
import com.hjq.permissions.Permission
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_effect.*

class EffectFragment : BaseFragment() {

    private var anim: AnimationDrawable? = null

    override fun getLayoutId(): Int = R.layout.fragment_effect

    override fun initView() {
        super.initView()

        anim = iv_banner.background as AnimationDrawable

        iv_banner.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
                MobclickAgent.onEvent(mActivity,BaseConstant.cace_effects)
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_COMIC)
                startActivity(intent)
            }
        }

        //变老相机
        old.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
                MobclickAgent.onEvent(mActivity,BaseConstant.grow_old)
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_OLD)
                startActivity(intent)
            }
        }

        //一键漫画脸
        comic.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
//                startActivity(Intent(mActivity,ComicActivity::class.java))
                MobclickAgent.onEvent(mActivity,BaseConstant.comic_face)
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_COMIC)
                startActivity(intent)
            }
        }

        //性别转换
        sex.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
//                startActivity(Intent(mActivity,SexActivity::class.java))
                MobclickAgent.onEvent(mActivity,BaseConstant.gender_conversion)
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_SEX)
                startActivity(intent)
            }
        }

        //童颜相机
        young.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
                MobclickAgent.onEvent(mActivity,BaseConstant.tong_yan)
                val intent = Intent(mActivity,OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey,OldActivity2.TYPE_YOUNG)
                startActivity(intent)
            }
        }

        //魔法相机
        magic.setOnClickListener {
            PermissionUtils.askPermission(mActivity,Permission.CAMERA){
                MobclickAgent.onEvent(mActivity,BaseConstant.magic)
                startActivity(Intent(mActivity,MagicCameraActivity::class.java))
            }
        }
    }

    fun startAnim(){
        anim?.stop()
        anim?.start()
    }
}