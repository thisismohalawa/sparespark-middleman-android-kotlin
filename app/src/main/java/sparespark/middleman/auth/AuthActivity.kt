package sparespark.middleman.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import sparespark.middleman.R
import sparespark.middleman.core.makeToast
import sparespark.middleman.core.visible
import sparespark.middleman.databinding.ActivityAuthBinding
import sparespark.middleman.home.HomeActivity

class AuthActivity : AppCompatActivity(), AuthActivityInteract {
    private lateinit var aBinding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBindingUtil()
    }

    private fun setupDataBindingUtil() {
        aBinding = DataBindingUtil.setContentView(this@AuthActivity, R.layout.activity_auth)
    }

    override fun updateProgressBarVisible(isVisible: Boolean) {
        aBinding.progressCircular.visible(isVisible)
    }

    override fun displayToast(msg: String) = makeToast(msg)

    override fun startDataActivity() = startActivity(
        Intent(this@AuthActivity, HomeActivity::class.java)
    ).also { this@AuthActivity.finish() }
}