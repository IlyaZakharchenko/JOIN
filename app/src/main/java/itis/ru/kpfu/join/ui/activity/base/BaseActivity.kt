package itis.ru.kpfu.join.ui.activity.base

import android.app.DialogFragment
import android.os.Bundle
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import com.arellomobile.mvp.MvpAppCompatActivity
import itis.ru.kpfu.join.JoinApplication
import itis.ru.kpfu.join.ui.fragment.ProgressDialogFragment

abstract class BaseActivity: MvpAppCompatActivity() {

    protected abstract val contentLayout: Int

    protected abstract val menu: Int?

    protected abstract val toolbarTitle: Int?

    protected abstract val enableBackPressed: Boolean

    protected abstract val toolbar: Toolbar?

    private lateinit var dialog: DialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentLayout)

        toolbar?.let { setSupportActionBar(it) }
        enableBackPressed(enableBackPressed)
        setToolbarTitle(toolbarTitle)
        dialog = ProgressDialogFragment.newInstance()
    }

    fun enableBackPressed(enable: Boolean) {
        supportActionBar?.setDisplayHomeAsUpEnabled(enable)
        supportActionBar?.setDisplayShowHomeEnabled(enable)
    }

    fun setToolbarTitle(title: Int?) {
        supportActionBar?.title = title?.let { getString(it) }
    }

    fun showProgressBar() {
        dialog.show(fragmentManager, null)
    }

    fun hideProgressBar() {
        dialog.dismiss()
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
}