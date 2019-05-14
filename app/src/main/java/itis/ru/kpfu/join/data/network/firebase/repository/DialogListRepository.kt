package itis.ru.kpfu.join.data.network.firebase.repository

import io.reactivex.Observable
import itis.ru.kpfu.join.presentation.model.CreatedDialogModel
import itis.ru.kpfu.join.presentation.model.DialogModel

interface DialogListRepository {

    fun createDialog(opponentId: String): Observable<CreatedDialogModel>

    fun getDialogs(): Observable<DialogModel>
}