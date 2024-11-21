package sparespark.middleman.userprofile

sealed class UserEvent {
    data object GetCurrentUser : UserEvent()
    data class OnMenuItemClick(val menuId: Int) : UserEvent()
}
