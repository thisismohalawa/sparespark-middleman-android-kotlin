package sparespark.middleman.userprofile.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.menu.MENU_AUTH
import sparespark.middleman.core.menu.menuList
import sparespark.middleman.core.wrapper.Event
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.model.ProfileMenu
import sparespark.middleman.data.repository.UserRepository
import sparespark.middleman.userprofile.UserEvent
import sparespark.middleman.userprofile.UserViewModel

class ProfileViewModel(
    private val userRepo: UserRepository
) : UserViewModel(userRepo) {

    internal val loginAttempt = MutableLiveData<Unit>()

    private val menuListState = MutableLiveData<List<ProfileMenu>>()
    val menuList: LiveData<List<ProfileMenu>> get() = menuListState

    private val editMenuState = MutableLiveData<Event<Int>>()
    val editMenu: LiveData<Event<Int>> get() = editMenuState

    override fun handleEvent(event: UserEvent) {
        when (event) {
            is UserEvent.GetCurrentUser -> getUser(followAction = {
                menuListState.value = menuList(userExist = isUserExist())
            })

            is UserEvent.OnMenuItemClick -> {
                if (event.menuId == MENU_AUTH) signOut()
                else editMenuState.value = Event(event.menuId)
            }

        }
    }

    private fun signOut() = viewModelScope.launch {
        showLoading()
        when (val result = userRepo.signOutCurrentUser()) {
            is Result.Error -> result.error.message.actionExceptionMsg(
                offline = {
                    showError(R.string.error_no_internet)
                },
                unauthorised = {
                    loginAttempt.value = Unit
                },
                error = {
                    showError(R.string.unable_to_sign_out)
                })

            is Result.Value -> userUpdated()
        }
        hideLoading()
    }
}