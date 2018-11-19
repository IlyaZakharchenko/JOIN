package itis.ru.kpfu.join.ui.activity.base

import android.app.DialogFragment
import android.content.Context
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CollapsingToolbarLayout
import android.support.design.widget.CoordinatorLayout
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import android.widget.ProgressBar
import com.afollestad.materialdialogs.MaterialDialog
import com.arellomobile.mvp.MvpAppCompatActivity
import com.facebook.appevents.codeless.ViewIndexer
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.R
import kotlinx.android.synthetic.main.fragment_progress.view.progress_dialog_progress

abstract class BaseActivity : MvpAppCompatActivity() {

    protected abstract val contentLayout: Int

    protected abstract val menu: Int?

    protected abstract val toolbarTitle: Int?

    protected abstract val enableBackPressed: Boolean

    protected abstract val toolbar: Toolbar?

    lateinit var exitDialog: MaterialDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)

        setToolbar(toolbar)
        enableBackPressed(enableBackPressed)
        setToolbarTitle(toolbarTitle)
        initExitDialog()
    }

    private fun initExitDialog() {
        exitDialog = MaterialDialog.Builder(this)
                .title("Вы действительно хотите выйти?")
                .positiveText("Да")
                .negativeText("Нет")
                .onPositive { dialog, which -> finish() }
                .onNegative { dialog, which -> dialog.dismiss() }
                .build()
    }

    fun enableBackPressed(enable: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        supportActionBar?.setDisplayShowHomeEnabled(enable)
    }

    fun setToolbarTitle(title: Int?) {
        supportActionBar?.title = title?.let { getString(it) }
    }

    fun setToolbarTitle(title: String){
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
            exitDialog.show()
        } else {
            super.onBackPressed()
        }
    }

    fun setToolbar(toolbar: Toolbar?) {
        setSupportActionBar(toolbar)
    }
}