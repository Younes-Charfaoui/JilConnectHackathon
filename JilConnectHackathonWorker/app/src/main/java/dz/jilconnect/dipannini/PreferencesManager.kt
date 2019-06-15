package dz.jilconnect.dipannini

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

class PreferencesManager @SuppressLint("CommitPrefEdits")
constructor(context: Context) {

    private val preferences: SharedPreferences
    private val editor: SharedPreferences.Editor

    init {
        preferences = context.getSharedPreferences(PREFERENCE_CONFIGURATION_NAME, PRIVATE_MODE)
        editor = preferences.edit()
    }

    var userId: String
        get() = preferences.getString(USER_ID, "") ?: ""
        set(value) {
            editor.putString(USER_ID, value).commit()
        }

    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREFERENCE_CONFIGURATION_NAME = "configuration"
        private const val USER_ID = "userId"
    }
}
