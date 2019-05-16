package itis.ru.kpfu.join.data.network.firebase.repository

import io.reactivex.Completable
import io.reactivex.Observable
import itis.ru.kpfu.join.presentation.model.AddMessageModel
import itis.ru.kpfu.join.presentation.model.DialogModel
import itis.ru.kpfu.join.presentation.model.MessageModel

interface MessagesRepository {

    fun getLastMessage(chatId: String): Observable<MessageModel>

    fun addMessage(addMessageModel: AddMessageModel): Completable
}