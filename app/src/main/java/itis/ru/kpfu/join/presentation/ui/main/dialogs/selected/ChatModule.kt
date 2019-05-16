package itis.ru.kpfu.join.presentation.ui.main.dialogs.selected

import android.support.v4.app.DialogFragment
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.SnapshotParser
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import itis.ru.kpfu.join.data.network.firebase.pojo.response.MessageItem
import itis.ru.kpfu.join.db.repository.UserRepository
import itis.ru.kpfu.join.presentation.adapter.DialogAdapter
import itis.ru.kpfu.join.presentation.model.MessageModel
import itis.ru.kpfu.join.presentation.model.MessageModelMapper
import javax.inject.Named

@Module
class ChatModule {

    @Provides
    fun getDialogCreatedDialog(fragment: ChatFragment) = fragment.getCreatedDialog()

    @Provides
    fun provideAdapterOptions(
            fragment: ChatFragment,
            firebaseDb: FirebaseDatabase,
            userRepository: UserRepository
    ): FirebaseRecyclerOptions<MessageModel> {
        val parser = SnapshotParser { snapshot ->
            val item = snapshot.getValue(MessageItem::class.java) ?: MessageItem()
            MessageModelMapper.map(item, userRepository.getUser()?.id ?: -1L)
        }

        val ref = firebaseDb.getReference("messages")
                .child(fragment.getCreatedDialog().dialogId.orEmpty())
                .orderByChild("created_date")

        return FirebaseRecyclerOptions.Builder<MessageModel>()
                .setQuery(ref, parser)
                .build()
    }

    @Provides
    fun provideAdapter(options: FirebaseRecyclerOptions<MessageModel>) = DialogAdapter(options)
}