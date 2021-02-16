package com.okay.android.sdkdemo.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.itransition.protectoria.psa_multitenant.protocol.scenarios.linking.LinkingScenarioListener
import com.itransition.protectoria.psa_multitenant.state.ApplicationState
import com.okay.android.sdkdemo.BuildConfig
import com.okay.android.sdkdemo.DemoApplication
import com.okay.android.sdkdemo.R
import com.okay.android.sdkdemo.databinding.MainFragmentBinding
import com.okay.android.sdkdemo.repository.PreferenceRepository
import com.okay.android.sdkdemo.ui.BaseTheme
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment(), LinkingScenarioListener {

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
    ): View {
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
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
                            preferenceRepository.appPNS,
                            BuildConfig.PUB_PSS_B64,
                            BuildConfig.INSTALLATION_ID,
                            BaseTheme(this).DEFAULT_PAGE_THEME,
                            PsaType.OKAY
                        )
                    )
                }
            })
            getResetEnroll().observe(viewLifecycleOwner, {
                PsaManager.getInstance().resetEnroll()
            })
            startLinkTenant.observe(viewLifecycleOwner, {
                PsaManager.getInstance().linkTenant(
                    it, preferenceRepository, this@MainFragment
                )
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

    override fun onLinkingCompletedSuccessful(tenantId: Long, tenantName: String) {
        et_tenant_linking_code.text?.clear()
        preferenceRepository.putTenantName(tenantName)
        Toast.makeText(context, getString(R.string.tenant_linking_successful, tenantName), Toast.LENGTH_SHORT).show()
        Log.i("LINKING", "tenant ID = $tenantId")
    }

    override fun onLinkingFailed(linkingError: ApplicationState?) {
        Toast.makeText(context, "linking error: ${linkingError?.name}", Toast.LENGTH_LONG).show()
        Log.i("LINKING", "linking error: ${linkingError?.name}")
    }

}
