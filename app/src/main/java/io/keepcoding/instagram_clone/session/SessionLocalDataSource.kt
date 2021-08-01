package io.keepcoding.instagram_clone.session

import android.content.SharedPreferences

class SessionLocalDataSource(val sharedPreferences: SharedPreferences) {

    fun saveSession(session: Session) {
        sharedPreferences.edit().putString("ACCESS TOKEN", session.accessToken).apply()
        sharedPreferences.edit().putString("ACCOUNT NAME", session.accountName).apply()
    }

    fun retrieveSession() {
        val accessToken = sharedPreferences.getString("ACCESS TOKEN", null)
        val accountName = sharedPreferences.getString("ACCOUNT NAME", null)

        if(accessToken != null && accountName != null) {
            Session(
                accessToken,
                accountName
            )
        }


    }



}