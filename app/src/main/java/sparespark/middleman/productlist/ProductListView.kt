package sparespark.middleman.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import sparespark.middleman.R
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.isMatch
import sparespark.middleman.core.setStaggeredGradList
import sparespark.middleman.core.wrapper.EventObserver
import sparespark.middleman.data.model.category.CategoryMenu
import sparespark.middleman.data.model.category.CategorySelect
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.databinding.FragmentProductListBinding
import sparespark.middleman.home.HomeActivityInteract
import sparespark.middleman.productlist.adapter.CategoryMenuAdapter
import sparespark.middleman.productlist.adapter.ProductListAdapter
import sparespark.middleman.productlist.buildlogic.ProductListViewInjector

class ProductListView : Fragment(),
    ViewBindingHolder<FragmentProductListBinding> by ViewBindingHolderImpl() {

    private lateinit var listAdapter: ProductListAdapter
    private lateinit var viewModel: ProductListViewModel
    private lateinit var viewInteract: HomeActivityInteract
    private lateinit var categoryMenuAdapter: CategoryMenuAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        initBinding(FragmentProductListBinding.inflate(layoutInflater), this@ProductListView) {
            setupViewInteract()
            setupBottomNav()
            setupViewInputs()
            setupProductListAdapter()
            setupViewModel()
            viewModel.setupStatesObserver()
        }

    private fun setupViewInteract() {
        viewInteract = activity as HomeActivityInteract
        viewInteract.updateHeaderTitle(getString(R.string.app_name))
        viewInteract.updateHeaderSubTitle(getString(R.string.app_subtitle))
    }

    private fun setupViewInputs() {
        binding?.apply {
            itemSearch.mtSearchView.queryHint = getString(R.string.search_for_product)
        }
    }

    private fun setupCategoryListAdapter(list: List<CategoryMenu>) {
        categoryMenuAdapter = CategoryMenuAdapter(categoryList = list)
        binding?.recCategoryList?.adapter = categoryMenuAdapter
        categoryMenuAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
    }

    private fun setupProductListAdapter() {
        listAdapter = ProductListAdapter()
        binding?.recListFragment?.apply {
            setStaggeredGradList(spanCount = 2)
            adapter = listAdapter
        }
        listAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@ProductListView,
            factory = ProductListViewInjector(requireActivity().application).provideViewModelFactory()
        )[ProductListViewModel::class.java]
        viewModel.handleEvent(ProductListEvent.OnViewStart)
    }

    private fun setupBottomNav() {
        binding?.bottomNav?.apply {
            setupWithNavController(findNavController())
            setOnNavigationItemSelectedListener {
                when (it.itemId) {
                    R.id.cartListView -> if (isDestination()) findNavController().navigate(
                        ProductListViewDirections.navigateToCart()
                    )

                    R.id.profileView -> if (isDestination()) findNavController().navigate(
                        ProductListViewDirections.navigateToProfile()
                    )
                }
                true
            }
        }
    }


    private fun updateCategoryMenuSelect(mList: List<CategorySelect>?) {
        mList?.forEach {
            val itemView =
                binding?.recCategoryList?.findViewHolderForAdapterPosition(it.pos)?.itemView
            val contentLayout = itemView?.findViewById<LinearLayout>(R.id.content_layout)
            if (it.isSelect) contentLayout?.setBackgroundResource(R.drawable.item_border_dark)
            else contentLayout?.setBackgroundResource(R.drawable.item_border_gray)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    private fun ProductListViewModel.setupStatesObserver() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(context))
        }
        editProduct.observe(viewLifecycleOwner, EventObserver {
            if (isDestination()) findNavController().navigate(
                ProductListViewDirections.navigateToProductDetails(
                    productId = it
                )
            )
        })
        updateCategoryMenuSelectAttempt.observe(viewLifecycleOwner) {
            updateCategoryMenuSelect(it)
        }
        categoryList.observe(viewLifecycleOwner) {
            setupCategoryListAdapter(it)
        }
        productList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            setupSearchViewListener(it)
        }
        navigateToBrandListAttempt.observe(viewLifecycleOwner, EventObserver {
            if (isDestination()) findNavController().navigate(
                ProductListViewDirections.navigateToBrandList()
            )
        })
    }

    private fun setupSearchViewListener(list: List<Product>) {
        binding?.itemSearch?.mtSearchView?.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredList = mutableListOf<Product>()
                    for (product: Product in list)
                        if (product.title.isMatch(it)) filteredList.add(product)
                        else if (product.des.isMatch(it)) filteredList.add(product)
                        else if (product.price.toString().contains(it)) filteredList.add(product)

                    if (filteredList.isNotEmpty()) listAdapter.submitList(filteredList)
                }
                return true
            }
        })
    }

    private fun isDestination() = findNavController().currentDestination?.id == R.id.productListView
}