package bigburger.ua.com.uabigburger.data.provider

import android.content.SharedPreferences

/**
 * Created by wessim.trimeche on 17/02/19.
 */
class SharedPreferencesProvider private constructor(preferences: SharedPreferences) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = preferences
    }

    fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    companion object {
        lateinit var sharedPreferencesProvider: SharedPreferencesProvider
        var NAME = "SHARED_PREFERENCES"
        fun getSharedPreferencesInstance(preferences: SharedPreferences): SharedPreferencesProvider {

                sharedPreferencesProvider = SharedPreferencesProvider(preferences)

            return sharedPreferencesProvider
        }
    }
}