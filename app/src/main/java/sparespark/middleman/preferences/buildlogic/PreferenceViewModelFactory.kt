package sparespark.middleman.preferences.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.data.repository.PreferenceRepository
import sparespark.middleman.preferences.PreferenceViewModel

class PreferenceViewModelFactory(
    private val preferenceRepo: PreferenceRepository,
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(PreferenceViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            PreferenceViewModel(preferenceRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}
