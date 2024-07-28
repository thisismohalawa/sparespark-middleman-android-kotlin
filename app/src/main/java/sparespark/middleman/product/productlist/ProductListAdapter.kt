package sparespark.middleman.product.productlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sparespark.middleman.R
import sparespark.middleman.common.loadImgUrl
import sparespark.middleman.common.toFullPrice
import sparespark.middleman.model.Product

class ProductListAdapter(
    private val onItemClick: OnItemClick,
) : ListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffUtilCallback()) {

    class ProductViewHolder(root: View) : ViewHolder(root) {
        var title: TextView = root.findViewById(R.id.txt_title)
        var price: TextView = root.findViewById(R.id.txt_price)
        var img: ImageView = root.findViewById(R.id.img_product)
    }

    interface OnItemClick {
        fun onClick(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ProductViewHolder(
            inflater.inflate(R.layout.item_product, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        getItem(position).let { product ->
            holder.title.text = product.title
            holder.price.text = product.price.toFullPrice()
            holder.img.loadImgUrl(
                imgUrl = product.imgUrl, userRandomColor = false, onResourceReadyAction = null
            )
            holder.itemView.setOnClickListener {
                onItemClick.onClick(product)
            }
        }
    }
}