package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

class MainFragment : BaseFragment() {

    companion object {
        fun newInstance(): MainFragment {
            val args = Bundle()
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_main

    override val toolbarTitle: Int?
        get() = R.string.app_name

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

}