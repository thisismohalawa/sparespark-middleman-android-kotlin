package sparespark.middleman.preference

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import sparespark.middleman.R

class PreferenceView : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }
}
