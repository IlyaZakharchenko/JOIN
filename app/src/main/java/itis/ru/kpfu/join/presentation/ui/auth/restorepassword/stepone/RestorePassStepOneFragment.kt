package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.stepone

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo.RestorePassStepTwoFragment
import kotlinx.android.synthetic.main.fragment_restore_pass_step_one.*
import javax.inject.Inject
import javax.inject.Provider

class RestorePassStepOneFragment : BaseFragment(), RestorePassStepOneView {

    companion object {
        fun newInstance(): RestorePassStepOneFragment {
            val args = Bundle()
            val fragment = RestorePassStepOneFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_restore_pass_step_one

    override val toolbarTitle: Int?
        get() = R.string.restore_password

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_restore_pass

    @InjectPresenter
    lateinit var presenter: RestorePassStepOnePresenter

    @Inject
    lateinit var presenterProvider: Provider<RestorePassStepOnePresenter>

    @ProvidePresenter
    fun providePresenter(): RestorePassStepOnePresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_restore_pass_send_code.setOnClickListener { presenter.onSendCode(et_restore_pass_email.text.toString().trim()) }
    }

    override fun setIsValidEmail(isValid: Boolean) {
        ti_restore_pass_email.error = if(!isValid) getString(R.string.error_email) else null
    }

    override fun setStepTwoFragment(email: String) {
        (activity as? FragmentHostActivity)?.setFragment(RestorePassStepTwoFragment.getInstance(email), true)
    }
}