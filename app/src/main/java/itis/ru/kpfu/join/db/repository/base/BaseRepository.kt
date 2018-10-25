package itis.ru.kpfu.join.db.repository.base

import android.util.Log
import io.realm.Realm
import io.realm.RealmObject

abstract class BaseRepository {

    protected val realm: Realm get() = Realm.getDefaultInstance()

    protected fun nextKey(c: Class<out RealmObject>): Long {
        val maxId = realm.where(c).max("id")
        return if (maxId == null) 1 else maxId.toLong() + 1
    }

    protected fun executeTransaction(transaction: Realm.Transaction) {
        var realm: Realm? = null
        try {
            realm = Realm.getDefaultInstance()
            realm.executeTransaction(transaction)
        } catch (e: Throwable) {
            Log.e("Realm", e.message)
        } finally {
            realm?.close()
        }
    }
}