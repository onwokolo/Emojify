package com.example.emojify.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.example.emojify.ApplicationStart
import java.io.File

//Grab instance of me using "StorageSystem.storage"
class StorageSystem(androidContext: Context) {

    private var context = androidContext
    private fun getBaseDirectory(): File {
        return(context.filesDir)
    }
    private fun getDirectory(file: String): File {
        return context.getDir(file, MODE_PRIVATE);
    }
    private fun getFile(file: String): File {
        return File(context.filesDir.absolutePath + file)
    }
    fun getEntrys(): ArrayList<Entry>? {
        return null
    }
    fun setEntrys(entries: ArrayList<Entry>) {

    }
    companion object {
        val storage = StorageSystem(ApplicationStart.context)
    }
}