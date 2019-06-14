package dz.jilconnect.dipannini

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import dz.jilconnect.dipannini.webservices.NetworkLayer
import kotlinx.android.synthetic.main.activity_auth.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {

    private var login = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        if (!PreferencesManager(this@AuthActivity).userId.isEmpty()) {
            startActivity(Intent(this@AuthActivity, MainActivity::class.java))
            finish()
        }

        otherActionTextView.setOnClickListener {
            login = !login
            if (login) {
                signButton.text = "Login"
                otherActionTextView.text = "Sign Up"
                titleText.text = "Login"
                nameContainer.visibility = View.GONE
            } else {
                signButton.text = "Sign Up"
                otherActionTextView.text = "Login"
                nameContainer.visibility = View.VISIBLE
                titleText.text = "Sign Up"
            }
        }

        signButton.setOnClickListener {
            val networkLayer = NetworkLayer()
            if (!login) {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = networkLayer.register(
                        nameEdit.text.toString(),
                        emailEdit.text.toString(),
                        passwordEdit.text.toString()
                    )
                    PreferencesManager(this@AuthActivity).userId = response.id
                    startActivity(Intent(this@AuthActivity, MainActivity::class.java))
                }
            } else {

            }
        }
    }
}
