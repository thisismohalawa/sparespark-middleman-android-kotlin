package sparespark.middleman.preferences

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sparespark.middleman.R
import sparespark.middleman.core.base.BaseViewModel
import sparespark.middleman.core.wrapper.Result
import sparespark.middleman.data.repository.PreferenceRepository

class PreferenceViewModel(
    private val prefRepo: PreferenceRepository
) : BaseViewModel<CachePreferenceEvent>() {

    private val cacheUpdateState = MutableLiveData<Unit>()
    val cacheUpdated: LiveData<Unit> get() = cacheUpdateState

    private val dbClearState = MutableLiveData<Unit>()
    val dbCleared: LiveData<Unit> get() = dbClearState

    override fun handleEvent(event: CachePreferenceEvent) {
        when (event) {
            is CachePreferenceEvent.OnClearCacheClick -> {
                clearPrefLastCacheTimes()
                clearDatabase()
            }
        }
    }

    private fun clearPrefLastCacheTimes() = viewModelScope.launch {
        if (prefRepo.clearPrefLastCacheTimes() is Result.Value) cacheUpdateState.value = Unit
        else showError(R.string.cannot_update_local_entries)
    }

    private fun clearDatabase() = viewModelScope.launch {
        if (prefRepo.clearDatabase() is Result.Value) dbClearState.value = Unit
        else showError(R.string.cannot_update_local_entries)
    }
}