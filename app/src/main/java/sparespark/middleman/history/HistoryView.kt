package sparespark.middleman.history

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import sparespark.middleman.R
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.databinding.FragmentHistoryListBinding
import sparespark.middleman.home.HomeActivityInteract

class HistoryView : Fragment(),
    ViewBindingHolder<FragmentHistoryListBinding> by ViewBindingHolderImpl() {
    private lateinit var viewInteract: HomeActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = initBinding(FragmentHistoryListBinding.inflate(layoutInflater), this@HistoryView) {

        setupViewInteract()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        destroyBinding()
    }

    private fun setupViewInteract() {
        viewInteract = activity as HomeActivityInteract
        viewInteract.updateHeaderTitle(getString(R.string.history))
        viewInteract.updateHeaderSubTitle("0 ${getString(R.string.orders)}")
    }
}