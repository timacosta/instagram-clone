package io.keepcoding.instagram_clone

fun Boolean.alsoIfTrue(cb: () -> Unit) {
    if(this) cb
}