package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

class SignUpFragment: BaseFragment() {

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
}