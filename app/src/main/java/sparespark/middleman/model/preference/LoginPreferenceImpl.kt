package sparespark.middleman.model.preference

import android.content.Context
import org.threeten.bp.ZonedDateTime

private const val loginLastCache = "login_last_attempt"

class LoginPreferenceImpl(
    context: Context,
) : PreferenceProvider(context), LoginPreference {

    private fun getLastCacheTime(): String? =
        preferences.getString(loginLastCache, null)

    override fun updateLoginAttemptTimeToNow() =
        preferencesEditor.putString(loginLastCache, ZonedDateTime.now().toString()).apply()

    override fun isLoginAttemptNeeded(): Boolean {
        if (getLastCacheTime() == null) return true
        return try {
            val timeAgo = ZonedDateTime.now().minusHours(24.toLong())
            val fetchedTime = ZonedDateTime.parse(getLastCacheTime())
            fetchedTime.isBefore(timeAgo)
        } catch (ex: Exception) {
            true
        }
    }
}