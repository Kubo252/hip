package sk.tuke.hip_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ImageView
import android.content.Intent
import sk.tuke.hip_project.R

class MainActivity : AppCompatActivity() {

    private lateinit var dots: Array<View>
    private var dotCount = 0
    private val handler = Handler(Looper.getMainLooper())
    private val ANIMATION_DELAY = 1000 // 1 second between each animation frame
    private val LOADING_DURATION = 3200 // Total loading time in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up logo
        val logoImageView = findViewById<ImageView>(R.id.logoImageView)
        logoImageView.setImageResource(R.drawable.tuk_end_logo)

        // Initialize dots array
        dots = arrayOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3),
            findViewById(R.id.dot4)
        )

        // Start loading animation
        startLoadingAnimation()

        // Navigate to menu activity after loading completes
        handler.postDelayed({
            navigateToMainMenu()
        }, LOADING_DURATION.toLong())
    }

    private fun startLoadingAnimation() {
        val animationRunnable = object : Runnable {
            override fun run() {
                updateLoadingDots()
                handler.postDelayed(this, ANIMATION_DELAY.toLong())
            }
        }
        handler.post(animationRunnable)
    }

    private fun updateLoadingDots() {
        dotCount = (dotCount % 4) + 1 // Cycle through 1-4 dots

        // Update visibility of dots based on current count
        for (i in dots.indices) {
            dots[i].visibility = if (i < dotCount) View.VISIBLE else View.INVISIBLE
        }
    }

    private fun navigateToMainMenu() {
        val intent = Intent(this, MenuActivity::class.java)
        startActivity(intent)
        finish() // Close this activity so user can't go back to splash screen
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Clean up handler
    }
}