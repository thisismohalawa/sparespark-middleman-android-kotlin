package sparespark.middleman.checkroom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.checkroom.buildlogic.CheckroomListInjector
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.displayAsLinedBoldStyle
import sparespark.middleman.core.enable
import sparespark.middleman.core.relaunchCurrentView
import sparespark.middleman.core.setupListItemDecoration
import sparespark.middleman.core.showConfirmationDialog
import sparespark.middleman.core.toFullPrice
import sparespark.middleman.core.wrapper.EventObserver
import sparespark.middleman.databinding.FragmentCheckroomBinding
import sparespark.middleman.home.HomeActivityInteract

class CheckroomView : Fragment(),
    ViewBindingHolder<FragmentCheckroomBinding> by ViewBindingHolderImpl() {

    private lateinit var listAdapter: CheckroomListAdapter
    private lateinit var viewModel: CheckroomViewModel
    private lateinit var viewInteract: HomeActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = initBinding(FragmentCheckroomBinding.inflate(layoutInflater), this@CheckroomView) {
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
        viewInteract.updateHeaderTitle(getString(R.string.checkroom))
    }

    private fun setupViewInputs() {
        binding?.apply {
            btnBuy.enable(enabled = false)
            whatsappTxt.apply {
                displayAsLinedBoldStyle(
                    content = getString(R.string.continue_via_whatsapp)
                )
                setOnClickListener {
                    viewModel.handleEvent(CheckroomEvent.OnWhatsAppBuyBtnClick)
                }
            }
            itemTaxPrice.apply {
                titleTxt.text = 0.0.toFullPrice()
                labelTxt.text = getString(R.string.tax)
            }
            itemTotalPrice.apply {
                titleTxt.text = 0.0.toFullPrice()
                labelTxt.text = getString(R.string.total_price)
            }
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@CheckroomView,
            factory = CheckroomListInjector(requireActivity().application).provideViewModelFactory()
        )[CheckroomViewModel::class.java]
        viewModel.handleEvent(CheckroomEvent.GetProducts)
    }

    private fun setupRecyclerView() {
        listAdapter = CheckroomListAdapter()
        binding?.recListFragment?.apply {
            setupListItemDecoration(requireContext())
            adapter = listAdapter
        }
        listAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
    }

    private fun CheckroomViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(requireContext()))
        }
        updated.observe(viewLifecycleOwner) {
            relaunchCurrentView()
        }
        editProduct.observe(viewLifecycleOwner, EventObserver {
            if (isDestination()) findNavController().navigate(
                CheckroomViewDirections.navigateToProductDetails(productId = it)
            )
        })
        totalPrice.observe(viewLifecycleOwner) {
            binding?.itemTotalPrice?.titleTxt?.text = it?.toFullPrice()
        }
        productList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
            viewInteract.updateHeaderSubTitle("${it.size} ${getString(R.string.items)}")
        }
        supportAttempt.observe(viewLifecycleOwner, EventObserver {
            context?.showConfirmationDialog(
                title = getString(R.string.continue_via_whatsapp),
                positiveText = getString(R.string.confirm),
                action = {
                    viewInteract.launchWhatsApp(content = it)
                }
            )
        })
    }

    private fun isDestination() = findNavController().currentDestination?.id == R.id.cartListView

}