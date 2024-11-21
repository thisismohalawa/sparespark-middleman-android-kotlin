package sparespark.middleman.brandlist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.core.loadImgUrl
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.databinding.ItemBrandBinding

class BrandListAdapter(val event: MutableLiveData<BrandListEvent> = MutableLiveData()) :
    ListAdapter<Brand, BrandListAdapter.BrandViewHolder>(BrandDiffUtilCallback()) {

    inner class BrandViewHolder(var binding: ItemBrandBinding) : RecyclerView.ViewHolder(
        binding.root
    )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BrandViewHolder =
        BrandViewHolder(
            ItemBrandBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: BrandViewHolder, position: Int) {
        val brand = getItem(position)
        with(holder.binding) {
            txtTitle.text = brand.title
            txtName.text = brand.name
            imgBrand.loadImgUrl(
                imgUrl = brand.imgUrl,
                useRandomColor = false,
                onResourceReadyAction = null
            )
            switchNotify.setOnCheckedChangeListener { _, isChecked ->
                event.value = BrandListEvent.OnSwitchItemUpdated(
                    position = position,
                    isChecked = isChecked
                )
            }
        }
    }
}