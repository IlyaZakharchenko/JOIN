package itis.ru.kpfu.join

import android.content.Context
import android.support.multidex.MultiDex
import android.support.multidex.MultiDexApplication
import com.crashlytics.android.Crashlytics
import com.facebook.FacebookSdk
import com.vk.sdk.VKSdk
import io.fabric.sdk.android.Fabric
import io.realm.Realm
import io.realm.RealmConfiguration
import io.realm.rx.RealmObservableFactory
import itis.ru.kpfu.join.di.component.AppComponent
import itis.ru.kpfu.join.di.component.DaggerAppComponent
import itis.ru.kpfu.join.di.module.AppContextModule

class JoinApplication : MultiDexApplication() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        initComponent()
        initRealm()
        initFabric()
    }

    private fun initFabric() {
        val fabric = Fabric.Builder(this)
                .kits(Crashlytics())
                .debuggable(true)
                .build()
        Fabric.with(fabric)
    }

    private fun initComponent() {
        appComponent = DaggerAppComponent.builder()
                .appContextModule(AppContextModule(this))
                .build()
    }

    private fun initRealm() {
        Realm.init(this)
        val realmConfiguration = RealmConfiguration.Builder()
                .rxFactory(RealmObservableFactory())
                .schemaVersion(BuildConfig.VERSION_CODE.toLong())
                .deleteRealmIfMigrationNeeded()
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}