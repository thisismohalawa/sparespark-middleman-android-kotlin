package sparespark.middleman.brandlist

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.model.Brand

class BrandDiffUtilCallback : DiffUtil.ItemCallback<Brand>() {
    override fun areItemsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Brand, newItem: Brand): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }
}
