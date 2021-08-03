package io.keepcoding.instagram_clone

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector

fun Boolean.alsoIfTrue(cb: () -> Unit) {
    if(this) cb
}

