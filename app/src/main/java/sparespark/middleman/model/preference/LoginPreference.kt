package sparespark.middleman.model.preference

interface LoginPreference {
    fun updateLoginAttemptTimeToNow()
    fun isLoginAttemptNeeded(): Boolean
}