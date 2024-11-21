package sparespark.middleman.productlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.core.loadImgUrl
import sparespark.middleman.core.toFullPrice
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.databinding.ItemProductBinding
import sparespark.middleman.productlist.ProductListEvent
import sparespark.middleman.productlist.ProductDiffUtilCallback

class ProductListAdapter(
    val event: MutableLiveData<ProductListEvent> = MutableLiveData()
) : ListAdapter<Product, ProductListAdapter.ProductViewHolder>(ProductDiffUtilCallback()) {

    inner class ProductViewHolder(var binding: ItemProductBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder =
        ProductViewHolder(
            ItemProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = getItem(position)
        with(holder.binding) {
            txtTitle.text = product.title
            txtPrice.text = product.price.toFullPrice()
            imgProduct.loadImgUrl(
                imgUrl = product.imgUrl, useRandomColor = false, onResourceReadyAction = null
            )
            root.setOnClickListener {
                event.value = ProductListEvent.OnProductItemClick(
                    position = holder.adapterPosition
                )
            }
        }
    }
}