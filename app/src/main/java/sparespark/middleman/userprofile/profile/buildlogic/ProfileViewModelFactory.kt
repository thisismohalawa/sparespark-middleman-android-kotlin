package sparespark.middleman.userprofile.profile.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.data.repository.UserRepository
import sparespark.middleman.userprofile.profile.ProfileViewModel

class ProfileViewModelFactory(
    private val userRepo: UserRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            ProfileViewModel(userRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}
