package itis.ru.kpfu.join.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKScope
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import com.vk.sdk.util.VKUtil
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.TestApi
import itis.ru.kpfu.join.db.repository.impl.TestRepositoryImpl
import itis.ru.kpfu.join.mvp.presenter.SignInPresenter
import itis.ru.kpfu.join.mvp.view.SignInView
import itis.ru.kpfu.join.ui.activity.MainActivity
import itis.ru.kpfu.join.ui.activity.base.BaseActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_google
import kotlinx.android.synthetic.main.fragment_sign_in.btn_sign_in_vk
import java.util.Arrays
import javax.inject.Inject
import kotlin.concurrent.timer

class SignInFragment : BaseFragment(), SignInView {
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
    @InjectPresenter
    lateinit var presenter: SignInPresenter

    @Inject
    lateinit var api: TestApi

    @Inject
    lateinit var testRepository: TestRepositoryImpl

    override val contentLayout: Int
        get() = R.layout.fragment_sign_in

    override val toolbarTitle: Int?
        get() = R.string.sign_in

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        JoinApplication.appComponent.inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isSignedInViaGoogle() || isSignedInViaVk()) {
            signIn()
        } else {
            initGoogleSignIn()
            initVkSignIn()
            presenter.getDataFromServer(api, testRepository)
        }
    }

    private fun isSignedInViaVk(): Boolean {
        return VKSdk.isLoggedIn()
                && VKAccessToken.currentToken() != null
                && !VKAccessToken.currentToken().isExpired
    }

    private fun isSignedInViaGoogle(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(baseActivity) != null
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
                //Open MainFragment
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
        (activity as MainActivity).setFragment(MainFragment.newInstance(), false)
    }

    override fun showResult(result: String) {
       // tv_test.text = result
    }

}
