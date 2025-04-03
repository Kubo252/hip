package sk.tuke.hip_project

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MenuActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        // Initialize buttons
        val navigationButton = findViewById<Button>(R.id.navigationButton)
        val mapButton = findViewById<Button>(R.id.mapButton)
        val webButton = findViewById<Button>(R.id.webButton)
        val aboutButton = findViewById<Button>(R.id.aboutButton)
        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        val closeButton = findViewById<ImageButton>(R.id.closeButton)

        // Set up button click listeners
        navigationButton.setOnClickListener {
            Toast.makeText(this, "Navigácia clicked", Toast.LENGTH_SHORT).show()
            // Add navigation functionality here
        }

        mapButton.setOnClickListener {
            val mapUrl = "https://at.tuke.sk/map"
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(mapUrl))
            startActivity(intent)
        }

        webButton.setOnClickListener {
            Toast.makeText(this, "TUKE web clicked", Toast.LENGTH_SHORT).show()
            // Add web functionality here
        }

        aboutButton.setOnClickListener {
            val intent = Intent(this, AboutUsActivity::class.java)
            startActivity(intent)
        }

        settingsButton.setOnClickListener {
            Toast.makeText(this, "Settings clicked", Toast.LENGTH_SHORT).show()
            // Add settings functionality here
        }

        closeButton.setOnClickListener {
            // Close the app
            finish()
        }
    }
}