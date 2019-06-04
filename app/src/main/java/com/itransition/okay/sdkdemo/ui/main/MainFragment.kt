package com.itransition.okay.sdkdemo.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.okay.sdkdemo.R
import com.itransition.okay.sdkdemo.databinding.MainFragmentBinding
import javax.inject.Inject

class MainFragment : Fragment() {

    private lateinit var dataBinding: MainFragmentBinding
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DemoApplication.appComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.apply {
            getMessage().observe(this@MainFragment, Observer { message ->
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
            startEnroll.observe(this@MainFragment, Observer {

            })
        }
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        dataBinding.lifecycleOwner = this
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }
}
