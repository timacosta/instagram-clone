package io.keepcoding.instagram_clone

import android.app.Application
import io.keepcoding.instagram_clone.di.*
import org.kodein.di.DI
import org.kodein.di.DIAware
import timber.log.Timber

class App: Application(), DIAware {
    override val di: DI by DI.lazy {
        import(AppDIModule(application = this@App).create())
        import(NetworkDIModule.create())
        import(ViewModelDIModule.create())
        import(SessionDIModule.create())
        import(GalleryDIModule.create())
        import(RoomDIModule.create())
    }

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}