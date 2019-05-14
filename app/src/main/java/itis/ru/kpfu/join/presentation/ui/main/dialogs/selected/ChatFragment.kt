package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.WindowManager
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.adapter.DialogAdapter
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.model.CreatedDialogModel
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.util.addTextChangedListener
import itis.ru.kpfu.join.presentation.util.itemdecoration.DialogItemDecoration
import kotlinx.android.synthetic.main.fragment_chat.*
import java.lang.IllegalArgumentException
import javax.inject.Inject
import javax.inject.Provider

class ChatFragment : BaseFragment(), ChatView {

    companion object {
        private const val KEY_CREATED_DIALOG = "KEY_CREATED_DIALOG"

        fun newInstance(dialog: CreatedDialogModel) = ChatFragment().also {
            it.arguments = Bundle().apply {
                putSerializable(KEY_CREATED_DIALOG, dialog)
            }
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_chat

    override val toolbarTitle: Int?
        get() = R.string.toolbar_title_empty

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = true

    override val enableBottomNavBar: Boolean
        get() = false

    override val toolbar: Toolbar?
        get() = toolbar_chat

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    @Inject
    lateinit var presenterProvider: Provider<ChatPresenter>

    @Inject
    lateinit var adapter: DialogAdapter

    private val itemDecoration = DialogItemDecoration()

    private var adapterObserver: RecyclerView.AdapterDataObserver? = null

    @ProvidePresenter
    fun providePresenter(): ChatPresenter = presenterProvider.get()

    fun getCreatedDialog() = (arguments?.getSerializable(KEY_CREATED_DIALOG) as? CreatedDialogModel)
            ?: throw IllegalArgumentException("chat id is null")

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as? FragmentHostActivity)?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)

        btn_send_msg.setOnClickListener { presenter.onAddMessage() }

        et_message_text.addTextChangedListener { presenter.onMessageChange(it.trim()) }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(requireContext()).also { it.stackFromEnd = true }

        adapterObserver = object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
                val friendlyMessageCount = adapter.itemCount
                val lastVisiblePosition = layoutManager.findLastCompletelyVisibleItemPosition()
                // If the recycler view is initially being loaded or the user is at the bottom of the list, scroll
                // to the bottom of the list to show the newly added message.
                if (lastVisiblePosition == -1 || positionStart >= friendlyMessageCount - 1 && lastVisiblePosition == positionStart - 1) {
                    rv_messages.scrollToPosition(positionStart)
                }
            }
        }
        adapterObserver?.let { adapter.registerAdapterDataObserver(it) }

        rv_messages.adapter = adapter.also {
            it.onDataChangeListener = { items ->
                presenter.onDataChange(items)
            }
        }
        rv_messages.layoutManager = layoutManager
        rv_messages.addItemDecoration(itemDecoration)
    }

    override fun onDestroyView() {
        adapterObserver?.let { adapter.unregisterAdapterDataObserver(it) }
        super.onDestroyView()
    }

    override fun showProgress() {
        progress.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progress.visibility = View.GONE
    }

    override fun startListeningAdapter() {
        adapter.startListening()
    }

    override fun stopListeningAdapter() {
        adapter.stopListening()
    }

    override fun clearMessageField() {
        et_message_text.text = null
    }

    override fun setButtonEnabled(enabled: Boolean) {
        btn_send_msg.isEnabled = enabled
    }

    override fun setItemDecorationItems(items: MutableList<String>) {
        itemDecoration.data = items
    }

    override fun setToolbarTitle(title: String) {
        (activity as? FragmentHostActivity)?.setToolbarTitle(title)
    }
}