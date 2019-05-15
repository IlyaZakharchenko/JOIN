package itis.ru.kpfu.join.presentation.ui.splash

import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.adapter.SpecializationsEditAdapter
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.ui.auth.signin.SignInFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import javax.inject.Inject
import javax.inject.Provider

class SplashFragment : BaseFragment(), SplashView {

    companion object {
        fun newInstance(): SplashFragment {
            val args = Bundle()
            val fragment = SplashFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_splash
    override val toolbarTitle: Int?
        get() = null
    override val menu: Int?
        get() = null
    override val enableBackPressed: Boolean
        get() = false
    override val enableBottomNavBar: Boolean
        get() = false
    override val toolbar: Toolbar?
        get() = null

    @InjectPresenter
    lateinit var presenter: SplashPresenter

    @Inject
    lateinit var presenterProvider: Provider<SplashPresenter>

    @ProvidePresenter
    fun providePresenter(): SplashPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.wakeServerUp()
    }

    override fun openAllProjectsFragment() {
        (activity as? FragmentHostActivity)?.setFragment(AllProjectsFragment.newInstance(), false)
    }

    override fun openSignInFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignInFragment.newInstance(), false)
    }
}