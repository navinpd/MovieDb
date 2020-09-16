package com.big.moviedb.di.component

import com.big.moviedb.di.ActivityScope
import com.big.moviedb.di.module.ActivityModule
import com.big.moviedb.ui.MainActivity
import dagger.Component

@ActivityScope
@Component(dependencies = [ApplicationComponent::class], modules = [ActivityModule::class])
interface ActivityComponent {

    fun inject(mainActivity: MainActivity)

}