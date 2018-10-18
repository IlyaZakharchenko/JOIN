package itis.ru.kpfu.join.ui.fragment

import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

class SignInFragment : BaseFragment() {

    override val contentLayout: Int
        get() = R.layout.fragment_sign_in

    override val toolbarTitle: Int?
        get() = R.string.sign_in

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false
}