package sparespark.middleman

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jakewharton.threetenabp.AndroidThreeTen

class MiddlemanApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this@MiddlemanApp)
        AndroidThreeTen.init(this@MiddlemanApp)
    }
}