package sk.tuke.hip_project

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.widget.ImageButton
import android.widget.Button
import android.net.Uri
import android.widget.Toast

class AboutUsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about_us)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Optional: close AboutUsActivity so user can't come back here with back button
        }

        // Send email
        val contactButton = findViewById<Button>(R.id.contactButton)
        contactButton.setOnClickListener {
            val emailIntent = Intent(Intent.ACTION_SEND).apply {
                type = "message/rfc822"
                putExtra(Intent.EXTRA_EMAIL, arrayOf("michal.jiricek@student.tuke.sk", "jakub.szabados@student.tuke.sk", "dominik.vrazel@student.tuke.sk"))
                putExtra(Intent.EXTRA_SUBJECT, "Otazka alebo pripomienka")
            }

            // Check if there's an email app to handle the intent
            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(emailIntent)
            } else {
                // Optional: Show a toast if no email client is installed
                Toast.makeText(this, "No email app found. Please install an email client.", Toast.LENGTH_LONG).show()
            }
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}
