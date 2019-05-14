package itis.ru.kpfu.join.data.network.firebase.repository.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.functions.BiFunction
import io.reactivex.subjects.AsyncSubject
import itis.ru.kpfu.join.data.network.firebase.pojo.response.CreateDialogItem
import itis.ru.kpfu.join.data.network.firebase.repository.DialogListRepository
import itis.ru.kpfu.join.data.network.firebase.repository.MessagesRepository
import itis.ru.kpfu.join.data.network.joinapi.request.JoinApiRequest
import itis.ru.kpfu.join.db.entity.User
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.model.*
import java.lang.Exception
import javax.inject.Inject

class DialogListRepositoryImpl @Inject constructor() : DialogListRepository {

    @Inject
    lateinit var firebaseDB: FirebaseDatabase

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var messagesRepository: MessagesRepository

    @Inject
    lateinit var apiRequest: JoinApiRequest

    override fun createDialog(opponentId: String): Observable<CreatedDialogModel> {
        val asyncSubject = AsyncSubject.create<Pair<Boolean, String>>()

        val d = checkIfExistsChat(opponentId, userRepository.getUser()?.id.toString())
                .subscribe({ chatId ->
                    if (chatId.isEmpty()) {
                        val ref = firebaseDB.getReference("chats").push()
                        ref.setValue(CreateDialogItem(userRepository.getUser()?.id.toString(), opponentId)).addOnCompleteListener {
                            asyncSubject.onNext(Pair(true, ref.key.orEmpty()))
                            asyncSubject.onComplete()
                        }.addOnCanceledListener {
                            asyncSubject.onNext(Pair(false, ""))
                            asyncSubject.onComplete()
                        }
                    } else {
                        asyncSubject.onNext(Pair(true, chatId))
                        asyncSubject.onComplete()
                    }
                }, {
                    asyncSubject.onNext(Pair(false, ""))
                    asyncSubject.onComplete()
                })


        return asyncSubject.flatMap { (success, chatId) ->
            if (success) {
                apiRequest
                        .getUserInfo(userRepository.getUser()?.token.orEmpty(), opponentId.toLong())
                        .map { CreatedDialogModelMapper.map(chatId, it) }
            } else {
                Observable.error(Exception())
            }
        }
    }

    override fun getDialogs(): Observable<DialogModel> {
        return getAllDialogs().flatMapObservable {
            Observable.fromIterable(it)
                    .flatMap { dialog ->
                        Observable.zip(
                                messagesRepository.getLastMessage(dialog.id.orEmpty()),
                                apiRequest.getUserInfo(userRepository.getUser()?.token.orEmpty(), dialog.opponentId?.toLong()),
                                BiFunction<MessageModel, User, DialogModel> { t1, t2 ->
                                    DialogModelMapper.map(t1, t2, dialog.id.orEmpty())
                                })
                    }
        }
    }

    private fun checkIfExistsChat(userId: String, opponentId: String): Observable<String> {
        val asyncSubject = AsyncSubject.create<Pair<Boolean, String>>()

        firebaseDB
                .getReference("chats")
                .orderByChild("user_id")
                .equalTo(userId)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = mutableListOf<String>()

                        snapshot.children.forEach { ds ->
                            val item = ds.getValue(CreateDialogItem::class.java)
                            if (item?.user_id == opponentId) {
                                items.add(ds.key.orEmpty())
                            }
                        }
                        if (items.size == 0) {
                            asyncSubject.onNext(Pair(true, ""))
                        } else {
                            asyncSubject.onNext(Pair(true, items.first()))
                        }
                        asyncSubject.onComplete()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        asyncSubject.onNext(Pair(false, ""))
                        asyncSubject.onComplete()
                    }
                })

        return asyncSubject.flatMap { (success, chatId) ->
            if (success) {
                Observable.just(chatId)
            } else {
                Observable.error(Exception())
            }
        }
    }

    private fun getAllDialogs(): Single<List<CreateDialogModel>> {
        val asyncSubject = AsyncSubject.create<Pair<Boolean, List<CreateDialogModel>>>()

        val ref = firebaseDB.getReference("chats")

        ref
                .orderByChild("user_id")
                .equalTo(userRepository.getUser()?.id.toString())
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val items = mutableListOf<CreateDialogModel>()

                        snapshot.children.forEach { sp ->
                            val item = sp.getValue(CreateDialogItem::class.java)
                            item?.let { items.add(CreateDialogModel(sp.key, item.user_id, item.companion_id)) }
                        }

                        asyncSubject.onNext(Pair(true, items))
                        asyncSubject.onComplete()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        asyncSubject.onNext(Pair(false, emptyList()))
                        asyncSubject.onComplete()
                    }
                })

        return asyncSubject.singleOrError().flatMap { (success, items) ->
            if (success) {
                Single.just(items)
            } else {
                Single.error(Exception())
            }
        }
    }
}