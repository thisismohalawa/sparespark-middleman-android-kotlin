package sparespark.middleman.common

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.SpannableString
import android.text.TextWatcher
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.bumptech.glide.request.RequestListener
import com.google.android.material.textfield.TextInputLayout

internal fun View.enable(enabled: Boolean) {
    isEnabled = enabled
    alpha = if (enabled) 1f else 0.5f
}

internal fun View.preventDoubleClick() {
    this.isEnabled = false
    this.postDelayed({ this.isEnabled = true }, 2000)
}

internal fun String.toEditable(): Editable = Editable.Factory.getInstance().newEditable(this)

internal fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

internal fun CardView.setCardBackgroundColor(hexColor: String) = try {
    if (hexColor.startsWith('#')) setCardBackgroundColor(Color.parseColor(hexColor)) else Unit
} catch (ex: Exception) {
    setCardBackgroundColor(Color.parseColor(DEF_CARD_HEX_COLOR))
}

internal fun Activity.makeToast(strValue: String?) {
    Toast.makeText(this@makeToast, strValue, Toast.LENGTH_SHORT).show()
}

internal fun RecyclerView.setListItemDecoration(context: Context) {
    addItemDecoration(
        DividerItemDecoration(
            context, DividerItemDecoration.VERTICAL
        )
    )
}

internal fun RecyclerView.setStaggeredGradList(
    spanCount: Int,
) {
    this.apply {
        setHasFixedSize(true)
        val sGridLayoutManager = StaggeredGridLayoutManager(
            spanCount, StaggeredGridLayoutManager.VERTICAL
        )
        layoutManager = sGridLayoutManager
    }
}

internal fun Fragment.relaunchCurrentView() {
    findNavController().apply {
        currentDestination?.id?.let {
            this.popBackStack(it, true)
            this.navigate(it)
        }
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

internal fun ImageView.loadImgUrl(
    imgUrl: String,
    userRandomColor: Boolean,
    onResourceReadyAction: (() -> Unit)? = null,
) {
    Glide.with(this@loadImgUrl.context).load(imgUrl).error(
        if (userRandomColor) getRandomDrawableColor()
        else ColorDrawable(Color.parseColor(DEF_CARD_HEX_COLOR))
    ).transform(FitCenter()).listener(object : RequestListener<Drawable?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any?,
                target: com.bumptech.glide.request.target.Target<Drawable?>,
                isFirstResource: Boolean,
            ): Boolean {
                return false
            }

            override fun onResourceReady(
                resource: Drawable,
                model: Any,
                target: com.bumptech.glide.request.target.Target<Drawable?>?,
                dataSource: DataSource,
                isFirstResource: Boolean,
            ): Boolean {
                onResourceReadyAction?.invoke()
                return false
            }
        }).into(this@loadImgUrl)
}

internal fun EditText.beginPhoneTextWatcher(
    inputLayout: TextInputLayout, msgError: String
) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        }

        override fun afterTextChanged(s: Editable) {
        }

        override fun onTextChanged(text: CharSequence, start: Int, count: Int, after: Int): Unit {
            if (text.isNotEmpty() && PhoneNumberUtils.isGlobalPhoneNumber(text.toString())) {
                inputLayout.isErrorEnabled = false
                inputLayout.error = null
            } else inputLayout.error = msgError
        }

    })
}