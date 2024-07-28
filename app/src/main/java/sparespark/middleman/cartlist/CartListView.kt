package sparespark.middleman.cartlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.addCallback
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import sparespark.middleman.R
import sparespark.middleman.cartlist.buildlogic.CartListInjector
import sparespark.middleman.common.EventObserver
import sparespark.middleman.common.displayAsLinedBoldStyle
import sparespark.middleman.common.enable
import sparespark.middleman.common.relaunchCurrentView
import sparespark.middleman.common.setListItemDecoration
import sparespark.middleman.model.Product
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract

class CartListView : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var listAdapter: CartListAdapter
    private lateinit var viewModel: CartListViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<View>
    private lateinit var cartFrameLayout: FrameLayout
    private lateinit var viewCommunicate: ProductActivityInteract
    private lateinit var totalText: TextView
    private lateinit var whatsAppText: TextView
    private lateinit var buyButton: AppCompatButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_cart_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.rec_list_fragment)
        cartFrameLayout = view.findViewById(R.id.bottom_sheet_cart_details)
        totalText = view.findViewById(R.id.txt_total)
        whatsAppText = view.findViewById(R.id.txt_whatsapp)
        buyButton = view.findViewById(R.id.btn_buy_now)
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
            factory = CartListInjector(requireActivity().application).provideCartListViewModelFactory()
        )[CartListViewModel::class.java]
        viewModel.handleEvent(CartListEvent.GetCartProducts)
        setUpRecyclerView()
        setUpBottomSheetBehavioral()
        setUpClickListener()
        viewModel.startObserving()
    }

    private fun setUpClickListener() {
        buyButton.enable(false)
        whatsAppText.displayAsLinedBoldStyle(getString(R.string.continue_whatsapp))
        whatsAppText.setOnClickListener {
            viewModel.handleEvent(CartListEvent.OnSupportTextClick)
        }

        buyButton.setOnClickListener {
            viewModel.handleEvent(CartListEvent.OnBuyButtonClick)
        }
        requireActivity().onBackPressedDispatcher.addCallback(this@CartListView) {
            viewModel.handleEvent(CartListEvent.OnBackButtonPressed)
        }
    }

    private fun setUpBottomSheetBehavioral() {
        bottomSheetBehavior = BottomSheetBehavior.from(cartFrameLayout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun setUpRecyclerView() {
        listAdapter = CartListAdapter()
        listAdapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        recyclerView.adapter = listAdapter
        recyclerView.setListItemDecoration(requireContext())
    }

    private fun CartListViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        updated.observe(viewLifecycleOwner) {
            relaunchCurrentView()
        }
        bottomSheetViewState.observe(viewLifecycleOwner) {
            bottomSheetBehavior.state = it
        }
        actionBackStack.observe(viewLifecycleOwner) {
            findNavController().popBackStack()
        }
        totalPrice.observe(viewLifecycleOwner) {
            totalText.text = it.toString()
        }
        productList.observe(viewLifecycleOwner) {
            listAdapter.submitList(it)
        }
        exploreProduct.observe(viewLifecycleOwner, EventObserver {
            startProductDetailWithArgs(it)
        })
        supportAttempt.observe(viewLifecycleOwner, EventObserver {
            viewCommunicate.launchWhatsApp(
                dTitle = getString(R.string.continue_whatsapp),
                dPositiveTitle = getString(R.string.ok),
                wContent = it
            )
        })
        buyAttempt.observe(viewLifecycleOwner) {
            viewCommunicate.launchWhatsApp(
                dTitle = getString(R.string.not_impl_continue_whatsapp),
                dPositiveTitle = getString(R.string.ok),
                wContent = ""
            )
        }
    }

    private fun startProductDetailWithArgs(product: Product) = findNavController().navigate(
        CartListViewDirections.navigateToProductDetailView(product)
    )
}