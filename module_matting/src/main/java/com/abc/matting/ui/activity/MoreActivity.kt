package com.abc.matting.ui.activity

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import com.abc.matting.R
import com.abc.matting.adapter.MoreAdapter
import com.abc.matting.bean.PictureBean
import com.abc.matting.ui.dialog.NoSaveDialog
import com.abc.matting.ui.dialog.ReNameDialog
import com.feisukj.base.BaseConstant
import com.feisukj.base.baseclass.BaseActivity
import com.feisukj.base.util.DateUtils
import com.feisukj.base.util.ToastUtil
import kotlinx.android.synthetic.main.activity_more.*
import java.io.File
import java.util.*

class MoreActivity : BaseActivity(), MoreAdapter.MoreCallback, ReNameDialog.ReNameCallback {
    companion object{
        const val GO = 120
        const val BACK = 121
    }

    private var mList: MutableList<PictureBean> = arrayListOf()

    private var adapter: MoreAdapter = MoreAdapter()
    private lateinit var reNameDialog: ReNameDialog
    private var showDelete = false

    override fun getLayoutId(): Int = R.layout.activity_more

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.theme_bg).statusBarDarkFont(false).init()
        reNameDialog = ReNameDialog(this)
        getData()
        adapter.setCallback(this)
        recycler.layoutManager = GridLayoutManager(this,3)
        recycler.adapter = adapter

        barBack.setOnClickListener {
            finish()
        }

        tv_edit.setOnClickListener {
            showDelete = !showDelete
            adapter.showDelete(showDelete)
            tv_edit.text = if (showDelete) "完成" else "编辑"
        }
    }

    override fun itemClick(position: Int) {
        val intent = Intent(this,DetailsActivity::class.java)
        intent.putExtra(DetailsActivity.PATH_KEY,mList[position].filePath)
        startActivityForResult(intent, GO)
    }

    override fun rename(position: Int) {
        reNameDialog = ReNameDialog(this)
        reNameDialog.setCallback(this)
        reNameDialog.setBean(mList[position])
        reNameDialog.show()
    }

    override fun delete(position: Int) {
        val path = mList[position].filePath
        if (path.isEmpty())
            return
        NoSaveDialog(this,{
            val file = File(path)
            if (file.delete()){
                ToastUtil.showCenterToast("删除成功")
                getData()
            }else{
                ToastUtil.showCenterToast("删除失败")
            }
        },{

        }).setTitleText("是否删除${mList[position].fileName}?").setCancelText("取消").setConfirmText("确定").show()

    }

    override fun rename(bean: PictureBean, name: String) {
        mList.forEach {
            if (it.fileName == name){
                ToastUtil.showCenterToast("该文件已存在")
                return
            }
        }
        val file = File(bean.filePath)
        var newPath = bean.filePath
        newPath = newPath.substring(0,newPath.lastIndexOf("/"))+"/"+name
        if (file.renameTo(File(newPath))){
            ToastUtil.showCenterToast("修改成功")
            reNameDialog.dismiss()
            getData()
        }else{
            ToastUtil.showCenterToast("修改失败")
        }
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
        adapter.setData(mList)
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
        if (requestCode == GO && resultCode == BACK){
            finish()
        }
    }

}