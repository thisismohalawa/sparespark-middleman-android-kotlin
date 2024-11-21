package sparespark.middleman.productlist.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.R
import sparespark.middleman.databinding.ItemCategoryMenuBinding
import sparespark.middleman.data.model.category.CategoryMenu
import sparespark.middleman.productlist.ProductListEvent

class CategoryMenuAdapter(
    private val categoryList: List<CategoryMenu>,
    val event: MutableLiveData<ProductListEvent> = MutableLiveData()
) : RecyclerView.Adapter<CategoryMenuAdapter.CategoryTitleViewHolder>() {

    inner class CategoryTitleViewHolder(var binding: ItemCategoryMenuBinding) :
        RecyclerView.ViewHolder(
            binding.root
        )

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryTitleViewHolder =
        CategoryTitleViewHolder(
            ItemCategoryMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: CategoryTitleViewHolder, position: Int) {
        val item = categoryList[position]
        with(holder) {
            binding.txtTitle.text = itemView.context.getString(item.titleRes)
            binding.root.setOnClickListener {
                event.value = ProductListEvent.OnMenuCategoryClicked(item.id)
            }
            if (position == 0) binding.root.setBackgroundResource(R.drawable.item_border_dark)
        }
    }


    override fun getItemCount(): Int = categoryList.size


}