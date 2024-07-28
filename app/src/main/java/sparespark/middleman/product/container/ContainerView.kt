package sparespark.middleman.product.container

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.bottomappbar.BottomAppBar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import sparespark.middleman.R
import sparespark.middleman.common.EventObserver
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityEvent
import sparespark.middleman.product.ProductActivityInteract
import sparespark.middleman.product.container.buildlogic.ContainerInjector

class ContainerView : Fragment() {

    private lateinit var viewCommunicate: ProductActivityInteract
    private lateinit var viewModel: ContainerViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_container, container, false)

    override fun onStart() {
        super.onStart()
        viewCommunicate = activity as ProductActivity
        viewModel = ViewModelProvider(
            owner = this,
            factory = ContainerInjector(requireActivity().application).provideContainerViewModelFactory()
        )[ContainerViewModel::class.java]
        viewModel.handleEvent(ProductActivityEvent.OnStartCheckUser)
        viewModel.loginAttempt.observe(viewLifecycleOwner, EventObserver {
            navigateToLoginView()
        })

        setUpClickListener()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        val viewPager = view.findViewById<ViewPager>(R.id.view_pager)
        val bottomAppBar = view.findViewById<BottomAppBar>(R.id.bottom_app_bar)
        val floatingBtn = view.findViewById<FloatingActionButton>(R.id.floating_btn)

        val pageAdapter = PageAdapter(childFragmentManager, tabLayout.tabCount)
        viewPager.adapter = pageAdapter
        viewPager.addOnPageChangeListener(object :
            TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            override fun onPageScrollStateChanged(state: Int) {

            }
        })
        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
                when (tab.position) {
                    0 -> {
                    }

                    1 -> {

                    }

                    else -> {

                    }
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })

        floatingBtn.setOnClickListener {
            navigateToCartView()
        }
        bottomAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_settings -> {
                    navigateToPreferenceView()
                    true
                }

                R.id.menu_filtered -> {
                    navigateToFilteredListView()
                    true
                }

                R.id.menu_user -> {
                    navigateToLoginView()
                    true
                }

                else -> false
            }
        }
    }

    private fun navigateToPreferenceView() =
        if (findNavController().currentDestination?.id == R.id.containerView)
            findNavController().navigate(
                ContainerViewDirections.navigateToPreferenceView()
            ) else Unit

    private fun navigateToCartView() =
        if (findNavController().currentDestination?.id == R.id.containerView)
            findNavController().navigate(
                ContainerViewDirections.navigateToCartView()
            ) else Unit

    private fun navigateToFilteredListView() =
        if (findNavController().currentDestination?.id == R.id.containerView)
            findNavController().navigate(
                ContainerViewDirections.navigateToFilterView()
            ) else Unit

    private fun navigateToLoginView() =
        if (findNavController().currentDestination?.id == R.id.containerView)
            findNavController().navigate(
                ContainerViewDirections.navigateToLoginView()
            ) else Unit

    private fun setUpClickListener() {
        val callback = object : OnBackPressedCallback(
            true
        ) {
            override fun handleOnBackPressed() {
                viewCommunicate.actionFinish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }
}