package com.zwc.filepicker

import android.content.Context
import com.zwc.filepicker.entity.LocalFileData
import com.zwc.filepicker.filetype.FileType

/**
 * ClassName loader
 * User: zuoweichen
 * Date: 2022/5/19 17:58
 * Description: 加载
 */
interface LoaderInterface {
    /**
     *
     * @param context Context
     * @param type PickFileType? 筛选类型
     * @param filtrate String? 筛选条件
     */
    fun load(context: Context, types: List<FileType>, page: Int, pageSize: Int): LocalFileData

    /**
     *
     * @param context Context
     * @param types List<FileType>
     * @param page Int
     * @param pageSize Int
     * @param keyWords String 搜索关键词
     * @return LocalFileData
     */
    fun load(
        context: Context,
        keyWords: String
    ): LocalFileData
}