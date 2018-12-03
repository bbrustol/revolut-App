package com.brustoloni.revoluttestapp.presentation.conversions

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.Fragment
import com.brustoloni.revoluttestapp.R
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import com.brustoloni.revoluttestapp.infrastruture.replaceFragment

class ConversionsActivity : AppCompatActivity(), HasSupportFragmentInjector {
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    private val conversionsFragment by lazy { newConversionsFragment() }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conversions)
        if (savedInstanceState == null) replaceFragment(R.id.framelayout_container, conversionsFragment, CONVERSIONS_FRAGMENT_TAG)
    }
}
