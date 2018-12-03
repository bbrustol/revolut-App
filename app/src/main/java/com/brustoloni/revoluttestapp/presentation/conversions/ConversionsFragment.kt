package com.brustoloni.revoluttestapp.presentation.conversions

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.brustoloni.revoluttestapp.BuildConfig
import com.brustoloni.revoluttestapp.R

import dagger.android.support.AndroidSupportInjection
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_conversions.view.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.warn

import javax.inject.Inject

val CONVERSIONS_FRAGMENT_TAG = ConversionsFragment::class.java.name

private val TAG = ConversionsFragment::class.java.name

fun newConversionsFragment() = ConversionsFragment()

class ConversionsFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val log = AnkoLogger(this.javaClass)

    private lateinit var viewModel: ConversionsViewModel

    private val conversionsAdapter by lazy { ConversionsAdapter() }

    private var newBaseDisposse: Disposable? = null

    private val stateObserver = Observer<ConversionsState> { state ->
        state?.let {
            when (state) {
                is DefaultState -> {
//                    viewModel.updateBase()
                    conversionsAdapter.updateData(viewModel.getCoonvertToArrayList(), it.conversions.base)
                }
                is ErrorState -> {
                    log.warn { viewModel.stateLiveData.value }
                }
            }
        }
    }

    private fun initializeRecyclerView(view:View) {
        view.rv_conversions.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = conversionsAdapter
        }
        setupItemClick()
    }
    private fun dismissObeservers() {
        newBaseDisposse?.dispose()
        viewModel.disposable?.dispose()
        viewModel.stateLiveData.removeObserver(stateObserver)
    }

    private fun setupItemClick() {
        newBaseDisposse = conversionsAdapter.clickEvent
            .subscribe {
                viewModel.updateBase(it)
            }
    }

    override fun onAttach(context: Context?) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(ConversionsViewModel::class.java)
        viewModel.updateRecycled(BuildConfig.CURRENCY_DEFAULT)
    }

    override fun onResume() {
        super.onResume()
        viewModel.stateLiveData.observe(this, stateObserver)
    }
    override fun onDestroy() {
        super.onDestroy()
        dismissObeservers()
    }

    override fun onPause() {
        super.onPause()
        dismissObeservers()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_conversions, container, false)
        initializeRecyclerView(view)
        return view
    }
}