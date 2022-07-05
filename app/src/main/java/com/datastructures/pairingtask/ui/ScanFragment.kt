package com.datastructures.pairingtask.ui

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.datastructures.pairingtask.R


class ScanFragment : Fragment(R.layout.fragment_scan) {
    private lateinit var bleImage:ImageView
    private lateinit var nfcImage:ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bleImage = view.findViewById(R.id.ble_image)
        nfcImage = view.findViewById(R.id.nfc_image)

        bleImage.setOnClickListener{
            statusCheck()
        }
        nfcImage.setOnClickListener{
            navigateToNFC()
        }


    }

    private fun navigateToNFC() {
        val i = Intent(activity, NFCActivity::class.java)
        startActivity(i)
    }

    private fun navigateToBLE() {
        val fragment: Fragment = BLEDevicesFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragments, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    private fun statusCheck() {
        val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }else {
            navigateToBLE()
        }
    }

    private fun buildAlertMessageNoGps() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Location is needed to scan nearby Bluetooth devices, enable it ? ")
            .setCancelable(false)
            .setPositiveButton("Yes"
            ) { _, _ -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No"
            ) { dialog, _ -> dialog.cancel() }
        val alert: AlertDialog = builder.create()
        alert.show()
    }
}