package com.demo.filepicker

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.demo.filepicker.databinding.ActivityMainBinding
import com.socks.library.KLog
import com.tbruyelle.rxpermissions3.RxPermissions
import com.zwc.filepicker.YNFilePicker
import com.zwc.filepicker.entity.LocalMedia
import com.zwc.filepicker.filetype.*
import com.zwc.filepicker.listener.OnChooseListener
import io.github.idonans.core.util.ToastUtil
import timber.log.Timber


class MainActivity : AppCompatActivity() {
    companion object {
        const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.checkPermission.setOnClickListener {
            checkPermission()
        }
        binding.btn1.setOnClickListener {
            YNFilePicker.from(this).build().maxSelectable(7)
                .setOnChooseListener(object : OnChooseListener {
                    override fun onChoose(data: ArrayList<LocalMedia>) {
                        KLog.i(data)
                    }
                }).launch()
        }
        binding.btn2.setOnClickListener {
            YNFilePicker.from(this).build().maxSelectable(7).maxFileSize(1024 * 1024 * 100)
                .setOnChooseListener(object : OnChooseListener {
                    override fun onChoose(data: ArrayList<LocalMedia>) {
                        KLog.i(data)
                    }
                }).launch()
        }
        binding.btn3.setOnClickListener {
            YNFilePicker.from(this).build().maxSelectable(7).choose(
                arrayListOf(
                    ImageFileType,
                    DocumentFileType,
                )
            ).setOnChooseListener(object : OnChooseListener {
                override fun onChoose(data: ArrayList<LocalMedia>) {
                    KLog.i(data)
                }
            }).launch()
        }
        binding.btn4.setOnClickListener {
            YNFilePicker.from(this).build().maxSelectable(7).choose(
                arrayListOf(
                    ImageFileType,
                )
            ).setOnChooseListener(object : OnChooseListener {
                override fun onChoose(data: ArrayList<LocalMedia>) {
                    KLog.i(data)
                }
            }).launch()
        }
        binding.btn5.setOnClickListener {
            YNFilePicker.from(this).build().maxSelectable(7).choose(
                arrayListOf(
                    CustomFileType(),
                )
            ).setOnChooseListener(object : OnChooseListener {
                override fun onChoose(data: ArrayList<LocalMedia>) {
                    KLog.i(data)
                }
            }).launch()
        }
        binding.sysBtn.setOnClickListener {
            YNFilePicker.from(this).build().choose(
                arrayListOf(
                    ImageFileType,
                    AudioFileType,
                    VideoFileType,
                    DocumentFileType,
                    RARFileType,
                    CustomFileType()
                )
            ).maxSelectable(7).maxFileSize(1024 * 1024 * 100)
                .setOnChooseListener(object : OnChooseListener {
                    override fun onChoose(data: ArrayList<LocalMedia>) {
                        KLog.i(data)
                    }
                }).launch()
        }
        binding.testBtn.setOnClickListener {
            //SystemMediaTestActivity.start(this)
            val path1 =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).absolutePath
            val path2 = getExternalFilesDir(null)
            KLog.i(TAG, path1)
            KLog.i(TAG, path2)
        }
    }


    private fun checkPermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager()) {
            //Toast.makeText(this, "We can access all files on external storage now", Toast.LENGTH_SHORT).show()
            val rxPermissions = RxPermissions(this)
            rxPermissions.request(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ).subscribe({
                if (it) {
                    ToastUtil.show("已有权限")
                } else {
                    ToastUtil.show("无权限")
                }
            }, {
                ToastUtil.show("失败")
            })
        } else {
            val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
            intent.data = Uri.parse("package:$packageName")
            startActivityForResult(intent, 10001)
            return
        }
    }
}