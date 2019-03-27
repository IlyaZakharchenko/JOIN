package itis.ru.kpfu.join.presentation.ui.auth.signup.steptwo

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.RegistrationFormModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.btn_sign_up
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.et_confirmation_code
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.ti_confirmation_code
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.toolbar_sign_up_two
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.tv_resend_code
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class SignUpStepTwoFragment : BaseFragment(), SignUpStepTwoView {

    companion object {
        private const val USER = "user"

        fun newInstance(user: RegistrationFormModel): SignUpStepTwoFragment {
            val args = Bundle()
            args.putSerializable(USER, user)

            val fragment = SignUpStepTwoFragment()
            fragment.arguments = args
            return fragment
        }

    }

    override val contentLayout: Int
        get() = R.layout.fragment_sign_up_step_two
    override val toolbarTitle: Int?
        get() = R.string.sign_up

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_sign_up_two

    @InjectPresenter
    lateinit var presenter: SignUpStepTwoPresenter

    @Inject
    lateinit var presenterProvider: Provider<SignUpStepTwoPresenter>

    @ProvidePresenter
    fun providePresenter(): SignUpStepTwoPresenter = presenterProvider.get()

    fun getRegistrationFormModel(): RegistrationFormModel = arguments?.getSerializable(USER) as? RegistrationFormModel
            ?: throw IllegalArgumentException("registration form model is null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.checkButtonState(RxTextView.textChanges(et_confirmation_code))

        tv_resend_code.setOnClickListener { presenter.onResendCode() }
        btn_sign_up.setOnClickListener { presenter.onRegistrationFinish(et_confirmation_code.rawText.toString().trim()) }
    }

    override fun setButtonEnabled(state: Boolean) {
        btn_sign_up.isEnabled = state
        ti_confirmation_code.error = null
    }

    override fun showSuccessMessage() {
        Toast.makeText(activity, "Регистрация успешна", Toast.LENGTH_SHORT).show()
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, true)
    }

    override fun updateSendAgainMessage(text: String, isClickable: Boolean) {
        tv_resend_code.text = text
        tv_resend_code.isClickable = isClickable
    }
}
