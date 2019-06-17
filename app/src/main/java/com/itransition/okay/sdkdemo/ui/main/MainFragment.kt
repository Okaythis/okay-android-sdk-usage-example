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
import com.itransition.okay.sdkdemo.BuildConfig
import com.itransition.okay.sdkdemo.DemoApplication
import com.itransition.okay.sdkdemo.R
import com.itransition.okay.sdkdemo.databinding.MainFragmentBinding
import com.itransition.okay.sdkdemo.repository.PreferenceRepository
import com.itransition.okay.sdkdemo.ui.BaseTheme
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import javax.inject.Inject

class MainFragment : Fragment() {

    private lateinit var dataBinding: MainFragmentBinding
    @Inject
    lateinit var viewModelFactory: MainViewModelFactory
    @Inject
    lateinit var preferenceRepository: PreferenceRepository

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
            getMessage().observe(viewLifecycleOwner, Observer { message ->
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
            startEnroll.observe(viewLifecycleOwner, Observer {
                context?.run {
                    PsaManager.startEnrollmentActivity(
                        activity,
                        SpaEnrollData(
                            preferenceRepository.getAppPns(),
                            BuildConfig.PUB_PSS_B64,
                            BuildConfig.INSTALLATION_ID,
                            BaseTheme(this).DEFAULT_PAGE_THEME,
                            PsaType.OKAY
                        )
                    )
                }

            })
            getResetEnroll().observe(this@MainFragment, Observer {
                PsaManager.getInstance().resetEnroll()
            })
        }
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)
        dataBinding.lifecycleOwner = viewLifecycleOwner
        dataBinding.viewModel = viewModel
        return dataBinding.root
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStates()
    }

}
