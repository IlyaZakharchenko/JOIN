package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.RestorePassPresenter
import itis.ru.kpfu.join.mvp.view.RestorePassView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_restore_pass.btn_restore_pass_restore
import kotlinx.android.synthetic.main.fragment_restore_pass.toolbar_restore_pass

class RestorePassFragment : BaseFragment(), RestorePassView {

    companion object {
        fun newInstance(): RestorePassFragment {
            val args = Bundle()
            val fragment = RestorePassFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_restore_pass

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
    lateinit var presenter: RestorePassPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_restore_pass_restore.setOnClickListener { Toast.makeText(baseActivity, "OK", Toast.LENGTH_SHORT).show() }
    }
}