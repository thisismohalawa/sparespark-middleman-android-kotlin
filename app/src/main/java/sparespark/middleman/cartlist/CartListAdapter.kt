package sparespark.middleman.cartlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sparespark.middleman.R
import sparespark.middleman.common.displayAsLinedBoldStyle
import sparespark.middleman.common.loadImgUrl
import sparespark.middleman.common.toFullPrice
import sparespark.middleman.model.Product
import sparespark.middleman.product.productlist.ProductDiffUtilCallback

class CartListAdapter(
    val event: MutableLiveData<CartListEvent> = MutableLiveData()
) : ListAdapter<Product, CartListAdapter.CartViewHolder>(ProductDiffUtilCallback()) {

    class CartViewHolder(root: View) : ViewHolder(root) {
        var title: TextView = root.findViewById(R.id.txt_title)
        var price: TextView = root.findViewById(R.id.txt_price)
        var img: ImageView = root.findViewById(R.id.img_product)
        var imgClear: ImageView = root.findViewById(R.id.img_clear)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return CartViewHolder(inflater.inflate(R.layout.item_cart, parent, false))
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        getItem(position).let { product ->
            holder.title.text = product.title
            holder.price.displayAsLinedBoldStyle(product.price.toFullPrice())
            holder.img.loadImgUrl(
                imgUrl = product.imgUrl,
                userRandomColor = false,
                onResourceReadyAction = null
            )
            holder.imgClear.setOnClickListener {
                event.value = CartListEvent.OnClearItemClick(
                    position = position
                )
            }
            holder.img.setOnClickListener {
                event.value = CartListEvent.OnImgItemClick(
                    position = position
                )
            }
        }
    }
}