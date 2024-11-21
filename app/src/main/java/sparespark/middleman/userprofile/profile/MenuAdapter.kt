package sparespark.middleman.userprofile.profile

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import sparespark.middleman.R
import sparespark.middleman.core.setCustomColor
import sparespark.middleman.core.setCustomImage
import sparespark.middleman.core.visible
import sparespark.middleman.data.model.ProfileMenu
import sparespark.middleman.databinding.ItemMenuHeaderBinding
import sparespark.middleman.userprofile.UserEvent

class MenuAdapter(
    val list: List<ProfileMenu>,
    val event: MutableLiveData<UserEvent> = MutableLiveData()
) : RecyclerView.Adapter<MenuAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(var binding: ItemMenuHeaderBinding) : ViewHolder(
        binding.root
    )

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder =
        MenuViewHolder(
            ItemMenuHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = list[position]
        val iContext = holder.itemView.context
        with(holder.binding) {
            imgAction.setCustomImage(R.drawable.ic_arrow_nav, iContext)
            imgAction.visible(isVisible = menu.isNav)

            txtTitle.text = iContext.getString(menu.title)
            txtSubtitle.text = iContext.getString(menu.des)

            holder.itemView.setOnClickListener {
                event.value = UserEvent.OnMenuItemClick(menu.id)
            }
        }
    }
}

