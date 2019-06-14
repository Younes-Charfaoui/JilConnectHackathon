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

    fun isFirstTimeLaunched() = preferences.getBoolean(FIRST_TIME_CURRENCY, true)

    fun setFirstTimeLaunchedForCurrency() {
        editor.putString(PREFERENCE_CURRENCY, "Euro")
        editor.putBoolean(FIRST_TIME_CURRENCY, false)
        editor.commit()
    }

    fun isFirstTimeSubscribe() = preferences.getBoolean(FIRST_TIME_SUBSCRIBE, true)

    fun setFirstTimeLaunchedForSubscribe() {
        editor.putBoolean(FIRST_TIME_SUBSCRIBE, false)
        editor.commit()
    }

    var currency: String
        get() = preferences.getString(PREFERENCE_CURRENCY, "Euro") ?: "Euro"
        set(value) {
            editor.putString(PREFERENCE_CURRENCY, value).commit()
        }

    var userId: String
        get() = preferences.getString(USER_ID, "") ?: ""
        set(value) {
            editor.putString(USER_ID, value).commit()
        }

    fun unsubscribe() {
        editor.remove(USER_ID).commit()
    }

    var autoStart: Boolean
        get() = preferences.getBoolean(AUTO_START, true)
        set(value) {
            editor.putBoolean(AUTO_START, value).commit()
        }


    companion object {
        private const val PRIVATE_MODE = 0
        private const val PREFERENCE_CONFIGURATION_NAME = "configuration"
        private const val PREFERENCE_CURRENCY = "currency"
        private const val FIRST_TIME_CURRENCY = "isFirstTimeCurrency"
        private const val FIRST_TIME_SUBSCRIBE = "isFirstTimeSubscribe"
        private const val USER_ID = "userId"
        private const val AUTO_START = "autoStart"
    }
}
