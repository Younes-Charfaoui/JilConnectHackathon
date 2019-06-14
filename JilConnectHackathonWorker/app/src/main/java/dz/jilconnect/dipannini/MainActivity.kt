package dz.jilconnect.dipannini

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.profile_action) {
            startActivity(Intent(this, ProfileActivity::class.java))
            return true
        } else if (item?.itemId == R.id.update_action) {
            startActivity(Intent(this, UpdateProfileActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
