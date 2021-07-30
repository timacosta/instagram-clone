package io.keepcoding.instagram_clone

import android.app.Application
import io.keepcoding.instagram_clone.di.NetworkDIModule
import org.kodein.di.DI
import org.kodein.di.DIAware

class App: Application(), DIAware {
    override val di: DI by DI.lazy {
        import(NetworkDIModule.create())
    }
}