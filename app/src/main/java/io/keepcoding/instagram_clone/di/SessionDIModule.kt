package io.keepcoding.instagram_clone.di

import io.keepcoding.instagram_clone.session.SessionLocalDataSource
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

object SessionDIModule: DIBaseModule("SessionDIModule") {
    override val builder: DI.Builder.() -> Unit = {
        bind<SessionLocalDataSource>() with singleton {
            SessionLocalDataSource((instance()))
        }
    }
}