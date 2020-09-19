package com.big.moviedb.di.component

import com.big.moviedb.di.FragmentScope
import com.big.moviedb.di.module.MainFragmentModule
import com.big.moviedb.ui.main.HomeFragment
import dagger.Component

@FragmentScope
@Component(dependencies = [ApplicationComponent::class], modules = [MainFragmentModule::class])
interface FragmentComponent {

    fun inject(fragment: HomeFragment)

}
