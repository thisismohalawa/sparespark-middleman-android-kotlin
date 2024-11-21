package sparespark.middleman.home

import android.content.Intent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability
import sparespark.middleman.R
import sparespark.middleman.core.UPDATE_REQUEST_CODE
import sparespark.middleman.core.makeToast

open class HomeActivityUgly : AppCompatActivity() {


    protected fun HomeActivity.restartActivity() {
        val intent: Intent? = applicationContext.packageManager
            .getLaunchIntentForPackage(applicationContext.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }

    protected fun androidx.appcompat.widget.Toolbar.setToolbarTitleFont() = try {
        for (i in 0 until childCount) {
            val view = getChildAt(i)
            if (view is TextView && view.text == title) {
                view.typeface = ResourcesCompat.getFont(context, R.font.tango_bold)
                break
            }
        }
    } catch (ex: Exception) {
        ex.printStackTrace()
    }

    protected fun checkForNewUpdates() = try {
        val appUpdateManager = AppUpdateManagerFactory.create(this@HomeActivityUgly)
        appUpdateManager.appUpdateInfo.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && it.isUpdateTypeAllowed(
                    AppUpdateType.IMMEDIATE
                )
            ) {
                appUpdateManager.startUpdateFlowForResult(
                    it, AppUpdateType.IMMEDIATE, this@HomeActivityUgly,
                    UPDATE_REQUEST_CODE
                )
            }
        }.addOnFailureListener {
            //  makeToast(getString(R.string.error_update_version))
        }

    } catch (ex: Exception) {
        makeToast(getString(R.string.error_app_updates))
    }
}