package sparespark.middleman.product.productdetails.adapter

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.model.IRelated

class RelatedDiffUtilCallback : DiffUtil.ItemCallback<IRelated>() {
    override fun areItemsTheSame(oldItem: IRelated, newItem: IRelated): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: IRelated, newItem: IRelated): Boolean {
        return oldItem.url == newItem.url
    }
}
