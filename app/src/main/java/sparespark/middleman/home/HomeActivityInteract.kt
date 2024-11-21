package sparespark.middleman.home

interface HomeActivityInteract {
    fun displayToast(msg: String?)
    fun updateProgressBarVisible(isVisible: Boolean)
    fun updateHeaderTitle(title: String)
    fun updateHeaderSubTitle(subTitle: String)
    fun startAuthActivity()
    fun restartHomeActivity()
    fun finishHomeActivity()
    fun launchWhatsApp(content: String)
}