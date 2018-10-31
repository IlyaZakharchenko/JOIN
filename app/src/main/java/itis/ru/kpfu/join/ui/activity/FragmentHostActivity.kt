package itis.ru.kpfu.join.ui.activity

import android.os.Bundle
import android.support.v7.widget.Toolbar
import com.crashlytics.android.Crashlytics
import io.fabric.sdk.android.Fabric
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.MainFragment
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.activity_main.toolbar_main_activity


class FragmentHostActivity : BaseActivity() {

    override val contentLayout: Int
        get() = R.layout.activity_main

    override val menu: Int?
        get() = null

    override val toolbarTitle: Int?
        get() = R.string.app_name

    override val enableBackPressed: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_main_activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Fabric.with(this, Crashlytics())
        setFragment(MainFragment.newInstance(), false)
    }

    fun setFragment(fragment: BaseFragment, addToBackStack: Boolean) {
        val transaction = supportFragmentManager?.beginTransaction()
        if (addToBackStack) {
            transaction?.addToBackStack("")
        }
        transaction?.replace(R.id.main_container, fragment)?.commit()
    }
}