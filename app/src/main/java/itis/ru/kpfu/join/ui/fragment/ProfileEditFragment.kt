package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.view.PersonalProfileView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_personal_profile.toolbar_personal_profile

class PersonalProfileFragment : BaseFragment(), PersonalProfileView {

    override val contentLayout: Int
        get() = R.layout.fragment_personal_profile
    override val toolbarTitle: Int?
        get() = R.string.toolbar_personal_profile
    override val menu: Int?
        get() = null
    override val enableBackPressed: Boolean
        get() = false
    override val enableBottomNavBar: Boolean
        get() = true
    override val toolbar: Toolbar?
        get() = toolbar_personal_profile

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun onError(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_personal_profile, container, false)
    }
}
