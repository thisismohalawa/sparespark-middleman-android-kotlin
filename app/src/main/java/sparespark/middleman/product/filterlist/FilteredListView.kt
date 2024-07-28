package sparespark.middleman.product.filterlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import sparespark.middleman.R
import sparespark.middleman.common.EventObserver
import sparespark.middleman.common.setStaggeredGradList
import sparespark.middleman.model.Product
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract
import sparespark.middleman.product.filterlist.buildlogic.FilterListInjector
import sparespark.middleman.product.productlist.ProductListAdapter
import java.util.Locale

class FilteredListView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ProductListAdapter
    private lateinit var viewModel: FilterListViewModel
    private lateinit var viewCommunicate: ProductActivityInteract
    private lateinit var mtSearchView: SearchView
    private lateinit var fActionButton: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_filter_productlist, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rec_list_fragment)
        mtSearchView = view.findViewById(R.id.mt_searchView)
        fActionButton = view.findViewById(R.id.floating_btn)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewCommunicate = activity as ProductActivity
        viewModel = ViewModelProvider(
            owner = this,
            factory = FilterListInjector(requireActivity().application).provideFilterListViewModelFactory()
        )[FilterListViewModel::class.java]
        viewModel.handleEvent(FilterListEvent.GetProducts)
        setUpRecyclerView()
        setUpClickListener()
        viewModel.startObserving()
    }

    private fun setUpRecyclerView() {
        listAdapter = ProductListAdapter(onItemClick = object : ProductListAdapter.OnItemClick {
            override fun onClick(product: Product) {
                viewModel.handleEvent(FilterListEvent.OnProductClicked(product))
            }
        })
        recyclerView.adapter = listAdapter
        recyclerView.setStaggeredGradList(spanCount = 2)
    }

    private fun setUpClickListener() {
        fActionButton.setOnClickListener {
            viewModel.handleEvent(FilterListEvent.OnSupportButtonClick)
        }
    }

    private fun FilterListViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        productList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            setUpSearchViewListener(it)
        }
        exploreProduct.observe(viewLifecycleOwner, EventObserver {
            startProductDetailWithArgs(it)
        })
        supportAttempt.observe(viewLifecycleOwner, EventObserver {
            viewCommunicate.launchWhatsApp(
                dTitle = getString(R.string.support_contact),
                dPositiveTitle = getString(R.string.ok),
                wContent = it
            )
        })
    }

    private fun startProductDetailWithArgs(product: Product) = findNavController().navigate(
        FilteredListViewDirections.navigateToProductDetailView(product)
    )

    private fun setUpSearchViewListener(productList: List<Product>) {
        mtSearchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    val filteredList = mutableListOf<Product>()
                    for (product in productList) if (product.title.lowercase(Locale.ROOT)
                            .contains(it)
                    ) filteredList.add(product) else if (product.des.lowercase(Locale.ROOT)
                            .contains(it)
                    ) filteredList.add(product) else if (product.price.toString()
                            .lowercase(Locale.ROOT)
                            .contains(it)
                    ) filteredList.add(product)


                    if (filteredList.isNotEmpty()) listAdapter.submitList(filteredList)

                }
                return true
            }
        })
    }
}