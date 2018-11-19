package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.db.entity.Dialog
import itis.ru.kpfu.join.db.entity.TextMessage
import itis.ru.kpfu.join.mvp.presenter.DialogsPresenter
import itis.ru.kpfu.join.mvp.presenter.FragmentHostPresenter
import itis.ru.kpfu.join.mvp.view.DialogsView
import itis.ru.kpfu.join.ui.activity.FragmentHostActivity
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_dialogs.rv_dialogs
import kotlinx.android.synthetic.main.fragment_dialogs.toolbar_dialogs
import ru.sovcombank.mok.ui.adapter.recyclerview.DialogsAdapter

class DialogsFragment : BaseFragment(), DialogsView {

    companion object {
        fun newInstance(): DialogsFragment {
            val args = Bundle()
            val fragment = DialogsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var mAdapter: DialogsAdapter

    override val contentLayout: Int
        get() = R.layout.fragment_dialogs

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
    lateinit var presenter: DialogsPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        mAdapter = DialogsAdapter(initDialogs()) { onChatClick(it) }
        rv_dialogs.adapter = mAdapter
        rv_dialogs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initDialogs(): List<Dialog> {
        return arrayListOf(
                Dialog("Project THE BEST", R.drawable.rx_logo,
                        TextMessage(text = "lastMessage", to = "staff", dateSend = "12:12")),
                Dialog("Project IPHONE 11", R.drawable.kotlin_logo,
                        TextMessage(text = "lastMessage", dateSend = "20:23")),
                Dialog("Project SMTH", R.drawable.icon_vk,
                        TextMessage(text = "lastMessage", to = "staff", dateSend = "19:12")),
                Dialog("Project HZ", R.drawable.icon_twitter,
                        TextMessage(text = "lastMessage", dateSend = "15:00")),
                Dialog("Project V", R.drawable.junit_logo,
                        TextMessage(text = "lastMessage", to = "staff", dateSend = "18:12"))
        ).shuffled()
    }

    private fun onChatClick(dialog: Dialog) {
        (activity as? FragmentHostActivity)?.setFragment(ChatFragment.newInstance(dialog), true)
    }
}