package com.example.locationtracking

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.locationtracking.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var binding: ActivityMapsBinding
    private lateinit var mMap: GoogleMap
    private var lat = 0.0
    private var long = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lat = intent.getDoubleExtra("latitude", 0.0)
        long = intent.getDoubleExtra("longitude", 0.0)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val location = com.google.android.gms.maps.model.LatLng(lat, long)
        val marker = mMap.addMarker(MarkerOptions().position(location).title("Shared Location"))
        marker?.tag = "user_id_placeholder" // You should pass the actual user ID here
        
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15f))

        mMap.setOnMarkerClickListener { marker ->
            showModernMarkerOptions(marker)
            true
        }
    }

    private fun showModernMarkerOptions(marker: Marker) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_marker_options, null)
        bottomSheetDialog.setContentView(view)

        val tvTitle = view.findViewById<TextView>(R.id.tvMarkerTitle)
        val btnChat = view.findViewById<MaterialButton>(R.id.btnChat)
        val btnViewInfo = view.findViewById<MaterialButton>(R.id.btnViewInfo)

        tvTitle.text = marker.title

        btnChat.setOnClickListener {
            val intent = Intent(this, ChatActivity::class.java)
            intent.putExtra("partnerId", marker.tag as? String)
            intent.putExtra("partnerName", marker.title)
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        btnViewInfo.setOnClickListener {
            val intent = Intent(this, Profile::class.java)
            intent.putExtra("userId", marker.tag as? String)
            startActivity(intent)
            bottomSheetDialog.dismiss()
        }

        bottomSheetDialog.show()
    }
}