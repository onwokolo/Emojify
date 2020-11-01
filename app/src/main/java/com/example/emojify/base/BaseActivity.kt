package com.example.emojify.base

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
//Base Activity Class
//Activities can be represented as views and they do the work that changes the appearance of the screen
//They also pass themselves to the presenter for the logic stuff
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayout())
    }

    @LayoutRes
    abstract fun getLayout(): Int

    fun setActionBar(toolbar: Toolbar, title: String, isBackEnabled: Boolean) {
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.title = title
            actionBar.setDisplayHomeAsUpEnabled(isBackEnabled)
        }
    }
}