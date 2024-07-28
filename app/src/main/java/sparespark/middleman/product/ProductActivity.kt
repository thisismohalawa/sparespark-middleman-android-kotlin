package sparespark.middleman.product

import android.os.Bundle
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import sparespark.middleman.R
import sparespark.middleman.common.UPDATE_REQUEST_CODE
import sparespark.middleman.common.actionLaunchWhatsApp
import sparespark.middleman.common.makeToast
import sparespark.middleman.common.showConfirmationDialog
import sparespark.middleman.common.visible

class ProductActivity : AppCompatActivity(), ProductActivityInteract {
    private lateinit var navController: NavController
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        progressBar = findViewById(R.id.progress_bar)
        setUpNavigationController()
        checkForNewUpdates()
    }

    override fun updateLoadingView(isLoading: Boolean) {
        progressBar.visible(isLoading)
    }

    override fun showError(error: String?) = makeToast(error)

    override fun actionFinish() = finish()

    override fun launchWhatsApp(
        dTitle: String,
        dPositiveTitle: String,
        wContent: String
    ) {
        this@ProductActivity.showConfirmationDialog(
            title = dTitle,
            positiveText = dPositiveTitle,
            action = {
                this@ProductActivity.actionLaunchWhatsApp(
                    content = wContent
                )
            }
        )
    }

    private fun setUpNavigationController() = try {
        val navHostFragment: NavHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.findNavController()
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    private fun checkForNewUpdates() = try {
        val appUpdateManager = AppUpdateManagerFactory.create(this@ProductActivity)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it, AppUpdateType.IMMEDIATE, this@ProductActivity,
                    UPDATE_REQUEST_CODE
                )
            }
        }.addOnFailureListener {
           // makeToast(getString(R.string.error_update_version))
        }

    } catch (ex: Exception) {
        makeToast(getString(R.string.error_app_updates))
    }
}
