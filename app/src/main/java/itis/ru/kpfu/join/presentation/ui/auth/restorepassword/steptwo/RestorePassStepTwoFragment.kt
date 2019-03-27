package itis.ru.kpfu.join.presentation.ui.auth.restorepassword.steptwo

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import kotlinx.android.synthetic.main.fragment_restore_pass_step_two.*
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class RestorePassStepTwoFragment : BaseFragment(), RestorePassStepTwoView {

    companion object {
        private const val EMAIL = "email"

        fun getInstance(email: String) = RestorePassStepTwoFragment().also {
            it.arguments = Bundle().apply {
                putString(EMAIL, email)
            }
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_restore_pass_step_two

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
    lateinit var presenter: RestorePassStepTwoPresenter

    @Inject
    lateinit var presenterProvider: Provider<RestorePassStepTwoPresenter>

    @ProvidePresenter
    fun providePresenter(): RestorePassStepTwoPresenter = presenterProvider.get()

    fun getEmail(): String = arguments?.getString(EMAIL)
            ?: throw IllegalArgumentException("email is null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_restore_pass_change_password.setOnClickListener {
            presenter.onPasswordChange(
                    et_restore_pass_new_password.text.toString().trim(),
                    et_restore_pass_code.text.toString().trim())
        }
    }

    override fun setIsValidNewPassword(isValid: Boolean) {
        ti_restore_pass_new_password.error = if (!isValid) getString(R.string.error_password) else null
    }

    override fun setIsValidCode(isValid: Boolean) {
        ti_restore_pass_code.error = if (!isValid) getString(R.string.error_code) else null
    }

    override fun setSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false, true)
    }

    override fun showSuccessMessage() {
        Toast.makeText(context, getText(R.string.password_successfully_changed), Toast.LENGTH_SHORT).show()
    }
}