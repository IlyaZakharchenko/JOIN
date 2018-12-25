package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.api.model.Dialog
import itis.ru.kpfu.join.api.model.TextMessage
import itis.ru.kpfu.join.mvp.presenter.ChatPresenter
import itis.ru.kpfu.join.mvp.view.ChatView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import itis.ru.kpfu.join.utils.Constants
import kotlinx.android.synthetic.main.fragment_chat.rv_messages
import kotlinx.android.synthetic.main.fragment_chat.toolbar_chat
import ru.sovcombank.mok.ui.adapter.recyclerview.MessagesAdapter

class ChatFragment : BaseFragment(), ChatView {

    companion object {
        fun newInstance(dialog: Dialog): ChatFragment {
            val args = Bundle()
            args.putSerializable(Constants.DIALOG, dialog)

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

    private var adapter: MessagesAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialog = arguments?.getSerializable(Constants.DIALOG) as Dialog
        dialog.dialogName?.let { (activity as? FragmentHostActivity)?.setToolbarTitle(it) }

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter = MessagesAdapter(initMessages())
        rv_messages.adapter = adapter
        rv_messages.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initMessages(): List<TextMessage> {
        return arrayListOf(
                TextMessage("Привет", "11:12", "staff", null),
                TextMessage("Привет", "11:12", null, "staff"),
                TextMessage(null, "Вчера", "staff", null),
                TextMessage("Как дела?", "11:12", "staff", null),
                TextMessage("Норм?", "11:12", null, "staff")
        )
    }
}