package sparespark.middleman.core

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.InputType
import android.text.SpannableString
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.preference.EditTextPreference
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target


internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

internal fun Activity.makeToast(msg: String) = Toast.makeText(
    this@makeToast, msg, Toast.LENGTH_SHORT
).show()

internal fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

internal fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

internal fun ImageView.setCustomImage(@DrawableRes drawable: Int, context: Context?) =
    if (context != null) setImageDrawable(ContextCompat.getDrawable(context, drawable))
    else Unit

internal fun TextView.setCustomColor(@ColorRes intColor: Int, context: Context?) =
    if (context != null) setTextColor(ContextCompat.getColor(context, intColor))
    else setTextColor(Color.DKGRAY)

internal fun CardView.setCustomCardBackgroundColor(hexColor: String?) = try {
    if (hexColor?.startsWith('#') == true) setCardBackgroundColor(Color.parseColor(hexColor))
    else setCardBackgroundColor(Color.parseColor(DEF_CARD_HEX_COLOR))
} catch (ex: Exception) {
    setCardBackgroundColor(Color.parseColor(DEF_CARD_HEX_COLOR))
}

internal fun EditTextPreference.setNumberDecimalPrefInput() {
    setOnBindEditTextListener { editText ->
        editText.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_DECIMAL
    }
}

internal fun TextView.displayAsLinedBoldStyle(content: String) = try {
    val spanString = SpannableString(content)
    spanString.setSpan(UnderlineSpan(), 0, spanString.length, 0)
    spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
    this.setText(spanString)
} catch (ex: Exception) {
    ex.printStackTrace()
}

internal fun RecyclerView.setupListItemDecoration(context: Context) {
    addItemDecoration(
        DividerItemDecoration(
            context, DividerItemDecoration.VERTICAL
        )
    )
}

internal fun RecyclerView.setStaggeredGradList(spanCount: Int) {
    this.apply {
        setHasFixedSize(true)
        val sGridLayoutManager = StaggeredGridLayoutManager(
            spanCount, StaggeredGridLayoutManager.VERTICAL
        )
        layoutManager = sGridLayoutManager
    }
}

internal fun ImageView.loadImgUrl(
    imgUrl: String,
    useRandomColor: Boolean,
    onResourceReadyAction: (() -> Unit)? = null,
) {
    Glide.with(this@loadImgUrl.context).load(imgUrl).error(
        if (useRandomColor) getRandomDrawableColor()
        else ColorDrawable(Color.parseColor(DEF_CARD_HEX_COLOR))
    ).transform(FitCenter()).listener(object : RequestListener<Drawable?> {
        override fun onLoadFailed(
            e: GlideException?,
            model: Any?,
            target: Target<Drawable?>,
            isFirstResource: Boolean,
        ): Boolean {
            return false
        }

        override fun onResourceReady(
            resource: Drawable,
            model: Any,
            target: Target<Drawable?>?,
            dataSource: com.bumptech.glide.load.DataSource,
            isFirstResource: Boolean
        ): Boolean {
            onResourceReadyAction?.invoke()
            return false
        }
    }).into(this@loadImgUrl)
}

internal fun Fragment.relaunchCurrentView() {
    findNavController().apply {
        currentDestination?.id?.let {
            this.popBackStack(it, true)
            this.navigate(it)
        }
    }
}