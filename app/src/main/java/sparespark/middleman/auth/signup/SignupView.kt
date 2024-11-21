package sparespark.middleman.auth.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.auth.AuthActivityInteract
import sparespark.middleman.auth.AuthEvent
import sparespark.middleman.auth.BaseAuthView
import sparespark.middleman.auth.signup.buildlogic.SignUpInjector
import sparespark.middleman.core.binding.ViewBindingHolder
import sparespark.middleman.core.binding.ViewBindingHolderImpl
import sparespark.middleman.core.visible
import sparespark.middleman.databinding.FragmentAuthBinding

class SignupView : BaseAuthView(), View.OnClickListener,
    ViewBindingHolder<FragmentAuthBinding> by ViewBindingHolderImpl() {

    private lateinit var viewInteract: AuthActivityInteract
    private lateinit var viewModel: SignUpViewModel

    override fun onClick(p0: View?) {
        if (p0?.id == R.id.btn_auth) viewModel.handleEvent(
            AuthEvent.OnSignupBtnClick(
                email = binding?.itemEmail?.edText?.text?.trim().toString(),
                pass = binding?.itemPassword?.edText?.text?.trim().toString()
            )
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        destroyBinding()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = initBinding(FragmentAuthBinding.inflate(layoutInflater), this@SignupView) {
        setupViewInteract()
        setupViews()
        setupViewModel()
        viewModel.startObserving()
    }

    private fun setupViewInteract() {
        viewInteract = activity as AuthActivityInteract
    }

    private fun setupViews() {
        binding?.apply {
            btnGoogleLogin.visible(isVisible = false)
            btnAuth.text = getString(R.string.signup)
            btnAuth.setOnClickListener(this@SignupView)
            itemEmail.edText.setUserEmailInput(
                inputLayout = itemEmail.textInputLayout
            )
            itemPassword.edText.setUserPasswordInput(
                inputLayout = itemPassword.textInputLayout
            )
        }
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this@SignupView,
            factory = SignUpInjector(requireActivity().application).provideViewModelFactory()
        )[SignUpViewModel::class.java]
    }

    private fun SignUpViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewInteract.updateProgressBarVisible(it)
        }
        error.observe(viewLifecycleOwner) {
            viewInteract.displayToast(it.asString(context) ?: getString(R.string.connecting))
        }
        emailTxtValidateState.observe(viewLifecycleOwner) {
            binding?.itemEmail?.textInputLayout?.error = getString(R.string.invalid)
        }
        passwordTxtValidateState.observe(viewLifecycleOwner) {
            binding?.itemPassword?.textInputLayout?.error = getString(R.string.invalid)
        }
        updated.observe(viewLifecycleOwner) {
            findNavController().navigate(SignupViewDirections.navigateToLogin())
        }
    }
}