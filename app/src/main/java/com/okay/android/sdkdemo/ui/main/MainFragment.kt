package com.okay.android.sdkdemo.ui.main

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.itransition.protectoria.psa_multitenant.protocol.scenarios.linking.LinkingScenarioListener
import com.itransition.protectoria.psa_multitenant.state.ApplicationState
import com.okay.android.sdkdemo.BuildConfig
import com.okay.android.sdkdemo.DemoApplication
import com.okay.android.sdkdemo.R
import com.okay.android.sdkdemo.repository.PreferenceRepository
import com.okay.android.sdkdemo.ui.BaseTheme
import com.protectoria.psa.PsaManager
import com.protectoria.psa.api.entities.SpaEnrollData
import com.protectoria.psa.dex.common.data.enums.PsaType
import kotlinx.android.synthetic.main.main_fragment.*
import javax.inject.Inject

class MainFragment : Fragment(), LinkingScenarioListener {

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
        viewModel = ViewModelProvider(this, viewModelFactory).get(MainViewModel::class.java)
        viewModel.apply {
            getMessage().observe(viewLifecycleOwner, Observer { message ->
                message?.let {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                }
            })
            isEnrolled.observe(viewLifecycleOwner, Observer { isEnrolled ->
                if (isEnrolled) {
                    btnStartEnroll.visibility = View.GONE
                    btnResetEnroll.visibility = View.VISIBLE
                    twEnrollStatus.setText(R.string.application_enrolled)
                    twEnrollStatus.isChecked = true
                } else {
                    btnStartEnroll.visibility = View.VISIBLE
                    btnResetEnroll.visibility = View.GONE
                    twEnrollStatus.setText(R.string.application_not_enrolled)
                    twEnrollStatus.isChecked = false
                }
            })
            startEnroll.observe(viewLifecycleOwner, Observer {
                context?.run {
                    PsaManager.startEnrollmentActivity(
                        activity,
                        SpaEnrollData(
                            preferenceRepository.getAppPNS(),
                            BuildConfig.PUB_PSS_B64,
                            BuildConfig.INSTALLATION_ID,
                            BaseTheme(this).DEFAULT_PAGE_THEME,
                            PsaType.OKAY
                        )
                    )
                }

            })
            getResetEnroll().observe(viewLifecycleOwner, Observer {
                PsaManager.getInstance().resetEnroll()
            })
            startLinkTenant.observe(viewLifecycleOwner, Observer {
                PsaManager.getInstance().linkTenant(
                    it, preferenceRepository, this@MainFragment
                )
            })
        }
        return inflater.inflate(R.layout.main_fragment, null)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUIComponents()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadStates()
    }

    override fun onLinkingCompletedSuccessful(tenantId: Long, tenantName: String?) {
        Toast.makeText(context, "tenant name $tenantName", Toast.LENGTH_SHORT).show()
        Log.i("LINKING", "tenant ID = $tenantId")
    }

    override fun onLinkingFailed(linkingError: ApplicationState?) {
        Toast.makeText(context, "linking error $linkingError", Toast.LENGTH_SHORT).show()
        Log.i("LINKING", "linking error $linkingError")
    }

    private fun initUIComponents() {
        btnECommerceTransaction.setOnClickListener { viewModel.startECommerceTransaction() }
        btnRemittanceTransaction.setOnClickListener { viewModel.startRemittanceTransaction() }
        btnCardTransaction.setOnClickListener { viewModel.startPaymentCardTransaction() }
        btnStartEnroll.setOnClickListener { viewModel.startEnroll() }
        btnResetEnroll.setOnClickListener { viewModel.resetEnroll() }
        btnLinkTenant.setOnClickListener { viewModel.linkTenant() }
        etLinkingCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.setLinkingCode(s.toString())
            }
        })
    }

}
