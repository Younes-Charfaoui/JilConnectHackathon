package dz.jilconnect.dipannini

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dz.jilconnect.dipannini.webservices.NetworkLayer
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_profile)
        super.onCreate(savedInstanceState)
        val networkLayer = NetworkLayer()
        CoroutineScope(Dispatchers.IO).launch {
            val id = PreferencesManager(this@ProfileActivity).userId
            val response = networkLayer.getUserData(id)
            CoroutineScope(Dispatchers.Main).launch {
                profileProgress.visibility = View.GONE
                mainLayout.visibility = View.VISIBLE
                if (response.activated) {
                    activateAccountText.visibility = View.GONE
                }
                emailProfile.text = response.email
                nameProfile.text = response.name
                phoneProfile.text = response.phone
                locationProfile.text = response.location
            }
        }
    }
}
