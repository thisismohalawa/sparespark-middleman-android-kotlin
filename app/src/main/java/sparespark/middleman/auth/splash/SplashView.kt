package sparespark.middleman.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.auth.AuthActivityInteract
import sparespark.middleman.auth.BaseAuthView
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.databinding.FragmentSplashBinding

class SplashView : BaseAuthView(), View.OnClickListener,
    ViewBindingHolder<FragmentSplashBinding> by ViewBindingHolderImpl() {
    private lateinit var viewInteract: AuthActivityInteract

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.btn_signup -> if (findNavController().currentDestination?.id == R.id.splashView) findNavController().navigate(
                SplashViewDirections.navigateToSignup()
            ) else Unit

            R.id.btn_login -> if (findNavController().currentDestination?.id == R.id.splashView) findNavController().navigate(
                SplashViewDirections.navigateToLogin()
            ) else Unit
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = initBinding(FragmentSplashBinding.inflate(layoutInflater), this@SplashView) {
        viewInteract = activity as AuthActivityInteract
        viewInteract.updateProgressBarVisible(isVisible = false)
        binding?.apply {
            btnLogin.setOnClickListener(this@SplashView)
            btnSignup.setOnClickListener(this@SplashView)
        }
    }
}