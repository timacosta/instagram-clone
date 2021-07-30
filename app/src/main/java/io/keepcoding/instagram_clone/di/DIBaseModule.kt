package io.keepcoding.instagram_clone.di

import android.app.Application
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton

abstract class DIBaseModule(val name: String) {
    fun create () = DI.Module(name = name,allowSilentOverride = false, init = builder)
    abstract val builder: DI.Builder.() -> Unit
}