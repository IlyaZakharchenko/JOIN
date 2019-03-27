package itis.ru.kpfu.join.presentation.base

import android.content.Context
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.app.Fragment
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import com.arellomobile.mvp.MvpAppCompatActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import itis.ru.kpfu.join.presentation.dialog.ConfirmationDialog
import itis.ru.kpfu.join.presentation.dialog.ErrorDialog
import itis.ru.kpfu.join.presentation.dialog.WaitDialog
import javax.inject.Inject

abstract class BaseActivity : MvpAppCompatActivity(), HasSupportFragmentInjector, BaseView {

    companion object {
        private const val EXIT_DIALOG_REQUEST_CODE = 2
        private const val TAG_EXIT_DIALOG = "onLogout dialog tag"
        private const val TAG_ERROR_DIALOG = "tag error dialog"
        private const val TAG_WAIT_DIALOG = "tag wait dialog"
    }

    protected abstract val contentLayout: Int

    protected abstract val menu: Int?

    protected abstract val toolbarTitle: Int?

    protected abstract val enableBackPressed: Boolean

    protected abstract val toolbar: Toolbar?

    @field:Inject
    lateinit var dispatchingAndroidInjector: DispatchingAndroidInjector<Fragment>

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return dispatchingAndroidInjector
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)

        setToolbar(toolbar)
        enableBackPressed(enableBackPressed)
        setToolbarTitle(toolbarTitle)
    }

    fun enableBackPressed(enable: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        supportActionBar?.setDisplayShowHomeEnabled(enable)
    }

    fun setToolbarTitle(title: Int?) {
        supportActionBar?.title = title?.let { getString(it) }
    }

    fun setToolbarTitle(title: String) {
        supportActionBar?.title = title
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu?.let {
            menuInflater.inflate(it, menu)
            return true
        }
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (supportFragmentManager.backStackEntryCount == 0) {
            ConfirmationDialog.getInstance(EXIT_DIALOG_REQUEST_CODE).show(supportFragmentManager, TAG_EXIT_DIALOG)
        } else {
            super.onBackPressed()
        }
    }

    fun setToolbar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
    }

    override fun showErrorDialog(text: String) {
        ErrorDialog.getInstance(text).show(supportFragmentManager, TAG_ERROR_DIALOG)
    }

    override fun showErrorDialog(text: Int) {
        ErrorDialog.getInstance(getString(text)).show(supportFragmentManager, TAG_ERROR_DIALOG)
    }

    override fun hideWaitDialog() {
        (supportFragmentManager.findFragmentByTag(TAG_WAIT_DIALOG) as? DialogFragment)?.dismiss()
    }

    override fun showWaitDialog() {
        WaitDialog.getInstance().show(supportFragmentManager, TAG_WAIT_DIALOG)
    }

    override fun hideKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        imm?.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }
}