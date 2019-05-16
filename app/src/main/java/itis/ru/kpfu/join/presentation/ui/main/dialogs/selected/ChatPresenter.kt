package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import com.arellomobile.mvp.InjectViewState
import itis.ru.kpfu.join.data.network.firebase.repository.MessagesRepository
import itis.ru.kpfu.join.presentation.base.BasePresenter
import itis.ru.kpfu.join.presentation.model.AddMessageModel
import itis.ru.kpfu.join.presentation.model.CreatedDialogModel
import javax.inject.Inject

@InjectViewState
class ChatPresenter @Inject constructor(): BasePresenter<ChatView>() {

    @Inject
    lateinit var dialog: CreatedDialogModel

    @Inject
    lateinit var messagesRepository: MessagesRepository

    private var message: String? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        viewState.setToolbarTitle(dialog.companionName.orEmpty())
        viewState.showProgress()
    }

    override fun attachView(view: ChatView?) {
        super.attachView(view)
        viewState.startListeningAdapter()
    }

    override fun detachView(view: ChatView) {
        super.detachView(view)
        viewState.stopListeningAdapter()
    }

    fun onAddMessage() {
        messagesRepository
                .addMessage(AddMessageModel(message, dialog.dialogId))
                .doOnSubscribe { viewState.clearMessageField() }
                .subscribe({
                    message = null
                }, {
                    viewState.showErrorDialog("Ошибка при отправке сообщения")
                })
                .disposeWhenDestroy()
    }

    fun onMessageChange(message: String) {
        this.message = if (message.isEmpty()) null else message
        checkButtonState()
    }

    private fun checkButtonState() {
        viewState.setButtonEnabled(message != null)
    }

    fun onDataChange(items: MutableList<String>) {
        viewState.setItemDecorationItems(items)
        viewState.hideProgress()
    }
}