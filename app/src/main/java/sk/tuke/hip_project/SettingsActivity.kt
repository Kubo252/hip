package sk.tuke.hip_project

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout

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

        val mainLayout = findViewById<ConstraintLayout>(R.id.main)
        val logoImageView = findViewById<ImageView>(R.id.logoImageView) // Changed to ImageView
        val bottomNavBar = findViewById<LinearLayout>(R.id.bottomNavBar)
        val textView1 = findViewById<TextView>(R.id.textView)
        val textView2 = findViewById<TextView>(R.id.textView2)
        val darkModeSwitch = findViewById<Switch>(R.id.switch1)
        val searchHeader = findViewById<TextView>(R.id.searchHeader)

        // Set up dark mode toggle
        darkModeSwitch.setOnCheckedChangeListener { _, isChecked ->
            try {
                if (isChecked) {
                    // Enable dark mode
                    mainLayout.setBackgroundColor(resources.getColor(android.R.color.black))
                    textView1.setTextColor(resources.getColor(android.R.color.white))
                    textView2.setTextColor(resources.getColor(android.R.color.white))
                    searchHeader.setTextColor(resources.getColor(android.R.color.white)) // Update text color
                    bottomNavBar.setBackgroundColor(resources.getColor(android.R.color.white))
                    contactButton.setTextColor(resources.getColor(android.R.color.black))
                    logoImageView.setImageResource(R.drawable.logo_dark)
                    backButton.setImageResource(R.drawable.back_button_dark)
                } else {
                    // Disable dark mode (revert to default)
                    mainLayout.setBackgroundColor(resources.getColor(android.R.color.white))
                    textView1.setTextColor(resources.getColor(android.R.color.black))
                    textView2.setTextColor(resources.getColor(android.R.color.black))
                    searchHeader.setTextColor(resources.getColor(android.R.color.black)) // Revert text color
                    bottomNavBar.setBackgroundColor(resources.getColor(android.R.color.black))
                    contactButton.setTextColor(resources.getColor(android.R.color.white))
                    logoImageView.setImageResource(R.drawable.logo)
                    backButton.setImageResource(R.drawable.back_button)
                }
            } catch (e: Exception) {
                Toast.makeText(this, "Error changing theme: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Helper method to check if a drawable resource exists
    private fun getDrawableResourceId(name: String): Int {
        return resources.getIdentifier(name, "drawable", packageName)
    }
}