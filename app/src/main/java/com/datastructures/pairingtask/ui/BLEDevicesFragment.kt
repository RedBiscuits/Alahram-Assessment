package com.datastructures.pairingtask.ui

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.datastructures.pairingtask.R


class BLEDevicesFragment : Fragment(R.layout.fragment_b_l_e_devices) {
    private lateinit var bluetoothAdapter: BluetoothAdapter
    private lateinit var listView:ListView
    private lateinit var arrayList:ArrayList<String>
    lateinit var arrayAdapter:ArrayAdapter<String>
    private lateinit var button:Button



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arrayList = ArrayList()
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        listView = view.findViewById(R.id.ble_devices_lv)
        button = view.findViewById(R.id.cancel_button)
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.BLUETOOTH_SCAN
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                1
            )
        }
        arrayAdapter = ArrayAdapter(
            requireContext(), android.R.layout.simple_list_item_1 , arrayList)

        if(!bluetoothAdapter.isEnabled){
            bluetoothAdapter.enable()
        }
        bluetoothAdapter.startDiscovery()

        val intentFilter = IntentFilter(BluetoothDevice.ACTION_FOUND)
        requireActivity().registerReceiver(broadcastReceiver,intentFilter)


        listView.adapter = arrayAdapter
        button.setOnClickListener{
            navigateToScanOptions()
        }
    }


    private val broadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action:String = intent.action.toString()
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            if (BluetoothDevice.ACTION_FOUND == action){
                val bluetoothDevice :BluetoothDevice= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)!!
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
                if(bluetoothDevice.name != null){
                arrayList.add(bluetoothDevice.name.toString())
                Log.d("device", bluetoothDevice.name.toString())
                arrayAdapter.notifyDataSetChanged()
                }
            }
        }
    }

    private fun navigateToScanOptions() {
        val fragment: Fragment = ScanFragment()
        val fragmentManager = requireActivity().supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.flFragments, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}