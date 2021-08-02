package io.keepcoding.instagram_clone.session

interface SessionRepository {
    fun getSession(): Session?
    fun saveSession(session: Session)
}