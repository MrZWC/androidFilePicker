package com.zwc.filepicker.widget

import com.flyco.tablayout.listener.CustomTabEntity

/**
 * ClassName FileTabEntity
 * User: zuoweichen
 * Date: 2022/5/23 17:18
 * Description: 描述
 */
internal class FileTabEntity : CustomTabEntity {
    var title: String = ""
    var selectedIcon = 0
    var unSelectedIcon = 0

    constructor(title: String, selectedIcon: Int, unSelectedIcon: Int) {
        this.title = title
        this.selectedIcon = selectedIcon
        this.unSelectedIcon = unSelectedIcon
    }

    constructor(title: String) {
        this.title = title
    }

    override fun getTabTitle(): String {
        return title
    }

    override fun getTabSelectedIcon(): Int {
        return selectedIcon
    }

    override fun getTabUnselectedIcon(): Int {
        return unSelectedIcon
    }
}