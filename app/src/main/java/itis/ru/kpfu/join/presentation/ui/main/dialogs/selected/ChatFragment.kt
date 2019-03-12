package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.presentation.model.DialogModel
import itis.ru.kpfu.join.presentation.model.TextMessageModel
import itis.ru.kpfu.join.presentation.adapter.MessagesAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_chat.rv_messages
import kotlinx.android.synthetic.main.fragment_chat.toolbar_chat
import javax.inject.Inject
import javax.inject.Provider

class ChatFragment : BaseFragment(), ChatView {

    companion object {
        private const val DIALOG = "dialog"

        fun newInstance(dialog: DialogModel): ChatFragment {
            val args = Bundle()
            args.putSerializable(DIALOG, dialog)

            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
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
    lateinit var adapter: MessagesAdapter

    @ProvidePresenter
    fun providePresenter(): ChatPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = arguments?.getSerializable(DIALOG) as DialogModel
        dialog.dialogName?.let { (activity as? FragmentHostActivity)?.setToolbarTitle(it) }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.items = initMessages()
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initMessages(): List<TextMessageModel> {
        return arrayListOf(
                TextMessageModel("Привет", "11:12", "staff", null),
                TextMessageModel("Привет", "11:12", null, "staff"),
                TextMessageModel(null, "Вчера", "staff", null),
                TextMessageModel("Как дела?", "11:12", "staff", null),
                TextMessageModel("Норм?", "11:12", null, "staff")
        )
    }
}