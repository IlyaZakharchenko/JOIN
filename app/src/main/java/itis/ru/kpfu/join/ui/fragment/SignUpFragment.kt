package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.SignUpPresenter
import itis.ru.kpfu.join.mvp.view.SignUpView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

class SignUpFragment : BaseFragment(), SignUpView {

    @InjectPresenter
    lateinit var presenter: SignUpPresenter

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
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override fun showProgress() {
        (activity as? BaseActivity)?.showProgressBar()
    }

    override fun hideProgress() {
        (activity as? BaseActivity)?.hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onSignUpClick() {
        presenter.onSignUpClick()
    }

    override fun openMainFragment() {
        (activity as? FragmentHostActivity)?.setFragment(MainFragment.newInstance(), false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}