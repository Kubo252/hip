package sk.tuke.hip_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore

class NavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        FirebaseApp.initializeApp(this)
        val db = Firebase.firestore
        setContentView(R.layout.activity_navigation)

        val backButton = findViewById<ImageButton>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
            finish() // Optional: close AboutUsActivity so user can't come back here with back button
        }

        /*val buildingData = hashMapOf(
            "name" to "TUKE hlavná budova",
            "wings" to listOf("L9B", "L9A")
        )*/

        /*db.collection("buildings")
            .add(buildingData)
            .addOnSuccessListener { buildingRef ->
                // Now add a room directly under "rooms" subcollection of this building
                val rooms = listOf(
                    hashMapOf("name" to "L9-A536","alt_name" to "Abydos - laboratórna miestnosť", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A504","alt_name" to "Aurora - prednášková miestnosť", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A512","alt_name" to "Hyperion - laboratórium IoT", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A534","alt_name" to "Romulus - laboratórium CNL", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A532","alt_name" to "Solaris - laboratórna miestnosť", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A514","alt_name" to "Vulcan - laboratórna miestnosť", "wing" to "L9A", "floor" to 9),
                    hashMapOf("name" to "L9-A204","alt_name" to "poslucháreň A204", "wing" to "L9A", "floor" to 2),
                    hashMapOf("name" to "L9-B527","alt_name" to "Caprica - Laboratórium IoT", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B529","alt_name" to "Duna - laboratórna miestnosť", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B526","alt_name" to "Endor - laboratórna miestnosť", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B524","alt_name" to "Kronos - laboratórna miestnosť", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-A538","alt_name" to "Laboratórium OpenLab", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B519/B","alt_name" to "Meridian - laboratórium CNL", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B501","alt_name" to "Nyx - laboratórna miestnosť", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B518","alt_name" to "Orion - katedrová knižnica", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B204","alt_name" to "Zasadacia miestnosť L9 - B204", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "kancelária B515","alt_name" to "", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B216","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B219","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B225","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B226","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B228","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-BS01","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-BS02","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-BS24","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-BS25","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-BS26","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-B221","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B520","alt_name" to "", "wing" to "L9B", "floor" to 5),
                    hashMapOf("name" to "L9-B/P004 (P25)","alt_name" to "poslucháreň P25 (B/P004)", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-B/P025 (P27)","alt_name" to "poslucháreň P27 (B/P025)", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-B004","alt_name" to "", "wing" to "L9B", "floor" to 0),
                    hashMapOf("name" to "L9-B231","alt_name" to "", "wing" to "L9B", "floor" to 2)
                )

                for (room in rooms) {
                    buildingRef.collection("rooms")
                        .add(room)
                        .addOnSuccessListener {
                            println("Room '${room["name"]}' added successfully.")
                        }
                        .addOnFailureListener { e ->
                            println("Error adding room '${room["name"]}': $e")
                        }
                }
            }
            .addOnFailureListener { e ->
                println("Error adding building: $e")
            }*/

        val searchInput = findViewById<EditText>(R.id.et_search_input)
        val searchButton = findViewById<ImageButton>(R.id.btn_search)
        val searchResultTextView = findViewById<TextView>(R.id.tv_search_result)

        searchButton.setOnClickListener {
            val input = searchInput.text.toString().trim()

            if (input.isEmpty()) {
                Toast.makeText(this, "Zadajte názov miestnosti", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Search in Firestore
            db.collectionGroup("rooms")
                .whereEqualTo("name", input)
                .get()
                .addOnSuccessListener { documents ->
                    if (!documents.isEmpty) {
                        val results = documents.joinToString("\n") { doc ->
                            val name = doc.getString("name") ?: "Neznáma"
                            val floor = doc.getLong("floor") ?: 0
                            val wing = doc.getString("wing") ?: "?"
                            "Miestnosť: $name\nPoschodie: $floor, Krídlo: $wing"
                        }
                        searchResultTextView.text = results
                    } else {
                        searchResultTextView.text = "Miestnosť sa nenašla"
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RoomSearch", "Chyba pri hľadaní miestnosti", e)
                    searchResultTextView.text = "Chyba pri načítaní: ${e.message}"
                }
        }
    }
}
