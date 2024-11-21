package sparespark.middleman.core

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri

import kotlin.random.Random

internal fun Double.toFullPrice(): String = "$this LE"

private val vibrantLightColorList = arrayOf(
    ColorDrawable(Color.parseColor("#ffeead")),
    ColorDrawable(Color.parseColor("#93cfb3")),
    ColorDrawable(Color.parseColor("#fd7a7a")),
    ColorDrawable(Color.parseColor("#faca5f")),
    ColorDrawable(Color.parseColor("#1ba798")),
    ColorDrawable(Color.parseColor("#6aa9ae")),
    ColorDrawable(Color.parseColor("#ffbf27")),
    ColorDrawable(Color.parseColor("#d93947"))
)

internal fun getRandomDrawableColor(): ColorDrawable {
    val idx = Random.nextInt(vibrantLightColorList.size)
    return vibrantLightColorList[idx]
}

internal fun Context.showConfirmationDialog(
    title: String,
    positiveText: String,
    action: (() -> Unit)? = null,
) {
    val alertMessage = AlertDialog.Builder(this@showConfirmationDialog)
    alertMessage.setTitle(title)
    alertMessage.setCancelable(true)
    alertMessage.setPositiveButton(positiveText) { _, _ ->
        action?.invoke()
    }
    alertMessage.show()
}

internal fun Context.actionLaunchWhatsApp(content: String): Unit = try {
    val intent = Intent(
        Intent.ACTION_VIEW, Uri.parse(
            String.format(
                "https://api.whatsapp.com/send?phone=%s&text=%s", BUY_NUMBER, content
            )
        )
    )
    startActivity(intent)
} catch (e: Exception) {
    e.printStackTrace()
}