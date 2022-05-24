package com.example.android_mobile_app

import android.content.Context
import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.ViewPort
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.android_mobile_app.databinding.FragmentSensorsBinding
import com.jjoe64.graphview.Viewport
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import kotlin.math.abs
import kotlin.math.sqrt


class SensorsFragment : Fragment(), SensorEventListener {
    private lateinit var binding: FragmentSensorsBinding
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var accelerationCurrentValue: Float = 0.0f
    private var accelerationPreviousValue: Float = 0.0f
    private var pointsPlotted = 5
    private var minPointsPlotted = 0

    private lateinit var viewPort:Viewport

    val series: LineGraphSeries<DataPoint> = LineGraphSeries(
        arrayOf(
            DataPoint(0.0, 1.0),
            DataPoint(1.0, 5.0),
            DataPoint(2.0, 3.0),
            DataPoint(3.0, 2.0),
            DataPoint(4.0, 6.0)
        )
    )
    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {

    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
//        Sensor change value
        var x = sensorEvent.values[0]
        var y = sensorEvent.values[1]
        var z = sensorEvent.values[2]

        accelerationCurrentValue = sqrt((x * x + y * y + z * z))
        var changeInAcceleration = abs(accelerationCurrentValue - accelerationPreviousValue).toInt()
        accelerationPreviousValue = accelerationCurrentValue

        binding.txtCurrentAccel.text = "Current acceleration  =${accelerationCurrentValue.toInt()}"
        binding.textPrevAccel.text = "Prev acceleration = ${accelerationPreviousValue.toInt()}"
        binding.txtAccel.text = "Acceleration change = $changeInAcceleration"

        binding.progShakeMeter.progress = changeInAcceleration
        if(changeInAcceleration>14){
            binding.txtAccel.setBackgroundColor(Color.RED)
        }
        else if (changeInAcceleration>5){
            binding.txtAccel.setBackgroundColor(Color.parseColor("#fcad03"))
        }
        else if (changeInAcceleration>2){
            binding.txtAccel.setBackgroundColor(Color.YELLOW)
        }
        else{
            binding.txtAccel.setBackgroundColor(resources.getColor(com.google.android.material.R.color.design_default_color_background))
        }
        //update the graph
        pointsPlotted++
        if(pointsPlotted >500){
            pointsPlotted = 1
            series.resetData(arrayOf(DataPoint(1.0,0.0)))
        }
        series.appendData(DataPoint(pointsPlotted.toDouble(), changeInAcceleration.toDouble()),true,pointsPlotted)
        viewPort.setMaxX(pointsPlotted.toDouble())
        viewPort.setMinX(minPointsPlotted.toDouble())
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sensors, container, false)
        // Inflate the layout for this fragment

        sensorManager = activity?.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
        }
        viewPort = binding.graph.viewport
        viewPort.isScrollable = true
        viewPort.isXAxisBoundsManual = true
        //sample graph code
        binding.graph.addSeries(series)

        return binding.root

    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { sensor ->
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }
}