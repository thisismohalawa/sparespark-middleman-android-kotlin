package sparespark.middleman.productdetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.core.loadImgUrl
import sparespark.middleman.data.model.product.RelatedUrl
import sparespark.middleman.databinding.ItemRelatedUrlBinding
import sparespark.middleman.productdetails.ProductDetailEvent
import sparespark.middleman.productdetails.diffutil.RelatedUrlDiffUtilCallback

class RelatedUrlListAdapter(
    val event: MutableLiveData<ProductDetailEvent> = MutableLiveData()
) : ListAdapter<RelatedUrl, RelatedUrlListAdapter.UrlViewHolder>(RelatedUrlDiffUtilCallback()) {
    inner class UrlViewHolder(var binding: ItemRelatedUrlBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrlViewHolder =
        UrlViewHolder(
            ItemRelatedUrlBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: UrlViewHolder, position: Int) {
        val product = getItem(position)
        with(holder.binding) {
            product.url?.let {
                imgProduct.loadImgUrl(
                    imgUrl = it,
                    useRandomColor = false,
                    onResourceReadyAction = null
                )
            }
            holder.itemView.setOnClickListener {
                event.value = ProductDetailEvent.OnRelatedItemClick(position)
            }
        }
    }
}