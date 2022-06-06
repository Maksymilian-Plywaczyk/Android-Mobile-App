package com.example.android_mobile_app

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.example.android_mobile_app.databinding.FragmentLigthSensorBinding


class LightSensorFragment : Fragment(),SensorEventListener {
    private lateinit var binding:FragmentLigthSensorBinding
    private lateinit var sensorManager: SensorManager
    private var brightness: Sensor? = null


    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        return
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if(sensorEvent?.sensor?.type ==Sensor.TYPE_LIGHT){
            val light  = sensorEvent.values[0]

            binding.tvText.text = "Sensor: $light\n${brightness(light)}"
            binding.circularProgressBar.setProgressWithAnimation(light)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ligth_sensor, container, false)

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        brightness = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)
        if (brightness != null) {
            sensorManager.registerListener(this, brightness, SensorManager.SENSOR_DELAY_NORMAL)
        }
        return binding.root
    }
    private fun brightness(brightness:Float):String{
        return when (brightness.toInt()){
            0 -> "Pitch black"
            in 1..1000 ->"Dark"
            in 1001..2000 -> "Grey"
            in 2001..5000->"Normal"
            in 5001..6100->"Incredibly bright"
            else -> "This light will blind you"
        }
    }
    override fun onResume() {
        super.onResume()
        brightness?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}