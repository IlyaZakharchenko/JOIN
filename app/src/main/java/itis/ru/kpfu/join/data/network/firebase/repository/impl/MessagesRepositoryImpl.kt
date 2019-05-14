package itis.ru.kpfu.join.data.network.firebase.repository.impl

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.subjects.AsyncSubject
import itis.ru.kpfu.join.data.network.firebase.pojo.response.MessageItem
import itis.ru.kpfu.join.data.network.firebase.repository.MessagesRepository
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.model.AddMessageModel
import itis.ru.kpfu.join.presentation.model.MessageModel
import itis.ru.kpfu.join.presentation.model.MessageModelMapper
import java.lang.Exception
import javax.inject.Inject

class MessagesRepositoryImpl @Inject constructor() : MessagesRepository {

    @Inject
    lateinit var userRepository: UserRepository

    @Inject
    lateinit var firebaseDB: FirebaseDatabase

    override fun addMessage(addMessageModel: AddMessageModel): Completable {
        val asyncSubject = AsyncSubject.create<Boolean>()

        val ref = firebaseDB.getReference("messages")
        ref.child(addMessageModel.chatId.orEmpty()).push().setValue(
                MessageItem(
                        addMessageModel.content,
                        System.currentTimeMillis(),
                        userRepository.getUser()?.id.toString()
                )
        ).addOnCompleteListener {
            asyncSubject.onNext(it.isSuccessful)
            asyncSubject.onComplete()
        }.addOnCanceledListener {
            asyncSubject.onNext(false)
            asyncSubject.onComplete()
        }

        return asyncSubject.flatMapCompletable { success ->
            if (success) {
                Completable.complete()
            } else {
                Completable.error(Exception())
            }
        }
    }

    override fun getLastMessage(chatId: String): Observable<MessageModel> {
        val asyncSubject = AsyncSubject.create<Pair<Boolean, MessageItem>>()

        val ref = firebaseDB.getReference("messages")

        ref
                .child(chatId)
                .orderByChild("created_date")
                .limitToLast(1)
                .addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val sp = snapshot.children.lastOrNull()
                        val item = sp?.getValue(MessageItem::class.java)

                        asyncSubject.onNext(Pair(true, item ?: MessageItem()))
                        asyncSubject.onComplete()
                    }

                    override fun onCancelled(p0: DatabaseError) {
                        asyncSubject.onNext(Pair(false, MessageItem()))
                        asyncSubject.onComplete()
                    }
                })

        return asyncSubject.flatMap { (success, message) ->
            if (success) {
                Observable.just(message).map {
                    MessageModelMapper.map(it, userRepository.getUser()?.id ?: -1L)
                }
            } else {
                Observable.error(Exception())
            }
        }
    }
}