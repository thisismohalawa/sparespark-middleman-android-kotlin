package sparespark.middleman.preferences

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import sparespark.middleman.R
import sparespark.middleman.core.BRAND_CACHE_TIME
import sparespark.middleman.core.PRODUCT_CACHE_TIME
import sparespark.middleman.core.setNumberDecimalPrefInput
import sparespark.middleman.home.HomeActivityInteract
import sparespark.middleman.preferences.buildlogic.PreferenceViewInjector

private const val CLEAR_CACHE = "CLEAR CACHE"

class CachePreferenceView : PreferenceFragmentCompat() {
    private lateinit var viewInteract: HomeActivityInteract
    private lateinit var viewModel: PreferenceViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.cache_preference)
        setupInteract()
        setupViewModel()
        setupViewInputs()
        viewModel.viewModelStateObserver()
    }

    private fun setupViewInputs() {
        val edCity: EditTextPreference? = findPreference(BRAND_CACHE_TIME)
        val edClient: EditTextPreference? = findPreference(PRODUCT_CACHE_TIME)
        val btnClearCache: Preference? = findPreference(CLEAR_CACHE)

        edCity?.setNumberDecimalPrefInput()
        edClient?.setNumberDecimalPrefInput()
        btnClearCache?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            viewModel.handleEvent(CachePreferenceEvent.OnClearCacheClick)
            true
        }
    }

    private fun setupInteract() {
        viewInteract = activity as HomeActivityInteract
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@CachePreferenceView,
            factory = PreferenceViewInjector(requireActivity().application).provideViewModelFactory()
        )[PreferenceViewModel::class.java]
    }

    private fun PreferenceViewModel.viewModelStateObserver() {
        error.observe(this@CachePreferenceView) {
            viewInteract.displayToast(it.asString(requireContext()))
        }
        cacheUpdated.observe(this@CachePreferenceView) {
            viewInteract.displayToast(getString(R.string.updated_success))
        }
        dbCleared.observe(this@CachePreferenceView) {
            viewInteract.restartHomeActivity()
        }
    }
}