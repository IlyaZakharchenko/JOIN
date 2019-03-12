package itis.ru.kpfu.join.presentation.ui.auth.signup.stepone

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.R.string
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo.SignUpStepTwoFragment
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.btn_sign_up
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.et_email
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.et_password
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.et_password_repeat
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.et_username
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.ti_email
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.ti_password
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.ti_password_repeat
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.ti_username
import kotlinx.android.synthetic.main.fragment_sign_up_step_one.toolbar_sign_up_one
import javax.inject.Inject
import javax.inject.Provider

class SignUpStepOneFragment : BaseFragment(), SignUpStepOneView {

    companion object {

        fun newInstance(): SignUpStepOneFragment {
            val args = Bundle()
            val fragment = SignUpStepOneFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_sign_up_step_one

    override val toolbarTitle: Int?
        get() = R.string.sign_up

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_sign_up_one

    @InjectPresenter
    lateinit var presenter: SignUpStepOnePresenter

    @Inject
    lateinit var presenterProvider: Provider<SignUpStepOnePresenter>

    @ProvidePresenter
    fun providePresenter(): SignUpStepOnePresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.checkButtonState(RxTextView.textChanges(et_username), RxTextView.textChanges(et_email),
                RxTextView.textChanges(et_password),
                RxTextView.textChanges(et_password_repeat))

        btn_sign_up.setOnClickListener {
            presenter.onSignUpClick(
                    RegistrationFormModel(et_username.text.toString(),
                            et_email.text.toString(),
                            et_password.text.toString(), et_password_repeat.text.toString()))
        }
    }

    override fun setSignUpStepTwoFragment(user: RegistrationFormModel) {
        ti_username.error = null
        ti_email.error = null
        ti_password.error = null
        ti_password_repeat.error = null

        (activity as? FragmentHostActivity)?.setFragment(SignUpStepTwoFragment.newInstance(user), true)
    }

    override fun setButtonEnabled(enabled: Boolean) {
        btn_sign_up.isEnabled = enabled
    }

    override fun onPasswordsNotEquals() {
        ti_password.error = getString(string.error_password_should_be_equals)
        ti_password_repeat.error = getString(string.error_password_should_be_equals)
    }

    override fun onInvalidUsername() {
        ti_username.error = getString(string.error_username)
    }

    override fun onInvalidEmail() {
        ti_email.error = getString(string.error_email)
    }

    override fun onInvalidPassword() {
        ti_password.error = getString(string.error_password)
        ti_password_repeat.error = getString(string.error_password)
    }
}
