package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.jakewharton.rxbinding2.widget.RxTextView
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.model.UserRegistrationForm
import itis.ru.kpfu.join.mvp.presenter.SignUpStepTwoPresenter
import itis.ru.kpfu.join.mvp.view.SignUpStepTwoView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.utils.Constants
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.btn_sign_up
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.et_confirmation_code
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.ti_confirmation_code
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.toolbar_sign_up_two
import kotlinx.android.synthetic.main.fragment_sign_up_step_two.tv_resend_code

class SignUpStepTwoFragment: BaseFragment(), SignUpStepTwoView {

    companion object {

        fun newInstance(user: UserRegistrationForm): SignUpStepTwoFragment {
            val args = Bundle()
            args.putSerializable(Constants.USER, user)

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

    @ProvidePresenter
    fun providePresenter(): SignUpStepTwoPresenter{
        return JoinApplication.appComponent.providePresenters().provideSignUpStepTwoPresenter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = arguments?.getSerializable(Constants.USER) as UserRegistrationForm

        presenter.checkButtonState(RxTextView.textChanges(et_confirmation_code))
        presenter.startCounter()

        tv_resend_code.setOnClickListener { user.email.let { it1 -> presenter.resendCode(it1) } }
        btn_sign_up.setOnClickListener {
            user.code = et_confirmation_code.rawText.toString()
            presenter.finishRegistration(user)
        }
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun buttonEnabled(state: Boolean) {
        btn_sign_up.isEnabled = state
        ti_confirmation_code.error = null
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onRegistrationSuccess() {
        Toast.makeText(activity, "Registration Successful", Toast.LENGTH_SHORT).show()
        (activity as? FragmentHostActivity)?.clearFragmentsStack()
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false)
    }

    override fun updateSendAgainMessage(text: String, isClickable: Boolean) {
        tv_resend_code.text = text
        tv_resend_code.isClickable = isClickable
    }

    override fun onPause() {
        presenter.stopTimer()
        super.onPause()
    }

    override fun onResume() {
        presenter.resumeTimer()
        super.onResume()
    }

    override fun onCodeInvalid() {
        ti_confirmation_code.error = "Неверный код"
    }
}
