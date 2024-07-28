package sparespark.middleman.product.productdetails.adapter

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.model.IColor

class ColorDiffUtilCallback : DiffUtil.ItemCallback<IColor>() {
    override fun areItemsTheSame(oldItem: IColor, newItem: IColor): Boolean {
        return oldItem.color == newItem.color
    }

    override fun areContentsTheSame(oldItem: IColor, newItem: IColor): Boolean {
        return oldItem.color == newItem.color
    }
}
