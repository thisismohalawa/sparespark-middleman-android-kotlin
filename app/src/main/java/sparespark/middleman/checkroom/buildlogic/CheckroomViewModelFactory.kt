package sparespark.middleman.checkroom.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.checkroom.CheckroomViewModel
import sparespark.middleman.data.repository.CartRepository

class CheckroomViewModelFactory(
    private val cartRepo: CartRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        if (modelClass.isAssignableFrom(CheckroomViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            CheckroomViewModel(cartRepo) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}