package sparespark.middleman.product.productdetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sparespark.middleman.R
import sparespark.middleman.common.DEF_CARD_HEX_COLOR
import sparespark.middleman.common.setCardBackgroundColor
import sparespark.middleman.model.IColor
import sparespark.middleman.product.productdetails.ProductDetailEvent

class ColorListAdapter(
    val event: MutableLiveData<ProductDetailEvent> = MutableLiveData()
) :
    ListAdapter<IColor, ColorListAdapter.ColorViewHolder>(ColorDiffUtilCallback()) {

    inner class ColorViewHolder(root: View) : ViewHolder(root) {
        var cardColor: CardView = root.findViewById(R.id.card_color)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ColorViewHolder =
        ColorViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_color, parent, false)
        )

    override fun onBindViewHolder(holder: ColorViewHolder, position: Int) {
        holder.cardColor.setCardBackgroundColor(getItem(position).color ?: DEF_CARD_HEX_COLOR)
        holder.itemView.setOnClickListener {
            event.value = ProductDetailEvent.OnColorItemClick(position)
        }
    }
}
