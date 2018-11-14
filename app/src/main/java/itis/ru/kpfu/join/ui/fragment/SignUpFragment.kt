package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.SignUpPresenter
import itis.ru.kpfu.join.mvp.view.SignUpView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_up.btn_sign_up
import kotlinx.android.synthetic.main.fragment_sign_up.et_email
import kotlinx.android.synthetic.main.fragment_sign_up.et_password
import kotlinx.android.synthetic.main.fragment_sign_up.et_username
import kotlinx.android.synthetic.main.fragment_sign_up.toolbar_sign_up

class SignUpFragment : BaseFragment(), SignUpView {

    companion object {

        fun newInstance(): SignUpFragment {
            val args = Bundle()
            val fragment = SignUpFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override val contentLayout: Int
        get() = R.layout.fragment_sign_up

    override val toolbarTitle: Int?
        get() = R.string.sign_up

    override val menu: Int?
        get() = R.menu.menu

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_sign_up

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_sign_up.setOnClickListener {
            onSignUpClick()
        }
    }

    override fun onError(message: String) {
        //TODO
        Toast.makeText(activity, message, LENGTH_SHORT).show()
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun onSignUpSuccess() {
        activity?.onBackPressed()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onSignUpClick() {
        presenter.onSignUpClick(et_email.text.toString(), et_username.text.toString(), et_password.text.toString())
    }
}