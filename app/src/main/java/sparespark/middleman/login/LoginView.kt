package sparespark.middleman.login

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import sparespark.middleman.R
import sparespark.middleman.common.SIGN_IN_REQUEST_CODE
import sparespark.middleman.common.displayAsLinedBoldStyle
import sparespark.middleman.common.enable
import sparespark.middleman.common.visible
import sparespark.middleman.login.buildlogic.LoginInjector
import sparespark.middleman.model.LoginResult
import sparespark.middleman.product.ProductActivity
import sparespark.middleman.product.ProductActivityInteract

class LoginView : Fragment() {

    private lateinit var viewModel: LoginViewModel
    private lateinit var btnLogin: AppCompatButton
    private lateinit var txtSign: TextView
    private lateinit var txtDeleteAcc: TextView
    private lateinit var viewCommunicate: ProductActivityInteract

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.login_view, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnLogin = view.findViewById(R.id.btn_login)
        txtSign = view.findViewById(R.id.txt_sign_status)
        txtDeleteAcc = view.findViewById(R.id.txt_delete_acc)
    }

    override fun onStart() {
        super.onStart()
        viewCommunicate = activity as ProductActivity
        viewModel = ViewModelProvider(
            owner = this,
            factory = LoginInjector(requireActivity().application).provideLoginViewModelFactory()
        )[LoginViewModel::class.java]
        viewModel.handleEvent(LoginEvent.OnStart)
        viewModel.startObserving()
        setUpClickListeners()
    }

    private fun setUpClickListeners() {
        txtDeleteAcc.displayAsLinedBoldStyle(getString(R.string.delete_my_account))
        txtDeleteAcc.setOnClickListener {
            viewModel.handleEvent(LoginEvent.OnDeleteAccTextClick)
        }
        btnLogin.setOnClickListener {
            viewModel.handleEvent(LoginEvent.OnAuthButtonClick)
        }
    }

    private fun LoginViewModel.startObserving() {
        loading.observe(viewLifecycleOwner) {
            viewCommunicate.updateLoadingView(it)
        }
        error.observe(viewLifecycleOwner) {
            viewCommunicate.showError(it.asString(requireContext()))
        }
        signInStatusText.observe(viewLifecycleOwner) {
            txtSign.text = it.asString(requireContext())
        }
        deleteTextVisibleStatus.observe(viewLifecycleOwner) {
            txtDeleteAcc.visible(it)
        }
        authButtonText.observe(viewLifecycleOwner) {
            btnLogin.text = it.asString(requireContext())
        }
        authAttempt.observe(viewLifecycleOwner) {
            startSignInFlow()
        }
        updated.observe(viewLifecycleOwner) {
            if (it) navigateToContainerView()
        }
    }

    private fun startSignInFlow() {
        val gso: GoogleSignInOptions =
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build()

        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        val signInIntent: Intent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, SIGN_IN_REQUEST_CODE)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        try {
            var userToken: String? = null
            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)

            val account: GoogleSignInAccount? = task.getResult(ApiException::class.java)
            if (account != null) {
                userToken = account.idToken
                viewModel.handleEvent(
                    LoginEvent.OnGoogleSignInResult(
                        LoginResult(requestCode, userToken)
                    )
                )
            }

        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun navigateToContainerView() = findNavController().navigate(
        LoginViewDirections.navigateToContainerView()
    )
}