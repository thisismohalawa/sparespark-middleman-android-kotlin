package sparespark.middleman.product.productdetails.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sparespark.middleman.R
import sparespark.middleman.cartlist.CartListEvent
import sparespark.middleman.common.loadImgUrl
import sparespark.middleman.model.IRelated
import sparespark.middleman.product.productdetails.ProductDetailEvent

class RelatedListAdapter(
    val event: MutableLiveData<ProductDetailEvent> = MutableLiveData()
) :
    ListAdapter<IRelated, RelatedListAdapter.RelatedViewHolder>(RelatedDiffUtilCallback()) {

    inner class RelatedViewHolder(root: View) : ViewHolder(root) {
        var imgProduct: ImageView = root.findViewById(R.id.img_product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHolder =
        RelatedViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_related, parent, false)
        )

    override fun onBindViewHolder(holder: RelatedViewHolder, position: Int) {
        getItem(position).url?.let {
            holder.imgProduct.loadImgUrl(
                imgUrl = it, userRandomColor = false, onResourceReadyAction = null
            )
            holder.imgProduct.setOnClickListener {
                event.value = ProductDetailEvent.OnRelatedItemClick(position)
            }
        }
    }
}
