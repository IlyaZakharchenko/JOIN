package itis.ru.kpfu.join.presentation.ui.main.dialogs

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.Toolbar
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.ProvidePresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.R.drawable
import itis.ru.kpfu.join.presentation.model.DialogModel
import itis.ru.kpfu.join.presentation.model.TextMessageModel
import itis.ru.kpfu.join.presentation.adapter.DialogsAdapter
import itis.ru.kpfu.join.presentation.ui.FragmentHostActivity
import itis.ru.kpfu.join.presentation.base.BaseFragment
import itis.ru.kpfu.join.presentation.ui.main.dialogs.selected.ChatFragment
import kotlinx.android.synthetic.main.fragment_dialogs.rv_dialogs
import kotlinx.android.synthetic.main.fragment_dialogs.toolbar_dialogs
import javax.inject.Inject
import javax.inject.Provider

class DialogsFragment : BaseFragment(), DialogsView {

    companion object {
        fun newInstance(): DialogsFragment {
            val args = Bundle()
            val fragment = DialogsFragment()
            fragment.arguments = args
            return fragment
        }
    }

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

    @Inject
    lateinit var presenterProvider: Provider<DialogsPresenter>

    @Inject
    lateinit var adapter: DialogsAdapter

    @ProvidePresenter
    fun providePresenter(): DialogsPresenter = presenterProvider.get()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
    }

    private fun initRecyclerView() {
        adapter.onChatClick = { onChatClick(it) }
        adapter.items = initDialogs()
        rv_dialogs.adapter = adapter
        rv_dialogs.layoutManager = LinearLayoutManager(baseActivity)
    }

    private fun initDialogs(): List<DialogModel> {
        return arrayListOf(
                DialogModel("ProjectModel THE BEST", drawable.rx_logo,
                        TextMessageModel(text = "lastMessage", to = "staff",
                                dateSend = "12:12")),
                DialogModel("ProjectModel IPHONE 11", drawable.kotlin_logo,
                        TextMessageModel(text = "lastMessage", dateSend = "20:23")),
                DialogModel("ProjectModel SMTH", drawable.icon_vk,
                        TextMessageModel(text = "lastMessage", to = "staff",
                                dateSend = "19:12")),
                DialogModel("ProjectModel HZ", drawable.icon_twitter,
                        TextMessageModel(text = "lastMessage", dateSend = "15:00")),
                DialogModel("ProjectModel V", drawable.junit_logo,
                        TextMessageModel(text = "lastMessage", to = "staff",
                                dateSend = "18:12"))
        ).shuffled()
    }

    private fun onChatClick(dialog: DialogModel) {
        (activity as? FragmentHostActivity)?.setFragment(ChatFragment.newInstance(dialog), true)
    }
}