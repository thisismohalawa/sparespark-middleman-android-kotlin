package sparespark.middleman.checkroom

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.core.loadImgUrl
import sparespark.middleman.core.toFullPrice
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.databinding.ItemCheckroomBinding
import sparespark.middleman.productlist.ProductDiffUtilCallback

class CheckroomListAdapter(
    val event: MutableLiveData<CheckroomEvent> = MutableLiveData()
) : ListAdapter<Product, CheckroomListAdapter.CheckroomViewHolder>(ProductDiffUtilCallback()) {

    inner class CheckroomViewHolder(var binding: ItemCheckroomBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CheckroomViewHolder =
        CheckroomViewHolder(
            ItemCheckroomBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CheckroomViewHolder, position: Int) {
        val product = getItem(position)
        with(holder.binding) {
            txtTitle.text = product.title
            txtPrice.text = product.price.toFullPrice()
            imgProduct.loadImgUrl(
                imgUrl = product.imgUrl, useRandomColor = false, onResourceReadyAction = null
            )
            imgClear.setOnClickListener {
                event.value = CheckroomEvent.OnImgClearItemClick(
                    position = holder.adapterPosition
                )
            }
            imgProduct.setOnClickListener {
                event.value = CheckroomEvent.OnImgProductItemClick(
                    position = holder.adapterPosition
                )
            }
        }
    }
}