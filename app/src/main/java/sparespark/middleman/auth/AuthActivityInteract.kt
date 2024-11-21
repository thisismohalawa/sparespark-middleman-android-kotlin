package sparespark.middleman.auth

interface AuthActivityInteract {
    fun updateProgressBarVisible(isVisible: Boolean)
    fun displayToast(msg: String)
    fun startDataActivity()
}