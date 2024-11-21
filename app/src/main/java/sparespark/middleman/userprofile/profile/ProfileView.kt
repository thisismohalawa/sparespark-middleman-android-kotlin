package sparespark.middleman.userprofile.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.menu.MENU_CACHE
import sparespark.middleman.core.menu.MENU_HISTORY
import sparespark.middleman.core.setupListItemDecoration
import sparespark.middleman.core.wrapper.EventObserver
import sparespark.middleman.data.model.ProfileMenu
import sparespark.middleman.databinding.ProfileViewBinding
import sparespark.middleman.home.HomeActivityInteract
import sparespark.middleman.userprofile.UserEvent
import sparespark.middleman.userprofile.profile.buildlogic.ProfileViewInjector

class ProfileView : Fragment(),
    ViewBindingHolder<ProfileViewBinding> by ViewBindingHolderImpl() {

    private lateinit var menuAdapter: MenuAdapter
    private lateinit var viewModel: ProfileViewModel
    private lateinit var viewInteract: HomeActivityInteract

    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = initBinding(ProfileViewBinding.inflate(layoutInflater), this@ProfileView) {
        setupViewInteract()
        setupViewModel()
        viewModel.startObserving()
    }

    private fun setupViewInteract() {
        viewInteract = activity as HomeActivityInteract
        viewInteract.updateHeaderTitle(getString(R.string.profile))
        viewInteract.updateHeaderSubTitle(getString(R.string.update_profile))
    }

    private fun setupMenuAdapter(list: List<ProfileMenu>) {
        menuAdapter = MenuAdapter(list = list)
        menuAdapter.event.observe(viewLifecycleOwner) {
            viewModel.handleEvent(it)
        }
        binding?.recMenuList?.apply {
            setupListItemDecoration(context)
            adapter = menuAdapter
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@ProfileView,
            factory = ProfileViewInjector(requireActivity().application).provideViewModelFactory()
        )[ProfileViewModel::class.java]
        viewModel.handleEvent(UserEvent.GetCurrentUser)
    }


    private fun isProfileView(): Boolean =
        findNavController().currentDestination?.id == R.id.profileView

    private fun ProfileViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(context))
        }
        updated.observe(viewLifecycleOwner) {
            viewInteract.restartHomeActivity()
        }
        loginAttempt.observe(viewLifecycleOwner) {
            viewInteract.startAuthActivity()
        }
        menuList.observe(viewLifecycleOwner) {
            setupMenuAdapter(it)
        }
        editMenu.observe(viewLifecycleOwner, EventObserver {
            if (isProfileView()) when (it) {
                MENU_HISTORY -> findNavController().navigate(ProfileViewDirections.navigateToHistoryList())
                MENU_CACHE -> findNavController().navigate(ProfileViewDirections.navigateToCachePreference())
            }
        })
    }
}