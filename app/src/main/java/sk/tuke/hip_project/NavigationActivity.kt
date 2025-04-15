package sk.tuke.hip_project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.RemoteException
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.firestore
import android.widget.ImageView
import org.altbeacon.beacon.*
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class NavigationActivity : AppCompatActivity(), BeaconConsumer {
    private lateinit var beaconManager: BeaconManager
    private val PERMISSION_REQUEST_CODE = 1001

    private var targetFloor: Int? = null
    private var lastDetectedFloor: Int? = null
    private var lastDistance: Double = Double.MAX_VALUE
    private val DISTANCE_THRESHOLD = 2.0 // meters

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
                    hashMapOf("name" to "L9-A536","alt_name" to "Abydos - laboratórna miestnosť", "wing" to "L9A", "floor" to 5),
                    hashMapOf("name" to "L9-A504","alt_name" to "Aurora - prednášková miestnosť", "wing" to "L9A", "floor" to 5),
                    hashMapOf("name" to "L9-A512","alt_name" to "Hyperion - laboratórium IoT", "wing" to "L9A", "floor" to 5),
                    hashMapOf("name" to "L9-A534","alt_name" to "Romulus - laboratórium CNL", "wing" to "L9A", "floor" to 5),
                    hashMapOf("name" to "L9-A532","alt_name" to "Solaris - laboratórna miestnosť", "wing" to "L9A", "floor" to 5),
                    hashMapOf("name" to "L9-A514","alt_name" to "Vulcan - laboratórna miestnosť", "wing" to "L9A", "floor" to 5),
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
                    hashMapOf("name" to "L9-B221","alt_name" to "", "wing" to "L9B", "floor" to 2),
                    hashMapOf("name" to "L9-B520","alt_name" to "", "wing" to "L9B", "floor" to 5),
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
        val floorImageView = findViewById<ImageView>(R.id.floor_image)

        val floorImages = mapOf(
            1L to R.drawable.tuke_floor1,
            2L to R.drawable.tuke_floor2,
            3L to R.drawable.tuke_floor3,
            4L to R.drawable.tuke_floor4,
            5L to R.drawable.tuke_floor5,
        )

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
                        val firstDoc = documents.first()
                        val name = firstDoc.getString("name") ?: "Neznáma"
                        val floor = firstDoc.getLong("floor")?.toInt() ?: 0
                        val wing = firstDoc.getString("wing") ?: "?"

                        targetFloor = floor // <-- save target floor globally
                        // Text result
                        val resultText = "Miestnosť: $name\nPoschodie: $floor, Krídlo: $wing"
                        searchResultTextView.text = resultText

                        val imageRes = floorImages[floor.toLong()]
                        if (imageRes != null) {
                            floorImageView.setImageResource(imageRes)
                        } else {
                            floorImageView.setImageDrawable(null) // Clear image if not found
                        }
                    } else {
                        searchResultTextView.text = "Miestnosť sa nenašla"
                        floorImageView.setImageDrawable(null)
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("RoomSearch", "Chyba pri hľadaní miestnosti", e)
                    searchResultTextView.text = "Chyba pri načítaní: ${e.message}"
                }
        }

        val settingsButton = findViewById<ImageButton>(R.id.settingsButton)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(
            BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24")
        )

        checkAndRequestPermissions()
    }

    private fun checkAndRequestPermissions() {
        val permissionsNeeded = mutableListOf<String>()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.BLUETOOTH_SCAN)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.BLUETOOTH_CONNECT)
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        if (permissionsNeeded.isNotEmpty()) {
            ActivityCompat.requestPermissions(this, permissionsNeeded.toTypedArray(), PERMISSION_REQUEST_CODE)
        } else {
            checkBluetoothEnabled()
        }
    }

    @SuppressLint("MissingPermission")
    private fun checkBluetoothEnabled() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Bluetooth oprávnenie nie je udelené", Toast.LENGTH_LONG).show()
            return
        }

        try {
            val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
            val bluetoothAdapter = bluetoothManager.adapter

            if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled) {
                Toast.makeText(this, "Bluetooth nie je zapnutý", Toast.LENGTH_LONG).show()
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1002)
            } else {
                beaconManager.bind(this)
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
            Toast.makeText(this, "Chyba pri prístupe k Bluetooth: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {
                checkBluetoothEnabled()
            } else {
                Toast.makeText(this, "Bluetooth a lokalizačné oprávnenia sú potrebné", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBeaconServiceConnect() {
        beaconManager.addRangeNotifier { beacons, _ ->
            if (beacons.isNotEmpty()) {
                val closest = beacons.minByOrNull { it.distance }
                closest?.let { it ->
                    val major = it.id2.toString().toIntOrNull()
                    val distance = it.distance

                    val detectedFloor = when (major) {
                        1 -> 1
                        2 -> 2
                        3 -> 3
                        4 -> 4
                        5 -> 5
                        else -> null
                    }

                    if (detectedFloor != null) {
                        runOnUiThread {
                            val floorImageView = findViewById<ImageView>(R.id.floor_image)
                            val currentFloorTextView = findViewById<TextView>(R.id.tv_current_floor)
                            val directionTextView = findViewById<TextView>(R.id.tv_direction)

                            val floorImages = mapOf(
                                1 to R.drawable.tuke_floor1,
                                2 to R.drawable.tuke_floor2,
                                3 to R.drawable.tuke_floor3,
                                4 to R.drawable.tuke_floor4,
                                5 to R.drawable.tuke_floor5,
                            )

                            currentFloorTextView.text = "Aktuálne poschodie: $detectedFloor"

                            val directionMessage = if (targetFloor != null) {
                                when {
                                    detectedFloor < targetFloor!! -> "Choď o poschodie vyššie"
                                    detectedFloor > targetFloor!! -> "Choď o poschodie nižšie"
                                    else -> "Si na správnom poschodí"
                                }
                            } else {
                                ""
                            }

                            directionTextView.text = directionMessage

                            if (detectedFloor != lastDetectedFloor) {
                                lastDetectedFloor = detectedFloor
                                floorImages[detectedFloor]?.let {
                                    floorImageView.setImageResource(it)
                                }
                            }
                        }
                    }
                }
            }
        }

        try {
            beaconManager.startRangingBeaconsInRegion(Region("all-beacons", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        beaconManager.unbind(this)
    }
}




