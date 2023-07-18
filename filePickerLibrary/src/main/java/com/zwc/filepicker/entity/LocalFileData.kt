package com.zwc.filepicker.entity

/**
 * ClassName LocalFileData
 * User: zuoweichen
 * Date: 2022/5/26 11:27
 * Description: 描述
 */
class LocalFileData {
    private var hasNextMore = false
    private var data: ArrayList<LocalMedia>

    constructor(hasNextMore: Boolean, data: ArrayList<LocalMedia>) {
        this.hasNextMore = hasNextMore
        this.data = data
    }

    fun isHasNextMore(): Boolean {
        return hasNextMore
    }

    fun getData(): ArrayList<LocalMedia> {
        return data
    }
}