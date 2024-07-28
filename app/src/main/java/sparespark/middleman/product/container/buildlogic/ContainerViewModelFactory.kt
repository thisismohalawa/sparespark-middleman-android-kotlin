package sparespark.middleman.product.container.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.model.preference.LoginPreference
import sparespark.middleman.model.repository.LoginRepository
import sparespark.middleman.product.container.ContainerViewModel

class ContainerViewModelFactory(
    private val loginRepo: LoginRepository,
    private val loginPref: LoginPreference
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ContainerViewModel(loginRepo, loginPref) as T
    }
}