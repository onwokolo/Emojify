package com.example.emojify.storage

import android.content.Context
import android.content.Context.MODE_PRIVATE
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.io.FileNotFoundException
import java.io.FileReader
import java.io.FileWriter

//Grab instance of me using "StorageSystem.storage"
class StorageSystem() {
    private var entries: ArrayList<Entry>? = null
    private var context: Context? = null
    private fun getBaseDirectory(): File {
        context?.let {
            return(it.filesDir)
        }
        throw FileNotFoundException("Context was not set")
    }
    private fun getDirectory(file: String): File {
        context?.let {
            return(it.getDir(file, MODE_PRIVATE))
        }
        throw FileNotFoundException("Context was not set")
    }
    private fun getFile(file: String): File {
        context?.let {
            return File(it.filesDir.absolutePath + file)
        }
        throw FileNotFoundException("Context was not set")
    }
    private fun parseFileIfExists(file: File): ArrayList<Entry> {
        if (!file.exists()) {return ArrayList()}
        val reader = FileReader(file)
        val result = deserializeEntries(reader.readText())
        reader.close()
        return result
    }
    private fun serializeEntries(): String {
        return Json.encodeToString(entries)
    }
    private fun deserializeEntries(data: String): ArrayList<Entry> {
        return Json.decodeFromString(data) as ArrayList<Entry>
    }
    fun setContext(context: Context) {
        this.context = context
    }
    //grabs all current entries
    fun getEntries(): ArrayList<Entry> {
        entries?.let {
            return entries as ArrayList<Entry>
        }
        this.entries = parseFileIfExists(getFile("Entries"))
        return this.entries as ArrayList<Entry>
    }
    //commits all current entries to a file for later use
    fun commitEntries() {
        getEntries()//Quick check to be sure entries exists
        FileWriter(getFile("Entries")).let {
            it.write(serializeEntries())
            it.close()
        }

    }
    //adds entry to list
    fun addEntry(entry: Entry) {
        getEntries() //Check to ensure we have the list
        entries?.add(entry)
    }

    fun delEntry(entry: Entry) {
        getEntries() //Check to ensure we have the list
        entries?.remove(entry)
    }
    companion object {
        val storage = StorageSystem()
    }
}