package sparespark.middleman.brandlist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.R
import sparespark.middleman.common.enable
import sparespark.middleman.common.loadImgUrl
import sparespark.middleman.model.Brand

class BrandListAdapter(val event: MutableLiveData<BrandListEvent> = MutableLiveData()) :
    ListAdapter<Brand, BrandListAdapter.BrandViewHolder>(BrandDiffUtilCallback()) {
    class BrandViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        var title: TextView = root.findViewById(R.id.txt_title)
        var name: TextView = root.findViewById(R.id.txt_name)
        var img: ImageView = root.findViewById(R.id.img_brand)
        var notifySwitch: SwitchCompat = root.findViewById(R.id.switch_notify)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return BrandViewHolder(
            inflater.inflate(R.layout.item_brand, parent, false)
        )
    }

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        getItem(position).let { brand ->
            holder.name.text = brand.name
            holder.title.text = brand.title
            holder.img.loadImgUrl(
                imgUrl = brand.imgUrl,
                userRandomColor = false,
                onResourceReadyAction = null
            )
            holder.itemView.setOnClickListener {
                event.value = BrandListEvent.OnBrandItemClick(position)
            }
            holder.notifySwitch.setOnCheckedChangeListener { _, isChecked ->
                event.value = BrandListEvent.OnSwitchItemUpdated(
                    position = position,
                    isChecked = isChecked
                )
            }
        }
    }
}