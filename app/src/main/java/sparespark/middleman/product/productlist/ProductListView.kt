package sparespark.middleman.product.productlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.R
import sparespark.middleman.common.EventObserver
import sparespark.middleman.common.setStaggeredGradList
import sparespark.middleman.model.Product
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract
import sparespark.middleman.product.container.ContainerViewDirections
import sparespark.middleman.product.productlist.buildlogic.ProductListInjector

class ProductListView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: ProductListAdapter
    private lateinit var viewModel: ProductListViewModel
    private lateinit var viewCommunicate: ProductActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_product_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rec_list_fragment)
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
            factory = ProductListInjector(requireActivity().application).provideProductListViewModelFactory()
        )[ProductListViewModel::class.java]
        viewModel.handleEvent(ProductListEvent.GetProducts)
        setUpRecyclerView()
        viewModel.startObserving()
    }

    private fun setUpRecyclerView() {
        listAdapter = ProductListAdapter(onItemClick = object : ProductListAdapter.OnItemClick {
            override fun onClick(product: Product) {
                viewModel.handleEvent(ProductListEvent.OnProductClicked(product))
            }
        })
        recyclerView.adapter = listAdapter
        recyclerView.setStaggeredGradList(spanCount = 2)
    }

    private fun ProductListViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        productList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
        exploreProduct.observe(viewLifecycleOwner, EventObserver {
            startProductDetailWithArgs(it)
        })
    }

    private fun startProductDetailWithArgs(product: Product) = findNavController().navigate(
        ContainerViewDirections.navigateToProductDetailView(product)
    )
}