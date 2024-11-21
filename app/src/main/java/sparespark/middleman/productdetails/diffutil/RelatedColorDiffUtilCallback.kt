package sparespark.middleman.productdetails.diffutil

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.data.model.product.RelatedColor

class RelatedColorDiffUtilCallback : DiffUtil.ItemCallback<RelatedColor>() {
    override fun areItemsTheSame(oldItem: RelatedColor, newItem: RelatedColor): Boolean {
        return oldItem.color == newItem.color
    }

    override fun areContentsTheSame(oldItem: RelatedColor, newItem: RelatedColor): Boolean {
        return oldItem.color == newItem.color
    }
}
