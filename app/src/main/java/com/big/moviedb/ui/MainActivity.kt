package com.big.moviedb.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.big.moviedb.MyApplication
import com.big.moviedb.R
import com.big.moviedb.di.component.DaggerActivityComponent
import com.big.moviedb.di.module.ActivityModule
import com.big.moviedb.ui.main.HomeFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        setUpDependencies()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                    .replace(R.id.container, HomeFragment.newInstance())
                    .commitNow()
        }
    }


    private fun setUpDependencies() {
        DaggerActivityComponent
                .builder()
                .applicationComponent((application as MyApplication).applicationComponent)
                .activityModule(ActivityModule(this))
                .build()
                .inject(this)
    }
}