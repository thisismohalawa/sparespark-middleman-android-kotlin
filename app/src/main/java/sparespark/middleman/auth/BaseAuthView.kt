package sparespark.middleman.auth

import android.widget.EditText
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputLayout
import sparespark.middleman.R
import sparespark.middleman.core.MAX_PASS_DIG
import sparespark.middleman.core.MIN_PASS_DIG
import sparespark.middleman.core.beginLayoutEmailWatcher
import sparespark.middleman.core.beginLayoutTitleLengthWatcher
import sparespark.middleman.core.setEmailInput
import sparespark.middleman.core.setPasswordInput

open class BaseAuthView : Fragment() {

    protected fun EditText.setUserEmailInput(inputLayout: TextInputLayout) = try {
        this.apply {
            inputLayout.hint = getString(R.string.email)
            setEmailInput()
            beginLayoutEmailWatcher(
                inputLayout = inputLayout,
                eMsg = getString(R.string.invalid)
            )
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    protected fun EditText.setUserPasswordInput(inputLayout: TextInputLayout) = try {
        this.apply {
            inputLayout.hint = getString(R.string.password)
            inputLayout.isPasswordVisibilityToggleEnabled = true
            setPasswordInput()
            beginLayoutTitleLengthWatcher(
                inputLayout = inputLayout,
                minDig = MIN_PASS_DIG,
                maxDig = MAX_PASS_DIG,
                eMsg = getString(R.string.invalid)
            )
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }
}