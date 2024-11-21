package sparespark.middleman.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import sparespark.middleman.R
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.enable
import sparespark.middleman.core.loadImgUrl
import sparespark.middleman.core.toFullPrice
import sparespark.middleman.data.model.product.Product
import sparespark.middleman.databinding.FragmentProductDetailBinding
import sparespark.middleman.home.HomeActivityInteract
import sparespark.middleman.productdetails.adapter.RelatedColorListAdapter
import sparespark.middleman.productdetails.adapter.RelatedUrlListAdapter
import sparespark.middleman.productdetails.buildlogic.ProductDetailInjector

class ProductDetailView : Fragment(),
    ViewBindingHolder<FragmentProductDetailBinding> by ViewBindingHolderImpl() {

    private lateinit var viewModel: ProductDetailsViewModel
    private lateinit var viewInteract: HomeActivityInteract
    private lateinit var colorListAdapter: RelatedColorListAdapter
    private lateinit var relatedListAdapter: RelatedUrlListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View =
        initBinding(FragmentProductDetailBinding.inflate(layoutInflater), this@ProductDetailView) {
            setupViewInteract()
            setupViewInputs()
            setupViewModel()
            setupRelatedColorAdapterList()
            setupRelatedUrlAdapterList()
            viewModel.setupStatesObserver()
            setupClickListener()
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
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@ProductDetailView,
            factory = ProductDetailInjector(requireActivity().application).provideViewModelFactory()
        )[ProductDetailsViewModel::class.java]
        viewModel.handleEvent(ProductDetailEvent.OnStartGetProduct)
    }

    private fun setupRelatedColorAdapterList() {
        colorListAdapter = RelatedColorListAdapter()
        colorListAdapter.event.observe(
            viewLifecycleOwner
        ) {
            viewModel.handleEvent(it)
        }
        binding?.recListColor?.adapter = colorListAdapter
    }

    private fun setupRelatedUrlAdapterList() {
        relatedListAdapter = RelatedUrlListAdapter()
        relatedListAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
        binding?.recListRelated?.adapter = relatedListAdapter
    }

    private fun ProductDetailsViewModel.setupStatesObserver() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(context))
        }
        cartState.observe(viewLifecycleOwner) {
            binding?.btnCart?.text = if (it == true) getString(R.string.remote_from_checkroom)
            else getString(R.string.add_to_checkroom)
        }
        colorList.observe(viewLifecycleOwner) {
            colorListAdapter.submitList(it)
        }
        relatedList.observe(viewLifecycleOwner) {
            relatedListAdapter.submitList(it)
        }
        product.observe(viewLifecycleOwner) {
            bindProduct(it)
        }
    }

    private fun bindProduct(product: Product) {
        binding?.apply {
            titleTxt.text = getString(R.string.product) + ": " + product.title
            descTxt.text = getString(R.string.details) + ": " + product.des
            priceTxt.text = product.price.toFullPrice()
            ratingBar.rating = product.rating.toFloat()
            productImg.loadImgUrl(
                imgUrl = product.imgUrl,
                useRandomColor = false,
                onResourceReadyAction = {


                }
            )
            btnCart.enable(enabled = product.activated)
        }
    }

    private fun setupClickListener() {
        binding?.apply {
            btnCart.setOnClickListener {
                viewModel.handleEvent(ProductDetailEvent.OnCartTextClick)
            }
        }
    }
}