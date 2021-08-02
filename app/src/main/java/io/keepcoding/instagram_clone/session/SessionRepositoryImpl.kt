package io.keepcoding.instagram_clone.session

class SessionRepositoryImpl(
    val memoryDataSource: SessionMemoryDataSource,
    val localDataSource: SessionLocalDataSource
): SessionRepository {
    override fun getSession(): Session? {
        return memoryDataSource.session
            ?: localDataSource.retrieveSession().also { memoryDataSource.session = it }
    }

    override fun saveSession(session: Session) {
        memoryDataSource.session = session
        localDataSource.saveSession(session)
    }

}