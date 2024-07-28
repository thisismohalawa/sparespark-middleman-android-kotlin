package sparespark.middleman.brandlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.R
import sparespark.middleman.brandlist.buidlogic.BrandListInjector
import sparespark.middleman.common.EventObserver
import sparespark.middleman.common.setListItemDecoration
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract
import sparespark.middleman.product.container.ContainerViewDirections

class BrandListView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: BrandListAdapter
    private lateinit var viewModel: BrandListViewModel
    private lateinit var viewCommunicate: ProductActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_brand_list, container, false)

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
            factory = BrandListInjector(requireActivity().application).provideBrandListViewModelFactory()
        )[BrandListViewModel::class.java]
        viewModel.handleEvent(BrandListEvent.GetBrands)
        setUpRecyclerView()
        viewModel.startObserving()
    }

    private fun setUpRecyclerView() {
        listAdapter = BrandListAdapter()
        listAdapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        recyclerView.setListItemDecoration(requireContext())
        recyclerView.adapter = listAdapter
    }

    private fun BrandListViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        brandsList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
        exploreBrand.observe(viewLifecycleOwner, EventObserver {
            startFilteredList(it)
        })
    }

    private fun startFilteredList(brandId: String) = findNavController().navigate(
        ContainerViewDirections.navigateToFilterView(brandId)
    )
}