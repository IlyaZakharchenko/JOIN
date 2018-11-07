package itis.ru.kpfu.join.ui.fragment

import android.os.Bundle
import android.view.View
import com.arellomobile.mvp.presenter.InjectPresenter
import itis.ru.kpfu.join.R
import itis.ru.kpfu.join.mvp.presenter.ChatPresenter
import itis.ru.kpfu.join.mvp.view.ChatView
import itis.ru.kpfu.join.ui.fragment.base.BaseFragment

class ChatFragment: BaseFragment(), ChatView {

    companion object {
        fun newInstance(): ChatFragment {
            val args = Bundle()
            val fragment = ChatFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override val contentLayout: Int
        get() = R.layout.fragment_chat

    override val toolbarTitle: Int?
        get() = R.string.chat_toolbar

    override val menu: Int?
        get() = null

    override val enableBackPressed: Boolean
        get() = false

    override val enableBottomNavBar: Boolean
        get() = true

    @InjectPresenter
    lateinit var presenter: ChatPresenter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}