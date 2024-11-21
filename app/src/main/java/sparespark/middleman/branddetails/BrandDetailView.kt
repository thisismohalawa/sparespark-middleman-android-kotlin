package sparespark.middleman.branddetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.branddetails.buildlogic.BrandDetailInjector
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.databinding.FragmentBrandDetailsBinding
import sparespark.middleman.home.HomeActivityInteract

class BrandDetailView : Fragment(),
    ViewBindingHolder<FragmentBrandDetailsBinding> by ViewBindingHolderImpl() {

    private lateinit var viewModel: BrandDetailsViewModel
    private lateinit var viewInteract: HomeActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        initBinding(FragmentBrandDetailsBinding.inflate(layoutInflater), this@BrandDetailView) {
            setupViewInteract()
            setupViewInputs()
            setupViewModel()
            viewModel.setupStatesObserver()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    private fun setupViewInteract() {
        viewInteract = activity as HomeActivityInteract
    }

    private fun setupViewInputs() {

    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@BrandDetailView,
            factory = BrandDetailInjector(requireActivity().application).provideViewModelFactory()
        )[BrandDetailsViewModel::class.java]
        viewModel.handleEvent(BrandDetailEvent.OnStartGetBrand)
    }


    private fun BrandDetailsViewModel.setupStatesObserver() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(context))
        }
        brand.observe(viewLifecycleOwner) {
            bindBrand(it)
        }
    }

    private fun bindBrand(brand: Brand) {
        binding?.apply {
            txtTitle.text = brand.title
            txtName.text = brand.name
        }
    }
}