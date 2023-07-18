package com.zwc.filepicker.entity

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.NonNull

/**
 * ClassName FileData
 * User: zuoweichen
 * Date: 2022/5/26 11:07
 * Description: 描述
 */
class LocalMedia() : Parcelable {
    var id: Int = 0
    var title = ""

    /**
     * 显示名称
     */
    var displayName = ""

    @NonNull
    var path: String = ""
    var size: Long = 0L
    var mimeType: String = "image/*"
    var duration: Int = 0
    var width: Int = 0
    var height: Int = 0
    var addTime: Long = 0

    /**
     * 是否选中
     */
    var selected = false
    var position: Int = 0
    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        title = parcel.readString().toString()
        displayName = parcel.readString().toString()
        path = parcel.readString().toString()
        size = parcel.readLong()
        mimeType = parcel.readString().toString()
        duration = parcel.readInt()
        width = parcel.readInt()
        height = parcel.readInt()
        addTime = parcel.readLong()
        selected = parcel.readByte() != 0.toByte()
    }


    //做本地判断是否有相同的文件数据出现
    override fun equals(other: Any?): Boolean {
        if (other == null || this.javaClass != other.javaClass) {
            return false
        }
        val otherData = other as LocalMedia
        return this.id == otherData.id
    }

    override fun hashCode(): Int {
        return id
    }

    override fun toString(): String {
        return "LocalMedia(id=$id, title='$title', displayName='$displayName', path='$path', size=$size, mimeType='$mimeType', duration=$duration, width=$width, height=$height, addTime=$addTime, selected=$selected)"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(title)
        parcel.writeString(displayName)
        parcel.writeString(path)
        parcel.writeLong(size)
        parcel.writeString(mimeType)
        parcel.writeInt(duration)
        parcel.writeInt(width)
        parcel.writeInt(height)
        parcel.writeLong(addTime)
        parcel.writeByte(if (selected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocalMedia> {
        override fun createFromParcel(parcel: Parcel): LocalMedia {
            return LocalMedia(parcel)
        }

        override fun newArray(size: Int): Array<LocalMedia?> {
            return arrayOfNulls(size)
        }
    }


}