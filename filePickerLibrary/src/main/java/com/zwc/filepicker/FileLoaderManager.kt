package com.zwc.filepicker

import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.socks.library.KLog
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.entity.LocalFileData
import com.zwc.filepicker.filetype.FileType

/**
 * ClassName LoaderManager
 * User: zuoweichen
 * Date: 2022/5/20 17:12
 * Description: 描述
 */
class FileLoaderManager : LoaderInterface {
    companion object {
        const val TAG = "LoaderManager"
    }

    override fun load(
        context: Context, types: List<FileType>, page: Int, pageSize: Int
    ): LocalFileData {
        return loadLocalData(context, types, page, pageSize, null)
    }

    override fun load(
        context: Context, keyWords: String
    ): LocalFileData {
        return loadLocalData(context, null, 1, 100, keyWords)
    }

    private fun loadLocalData(
        context: Context, types: List<FileType>?, page: Int, pageSize: Int, keyWords: String?
    ): LocalFileData {
        if (types == null && keyWords.isNullOrEmpty()) {
            throw IllegalStateException("Types和keyWords不能同时为空")
        }
        if (types != null && !keyWords.isNullOrEmpty()) {
            throw IllegalStateException("Types和keyWords不能同时不为空")
        }
        var select = ""
        if (types != null) {
            select = createSelect(types)
        }
        if (!keyWords.isNullOrEmpty()) {
            select = createSearchSelect(keyWords)
        }
        val contentResolver = context.contentResolver
        val sortField = MediaStore.Files.FileColumns.DATE_ADDED
        val contentUri = MediaStore.Files.getContentUri("external")
        val cursor = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val orderBy: String = MediaStore.Files.FileColumns.DATE_ADDED + " DESC"
            val sqlQueryBundle = createSqlQueryBundle(orderBy, select, pageSize, page, pageSize)
            contentResolver.query(contentUri, null, sqlQueryBundle, null)
        } else {
            val orderBy: String =
                if (page == -1) "$sortField DESC" else "$sortField DESC limit $pageSize offset ${(page - 1) * pageSize}"
            contentResolver.query(
                contentUri, null, select, null, orderBy
            )
        }

        if (cursor == null) {
            throw NullPointerException()
        }
        val list = ArrayList<LocalMedia>()
        var isHasNextMore = false
        if (cursor.count > 0) {
            cursor.moveToFirst()
            do {
                try {
                    val fileData = cursorToLocalMedia(cursor)
                    list.add(fileData)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } while (cursor.moveToNext())
        }
        isHasNextMore = cursor.count > 0
        cursor.close()
        return LocalFileData(isHasNextMore, list)
    }

    /**
     * 创建查询条件
     * @param types List<FileType>
     * @return String
     */
    private fun createSelect(types: List<FileType>): String {
        var filters = ""
        for ((groupIndex, type) in types.withIndex()) {
            for ((index, data) in type.getFileFilter().withIndex()) {
                filters = filters + MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE '%.$data'"
                if (!((groupIndex == types.size - 1) && (index == type.getFileFilter().size - 1))) {
                    filters += " or "
                }
            }

        }
        filters = "($filters) AND ${MediaStore.Files.FileColumns.SIZE}>0"
        return filters
    }

    private fun createSearchSelect(keys: String): String {
        val mimeTypeList = SelectionSpec.getInstance().mimeTypeList ?: return ""
        var filters = ""
        for ((groupIndex, type) in mimeTypeList.withIndex()) {
            for ((index, data) in type.getFileFilter().withIndex()) {
                filters = filters + MediaStore.Files.FileColumns.DISPLAY_NAME + " LIKE '%.$data'"
                if (!((groupIndex == mimeTypeList.size - 1) && (index == type.getFileFilter().size - 1))) {
                    filters += " or "
                }
            }

        }
        return "(${MediaStore.Files.FileColumns.DISPLAY_NAME} LIKE '%$keys%' OR ${MediaStore.Files.FileColumns.TITLE} LIKE '%$keys%') AND (${filters})"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun createSqlQueryBundle(
        sortOrder: String, selection: String?, limit: Int, page: Int, pageSize: Int
    ): Bundle {
        val bundle = Bundle()
        if (!TextUtils.isEmpty(sortOrder)) {
            bundle.putString(ContentResolver.QUERY_ARG_SQL_SORT_ORDER, sortOrder)
        }
        if (selection != null) {
            bundle.putString(ContentResolver.QUERY_ARG_SQL_SELECTION, selection)
        }
        if (page != -1) {
            bundle.putString(
                ContentResolver.QUERY_ARG_SQL_LIMIT,
                limit.toString() + " offset " + (page - 1) * pageSize
            )
        }
        return bundle
    }

    private fun cursorToLocalMedia(cursor: Cursor): LocalMedia {
        val localMedia = LocalMedia()
        localMedia.id =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns._ID))
        localMedia.title =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.TITLE))
        localMedia.displayName =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME))
        localMedia.path =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATA))
        localMedia.size =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.SIZE))
        localMedia.width =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.WIDTH))
        localMedia.height =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.HEIGHT))
        localMedia.mimeType =
            cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.MIME_TYPE))
                ?: "any/*"
        localMedia.duration =
            cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DURATION))
        var addTime = 0L
        addTime =
            cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DATE_ADDED))
        if (addTime > 0) {
            addTime *= 1000//转换成毫秒
        }
        localMedia.addTime = addTime
        KLog.i(TAG, localMedia)
        KLog.i(TAG, "--------------------------------------------------------")
        if (SelectionSpec.getInstance().selectDataList.contains(localMedia)) {
            localMedia.selected = true
        }
        return localMedia
    }
}