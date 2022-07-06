package com.datastructures.pairingtask.ui

import android.Manifest
import android.app.AlertDialog
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.airbnb.lottie.LottieAnimationView
import com.datastructures.pairingtask.R


class ScanFragment : Fragment(R.layout.fragment_scan) {

    private val bluetoothAdapter: BluetoothAdapter? by lazy {
        BluetoothAdapter.getDefaultAdapter()
    }

    private val bluetoothAnimation:LottieAnimationView by lazy {
        requireView().findViewById(R.id.bluetooth_animation)
    }
    private val nfcAnimation:LottieAnimationView by lazy {
        requireView().findViewById(R.id.NFC_animation)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hideKeyboard()


        bluetoothAnimation.setOnClickListener{
            if(bluetoothAdapter == null){
                Toast.makeText(requireContext(), "Your device doesn't support Bluetooth", Toast.LENGTH_LONG).show()
            }else{
                permissionsCheck()
            }        }
        nfcAnimation.setOnClickListener{
            navigateToNFC()
        }


    }

    private fun hideKeyboard() {
        try {
            val imm: InputMethodManager? =
                requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm?.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
        } catch (e: Exception) {
            Log.d("keyboard error", e.stackTraceToString())
        }
    }

    private fun navigateToNFC() {
        val i = Intent(activity, NFCActivity::class.java)
        startActivity(i)
    }

    private fun navigateToBLE() {
        requireActivity().findViewById<TextView>(R.id.mainActivityTV).text = "Nearby Devices"
        val fragment: Fragment = BLEDevicesFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragments, fragment)
        fragmentTransaction.commit()
    }

    private fun permissionsCheck() {
        checkBluetooth()
        val manager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        if (!manager!!.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps()
        }else {
            navigateToBLE()
        }
    }

    private fun checkBluetooth(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.BLUETOOTH_CONNECT),
                2
            )
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