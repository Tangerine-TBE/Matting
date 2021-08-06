package com.abc.matting.utils

import android.content.ContentResolver
import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import com.feisukj.base.util.LogUtils
import com.luck.picture.lib.entity.LocalMedia
import java.text.NumberFormat


object Utils {
    fun getUri(selectList: List<LocalMedia>):String{
        if (selectList.isEmpty())
            return ""
        val selectItem = selectList[0]
        return when{
            selectItem.isCut ->{
                LogUtils.e("--------------------${selectItem.cutPath}")
                selectItem.cutPath
            }
            selectItem.isCompressed ->{
                LogUtils.e("-------------------${selectItem.compressPath}")
                selectItem.compressPath
            }
            else ->{
                LogUtils.e("-------------------${selectItem.path}")
                selectItem.path
            }
        }
    }
    fun getReal(selectList: List<LocalMedia>):String{
        if (selectList.isEmpty())
            return ""
        val selectItem = selectList[0]
        return selectItem.realPath
    }


    /**
     * 获取绝对路径
     * */
    fun getRealFilePath(context: Context, uri: Uri?): String? {
        if (null == uri) return null
        val scheme: String? = uri.scheme
        var data: String? = null
        if (scheme == null) data =
            uri.path else if (ContentResolver.SCHEME_FILE == scheme) {
            data = uri.path
        } else if (ContentResolver.SCHEME_CONTENT == scheme) {
            val cursor: Cursor? = context.contentResolver
                .query(uri, arrayOf<String>(MediaStore.Images.ImageColumns.DATA), null, null, null)
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    val index: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
                    if (index > -1) {
                        data = cursor.getString(index)
                    }
                }
                cursor.close()
            }
        }
        return data
    }

    fun moneyFormat(d: Double): String {
        return try {
            val nf = NumberFormat.getNumberInstance()
            nf.minimumFractionDigits = 2
            nf.maximumFractionDigits = 2
            nf.format(d)
        }catch (e: Exception){
            e.printStackTrace()
            ""
        }
    }

    /***
     * 将指定路径的图片转uri
     * @param context
     * @param path ，指定图片(或文件)的路径
     * @return
     */
    fun getMediaUriFromPath(context: Context, path: String): Uri? {
        val mediaUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val cursor = context.contentResolver.query(
            mediaUri,
            null,
            MediaStore.Images.Media.DISPLAY_NAME + "= ?",
            arrayOf(path.substring(path.lastIndexOf("/") + 1)),
            null
        )
        var uri: Uri? = null
        if (cursor!!.moveToFirst()) {
            uri = ContentUris.withAppendedId(
                mediaUri,
                cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media._ID))
            )
        }
        cursor.close()
        return uri
    }
}