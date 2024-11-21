package sparespark.middleman.brandlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.brandlist.buildlogic.BrandListInjector
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.isMatch
import sparespark.middleman.core.setupListItemDecoration
import sparespark.middleman.core.wrapper.EventObserver
import sparespark.middleman.data.model.brand.Brand
import sparespark.middleman.databinding.FragmentBrandListBinding
import sparespark.middleman.home.HomeActivityInteract

class BrandListView : Fragment(),
    ViewBindingHolder<FragmentBrandListBinding> by ViewBindingHolderImpl() {

    private lateinit var listAdapter: BrandListAdapter
    private lateinit var viewModel: BrandListViewModel
    private lateinit var viewInteract: HomeActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        initBinding(FragmentBrandListBinding.inflate(layoutInflater), this@BrandListView) {
            setupViewInteract()
            setupViewInputs()
            setupRecyclerView()
            setupViewModel()
            viewModel.startObserving()
        }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    private fun setupViewInteract() {
        viewInteract = activity as HomeActivityInteract
    }

    private fun setupViewInputs() {
        binding?.apply {
            itemSearch.mtSearchView.queryHint = getString(R.string.search_for_brand)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@BrandListView,
            factory = BrandListInjector(requireActivity().application).provideViewModelFactory()
        )[BrandListViewModel::class.java]
        viewModel.handleEvent(BrandListEvent.GetBrands)
    }

    private fun setupRecyclerView() {
        listAdapter = BrandListAdapter()
        binding?.recListFragment?.apply {
            setupListItemDecoration(requireContext())
            adapter = listAdapter
        }
        listAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
    }

    private fun BrandListViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(requireContext()))
        }
        brandsList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            setupSearchViewListener(it)
        }
        editBrand.observe(viewLifecycleOwner, EventObserver {
            if (isDestination()) findNavController().navigate(
                BrandListViewDirections.navigateToBrandDetails(
                    brandId = it
                )
            )
        })
    }

    private fun setupSearchViewListener(list: List<Brand>) {
        binding?.itemSearch?.mtSearchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredList = mutableListOf<Brand>()
                    for (brand: Brand in list)
                        if (brand.name.isMatch(it)) filteredList.add(brand)
                        else if (brand.title.isMatch(it)) filteredList.add(brand)

                    if (filteredList.isNotEmpty()) listAdapter.submitList(filteredList)
                }
                return true
            }
        })
    }

    private fun isDestination() = findNavController().currentDestination?.id == R.id.brandListView
}