package sparespark.middleman.common

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import sparespark.middleman.R


internal fun Context.showConfirmationDialog(
    title: String,
    positiveText: String,
    action: (() -> Unit)? = null,
) {
    val alertMessage = AlertDialog.Builder(this@showConfirmationDialog)
    alertMessage.setMessage(title)
    alertMessage.setCancelable(true)
    alertMessage.setPositiveButton(positiveText) { _, _ ->
        action?.invoke()
    }
    alertMessage.show()
}

private fun Context.toastIncompleteAction() =
    Toast.makeText(this, this.getString(R.string.invalid_action), Toast.LENGTH_SHORT).show()

internal fun Context.actionLaunchWhatsApp(content: String): Unit = try {
    val intent = Intent(
        Intent.ACTION_VIEW, Uri.parse(
            String.format(
                "https://api.whatsapp.com/send?phone=%s&text=%s", ORDER_NUM, content
            )
        )
    )
    startActivity(intent)
} catch (e: Exception) {
    this.toastIncompleteAction()
}