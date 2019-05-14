package itis.ru.kpfu.join.presentation.ui.main.dialogs

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.adapter.DialogListAdapter
import itis.ru.kpfu.join.presentation.model.DialogModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.model.CreatedDialogModel
import itis.ru.kpfu.join.presentation.ui.main.dialogs.selected.ChatFragment
import kotlinx.android.synthetic.main.fragment_dialog_list.*
import kotlinx.android.synthetic.main.layout_progress_error.*
import javax.inject.Inject
import javax.inject.Provider

class DialogListFragment : BaseFragment(), DialogListView {

    companion object {
        fun newInstance(): DialogListFragment {
            val args = Bundle()
            val fragment = DialogListFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_dialog_list

    override val toolbarTitle: Int?
        get() = R.string.chat_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    override val toolbar: Toolbar?
        get() = toolbar_dialogs

    @InjectPresenter
    lateinit var presenter: DialogListPresenter

    @Inject
    lateinit var presenterProvider: Provider<DialogListPresenter>

    @Inject
    lateinit var adapter: DialogListAdapter

    @ProvidePresenter
    fun providePresenter(): DialogListPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onChatClick = { onChatClick(it) }
        rv_dialogs.adapter = adapter
        rv_dialogs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun onChatClick(dialog: DialogModel) {
        (activity as? FragmentHostActivity)?.setFragment(ChatFragment.newInstance(
                CreatedDialogModel(dialog.dialogId, dialog.username)),
                true
        )
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun showRetry(errorText: String) {
        progress_error.visibility = View.VISIBLE

        retry_title.text = errorText
        btn_retry.setOnClickListener { presenter.onRetry() }
    }

    override fun hideRetry() {
        progress_error.visibility = View.GONE
    }

    override fun setDialogs(dialogs: List<DialogModel>) {
        adapter.items = dialogs
    }
}