package sparespark.middleman.core

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout
import java.util.Locale

internal const val MIN_PASS_DIG = 6
internal const val MAX_PASS_DIG = 20

internal const val MAX_INPUT_NAME_DIG = 30
internal const val MIN_INPUT_NAME_DIG = 6
/*=============================*/

internal fun String.isEmailAddress(): Boolean = Patterns.EMAIL_ADDRESS.matcher(this).matches()
internal fun String.isValidPasswordLength(): Boolean = length in MIN_PASS_DIG..MAX_PASS_DIG
internal fun String.isMatch(query: String?): Boolean =
    query?.lowercase(Locale.ROOT)?.let { this.lowercase(Locale.ROOT).contains(it) } == true

/*=============================*/
/*=============================*/
internal fun EditText.setEmailInput() {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
}

internal fun EditText.setPasswordInput() {
    this.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
}

internal fun EditText.setPhoneNumInput() {
    this.inputType = InputType.TYPE_CLASS_PHONE
}

/*=====================Watcher===============*/
internal fun EditText.beginLayoutTitleLengthWatcher(
    inputLayout: TextInputLayout,
    minDig: Int,
    maxDig: Int,
    eMsg: String
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable) {
        }

        override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int): Unit {
            if (text.isNotEmpty() &&
                text.toString().length in minDig..maxDig
            ) {
                inputLayout.isErrorEnabled = false
                inputLayout.error = null
            } else inputLayout.error = eMsg
        }
    })
}

internal fun EditText.beginLayoutEmailWatcher(inputLayout: TextInputLayout, eMsg: String) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable) {
        }

        override fun onTextChanged(
            text: CharSequence,
            start: Int,
            count: Int,
            after: Int
        ): Unit {
            if (text.isNotEmpty() && text.toString().isEmailAddress()) {
                inputLayout.isErrorEnabled = false
                inputLayout.error = null
            } else inputLayout.error = eMsg
        }
    })
}
