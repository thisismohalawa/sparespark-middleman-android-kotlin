package sparespark.middleman.productdetails.diffutil

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.data.model.product.RelatedUrl

class RelatedUrlDiffUtilCallback : DiffUtil.ItemCallback<RelatedUrl>() {
    override fun areItemsTheSame(oldItem: RelatedUrl, newItem: RelatedUrl): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: RelatedUrl, newItem: RelatedUrl): Boolean {
        return oldItem.url == newItem.url
    }
}
