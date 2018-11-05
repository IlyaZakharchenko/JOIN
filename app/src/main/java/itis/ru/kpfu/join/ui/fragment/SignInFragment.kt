package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.os.Bundle
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
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.view.SignInView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.btn_create_account
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_facebook
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_google
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_vk
import kotlinx.android.synthetic.main.fragment_sign_up.btn_sign_in

class SignInFragment : BaseFragment(), SignInView {
    @InjectPresenter
    lateinit var presenter: SignInPresenter

    lateinit var callbackManager: CallbackManager

    companion object {

        const val GOOGLE_SIGN_IN = 0

        const val FACEBOOK_SIGN_IN = 1
        const val VK_SIGN_IN = 2
        fun newInstance(): SignInFragment {
            val args = Bundle()
            val fragment = SignInFragment()
            fragment.arguments = args
            return fragment
        }
    }
    @ProvidePresenter
    fun providePresenter(): SignInPresenter {
        return JoinApplication.appComponent.provideSignInPresenter()
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //(activity as FragmentHostActivity).hideToolbar()
        initFacebookSignIn()
        initGoogleSignIn()
        initVkSignIn()
        initClickListeners()
        // presenter.getDataFromServer(api, testRepository)
    }

    override fun initClickListeners() {
        btn_create_account.setOnClickListener { onCreateAccountClick() }
        btn_sign_in.setOnClickListener { presenter.getDataFromServer()}
    }

    private fun initFacebookSignIn() {
        btn_sign_in_facebook.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(this, arrayListOf("public_profile"))
        }
        callbackManager = CallbackManager.Factory.create()

        LoginManager.getInstance().registerCallback(callbackManager,
                object : FacebookCallback<LoginResult> {
                    override fun onSuccess(result: LoginResult?) {
                        presenter.getFacebookUserInfo(result, userRepository)
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
                presenter.getGoogleUserInfo(account, userRepository)
            } catch (e: ApiException) {
                Toast.makeText(baseActivity, "Authorization via Google failed", Toast.LENGTH_SHORT).show()
            }
        } else if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken?) {
                        presenter.getVkUserInfo(res, userRepository)
                    }

                    override fun onError(error: VKError?) {
                        Toast.makeText(baseActivity, "Authorization via VK failed", Toast.LENGTH_SHORT).show()
                    }
                }))

            callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun showProgress() {
        (activity as? BaseActivity)?.showProgressBar()
    }

    override fun hideProgress() {
        (activity as? BaseActivity)?.hideProgressBar()
    }

    override fun onConnectionError() {
        Toast.makeText(activity, "Internet Connection Error", Toast.LENGTH_SHORT).show()
    }

    override fun signIn() {
        (activity as FragmentHostActivity).setFragment(ProjectsFragment.newInstance(), false)
    }

    override fun showResult(result: String) {
         Toast.makeText(baseActivity, "Success", Toast.LENGTH_LONG).show()
    }

    override fun onCreateAccountClick() {
        presenter.onCreateAccountClick()
    }

    override fun openSignUpFragment() {
        (activity as? FragmentHostActivity)?.setFragment(SignUpFragment.newInstance(), true)
    }
}
