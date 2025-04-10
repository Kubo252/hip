package sk.tuke.hip_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        // Find the back button
        val backButton = findViewById<ImageButton>(R.id.back_button)

        // Set click listener to navigate back
        backButton.setOnClickListener {
            finish() // Close the current activity and return to the previous one
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
    }
}