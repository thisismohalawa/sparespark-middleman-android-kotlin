package sparespark.middleman.product.productlist

import androidx.recyclerview.widget.DiffUtil
import sparespark.middleman.model.Product

class ProductDiffUtilCallback : DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return oldItem.creationDate == newItem.creationDate
    }
}
