package sparespark.middleman.home

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import sparespark.middleman.R
import sparespark.middleman.auth.AuthActivity
import sparespark.middleman.core.actionLaunchWhatsApp
import sparespark.middleman.core.makeToast
import sparespark.middleman.core.visible
import sparespark.middleman.databinding.ActivityHomeBinding
import sparespark.middleman.home.buildlogic.HomeActivityInjector

class HomeActivity : HomeActivityUgly(),
    HomeActivityInteract {

    private lateinit var navController: NavController
    private lateinit var mBinding: ActivityHomeBinding
    private lateinit var viewModel: HomeActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBindingUtil()
        setUpNavigationController()
        setupViewModel()
        viewModel.startObserving()
        checkForNewUpdates()
    }

    private fun setupDataBindingUtil() {
        mBinding = DataBindingUtil.setContentView(this@HomeActivity, R.layout.activity_home)
    }


    private fun setUpNavigationController() = try {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }


    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            owner = this, HomeActivityInjector(this.application).provideViewModelFactory()
        )[HomeActivityViewModel::class.java]
        viewModel.handleEvent(HomeActivityEvent.OnStartCheckUser)
    }

    private fun HomeActivityViewModel.startObserving() {
        loading.observe(this@HomeActivity) {
            mBinding.progressCircular.visible(it)
        }
        error.observe(this@HomeActivity) {
            displayToast(it.asString(this@HomeActivity))
        }
        loginAttempt.observe(this@HomeActivity) {
            startAuthActivity()
        }
    }

    override fun displayToast(msg: String?) = makeToast(msg ?: getString(R.string.connecting))

    override fun updateProgressBarVisible(isVisible: Boolean) {
        mBinding.progressCircular.visible(isVisible)
    }


    override fun updateHeaderTitle(title: String) {
        mBinding.headerLayout.txtTitle.text = title
    }

    override fun updateHeaderSubTitle(subTitle: String) {
        mBinding.headerLayout.txtSubtitle.text = subTitle
    }

    override fun startAuthActivity() = startActivity(
        Intent(this@HomeActivity, AuthActivity::class.java)
    ).also { this@HomeActivity.finish() }

    override fun restartHomeActivity() = restartActivity()

    override fun finishHomeActivity() = finish()

    override fun launchWhatsApp(content: String) {
        this@HomeActivity.actionLaunchWhatsApp(
            content = content
        )
    }

}
