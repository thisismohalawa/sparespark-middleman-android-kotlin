package sparespark.middleman.branddetails.buildlogic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import sparespark.middleman.branddetails.BrandDetailsViewModel
import sparespark.middleman.data.repository.BrandRepository

class BrandDetailsViewModelFactory(
    private val brandRepo: BrandRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        if (modelClass.isAssignableFrom(BrandDetailsViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            BrandDetailsViewModel(brandRepo, extras.createSavedStateHandle()) as T
        else throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
}