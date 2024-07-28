package sparespark.middleman.product.productdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import sparespark.middleman.R
import sparespark.middleman.common.loadImgUrl
import sparespark.middleman.common.toFullPrice
import sparespark.middleman.common.visible
import sparespark.middleman.model.Product
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract
import sparespark.middleman.product.productdetails.adapter.ColorListAdapter
import sparespark.middleman.product.productdetails.adapter.RelatedListAdapter
import sparespark.middleman.product.productdetails.buildlogic.ProductDetailInjector

class ProductDetailView : Fragment() {

    private lateinit var progressBar: ProgressBar
    private lateinit var productImg: ImageView
    private lateinit var descTxt: TextView
    private lateinit var titleTxt: TextView
    private lateinit var cartTxt: TextView
    private lateinit var priceTxt: TextView
    private lateinit var toolbar: androidx.appcompat.widget.Toolbar
    private lateinit var viewModel: ProductViewModel
    private lateinit var viewCommunicate: ProductActivityInteract
    private lateinit var colorListAdapter: ColorListAdapter
    private lateinit var relatedListAdapter: RelatedListAdapter
    private lateinit var colorRecyclerView: RecyclerView
    private lateinit var relatedRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_product_detail, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar = view.findViewById(R.id.toolbar)
        descTxt = view.findViewById(R.id.desc_txt)
        titleTxt = view.findViewById(R.id.title_txt)
        cartTxt = view.findViewById(R.id.cart_txt)
        priceTxt = view.findViewById(R.id.price_txt)
        progressBar = view.findViewById(R.id.progress_circular)
        productImg = view.findViewById(R.id.product_img)
        colorRecyclerView = view.findViewById(R.id.rec_list_color)
        relatedRecyclerView = view.findViewById(R.id.rec_list_related)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        colorRecyclerView.adapter = null
        relatedRecyclerView.adapter = null
    }

    override fun onStart() {
        super.onStart()
        viewCommunicate = activity as ProductActivity
        viewModel = ViewModelProvider(
            owner = this,
            factory = ProductDetailInjector(requireActivity().application).provideProductViewModelFactory()
        )[ProductViewModel::class.java]
        viewModel.handleEvent(ProductDetailEvent.OnStartGetProduct)
        viewModel.handleEvent(ProductDetailEvent.GetBrandName)
        viewModel.startObserving()
        setUpColorRecyclerList()
        setupRelatedRecyclerList()
        setUpClickListener()
    }

    private fun setUpColorRecyclerList() {
        colorListAdapter = ColorListAdapter()
        colorListAdapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        colorRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = colorListAdapter
        }
    }

    private fun setupRelatedRecyclerList() {
        relatedListAdapter = RelatedListAdapter()
        relatedListAdapter.event.observe(
            viewLifecycleOwner,
            Observer {
                viewModel.handleEvent(it)
            }
        )
        relatedRecyclerView.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = relatedListAdapter
        }
    }

    private fun setUpClickListener() {
        cartTxt.setOnClickListener {
            viewModel.handleEvent(ProductDetailEvent.OnCartTextClick)
        }
    }

    private fun ProductViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        product.observe(viewLifecycleOwner) {
            bindProduct(product = it)
        }
        brandToolbarText.observe(viewLifecycleOwner) {
            toolbar.title = it
        }
        colorList.observe(viewLifecycleOwner) {
            colorListAdapter.submitList(it)
        }
        relatedList.observe(viewLifecycleOwner) {
            relatedListAdapter.submitList(it)
        }
        cartState.observe(viewLifecycleOwner) {
            if (it == true) cartTxt.text = getString(R.string.remote_from_bag)
            else cartTxt.text = getString(R.string.add_to_bag)
        }
    }

    private fun bindProduct(product: Product) {
        titleTxt.text = product.title
        descTxt.text = product.des
        priceTxt.text = product.price.toFullPrice()
        productImg.loadImgUrl(imgUrl = product.imgUrl,
            userRandomColor = true,
            onResourceReadyAction = {
                progressBar.visible(false)
            })
    }
}