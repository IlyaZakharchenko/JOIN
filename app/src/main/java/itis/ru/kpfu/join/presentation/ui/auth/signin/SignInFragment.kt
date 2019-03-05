package itis.ru.kpfu.join.presentation.ui.auth.signin

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.main.projects.all.AllProjectsFragment
import itis.ru.kpfu.join.presentation.ui.auth.restorepassword.RestorePassFragment
import itis.ru.kpfu.join.presentation.ui.auth.signup.stepone.SignUpStepOneFragment
import kotlinx.android.synthetic.main.fragment_sign_in.btn_create_account
import kotlinx.android.synthetic.main.fragment_sign_in.btn_forgot_pass
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_facebook
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_google
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_vk
import kotlinx.android.synthetic.main.fragment_sign_in.et_email
import kotlinx.android.synthetic.main.fragment_sign_in.et_password
import kotlinx.android.synthetic.main.fragment_sign_in.toolbar_sign_in
import javax.inject.Inject
import javax.inject.Provider

class SignInFragment : BaseFragment(), SignInView {

    companion object {

        const val GOOGLE_SIGN_IN = 0

        fun newInstance(): SignInFragment {
            val args = Bundle()
            val fragment = SignInFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_sign_in

    override val toolbarTitle: Int?
        get() = R.string.sign_in

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_sign_in

    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @Inject
    lateinit var presenterProvider: Provider<SignInPresenter>

    lateinit var callbackManager: CallbackManager

    @ProvidePresenter
    fun providePresenter(): SignInPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFacebookSignIn()
        initGoogleSignIn()
        initVkSignIn()
        initClickListeners()
    }

    override fun initClickListeners() {
        btn_create_account.setOnClickListener { presenter.onCreateAccountClick() }
        btn_sign_in.setOnClickListener {
            presenter.signIn(et_email.text.toString(), et_password.text.toString())
        }
        btn_forgot_pass.setOnClickListener {
            (activity as? FragmentHostActivity)?.setFragment(RestorePassFragment.newInstance(), true)
        }
    }

    private fun initFacebookSignIn() {
        btn_sign_in_facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("public_profile"))
        }
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        presenter.getFacebookUserInfo(result)
                    }

                    override fun onCancel() {
                    }

                    override fun onError(error: FacebookException?) {
                        Toast.makeText(baseActivity, "Authorization via Facebook failed", Toast.LENGTH_SHORT).show()
                    }
                })
    }

    private fun initVkSignIn() {
        btn_sign_in_vk.setOnClickListener {
            val intent = Intent(activity, VKServiceActivity::class.java)
            intent.putExtra("arg1", "Authorization")
            intent.putStringArrayListExtra("arg2", arrayListOf(VKScope.OFFLINE))
            intent.putExtra("arg4", VKSdk.isCustomInitialize())
            startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.getOuterCode())
        }
    }

    private fun initGoogleSignIn() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val googleSignInClient = GoogleSignIn.getClient(baseActivity, gso)

        btn_sign_in_google.setOnClickListener {
            startActivityForResult(googleSignInClient.signInIntent, GOOGLE_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                presenter.getGoogleUserInfo(account)
            } catch (e: ApiException) {
                Toast.makeText(baseActivity, "Authorization via Google failed", Toast.LENGTH_SHORT).show()
            }
        } else if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken?) {
                        presenter.getVkUserInfo(res)
                    }

                    override fun onError(error: VKError?) {
                        Toast.makeText(baseActivity, "Authorization via VK failed", Toast.LENGTH_SHORT).show()
                    }
                }))

            callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showProgress() {
        showProgressBar()
    }

    override fun hideProgress() {
        hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun signIn() {
        (activity as FragmentHostActivity).setFragment(AllProjectsFragment.newInstance(), false)
    }

    override fun showResult(result: String) {
        Toast.makeText(baseActivity, result, Toast.LENGTH_LONG).show()
    }

    override fun openSignUpFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignUpStepOneFragment.newInstance(), true)
    }

    override fun onSignInError() {
        Snackbar.make(btn_create_account, "Неверные данные авторизации", Snackbar.LENGTH_LONG).show()
    }
}
