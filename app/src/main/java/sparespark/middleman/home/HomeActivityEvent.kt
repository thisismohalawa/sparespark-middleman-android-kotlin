package sparespark.middleman.home

sealed class HomeActivityEvent {
    data object OnStartCheckUser : HomeActivityEvent()
}