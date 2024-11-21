package sparespark.middleman.productdetails.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.core.setCustomCardBackgroundColor
import sparespark.middleman.data.model.product.RelatedColor
import sparespark.middleman.databinding.ItemRelatedColorBinding
import sparespark.middleman.productdetails.ProductDetailEvent
import sparespark.middleman.productdetails.diffutil.RelatedColorDiffUtilCallback

class RelatedColorListAdapter(
    val event: MutableLiveData<ProductDetailEvent> = MutableLiveData()
) : ListAdapter<RelatedColor, RelatedColorListAdapter.ColorViewHolder>(RelatedColorDiffUtilCallback()) {
    inner class ColorViewHolder(var binding: ItemRelatedColorBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(
            ItemRelatedColorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            cardColor.setCustomCardBackgroundColor(item.color)
            holder.itemView.setOnClickListener {
                event.value = ProductDetailEvent.OnColorItemClick(position)
            }
        }
    }
}