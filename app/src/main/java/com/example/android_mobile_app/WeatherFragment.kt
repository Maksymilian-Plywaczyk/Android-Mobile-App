package com.example.android_mobile_app

import android.app.ActionBar
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import com.example.android_mobile_app.databinding.FragmentOpenBinding
import com.example.android_mobile_app.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.URL
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets.UTF_8
import java.text.SimpleDateFormat
import java.util.*
import kotlin.text.Charsets.UTF_8
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class WeatherFragment : Fragment() {
    private lateinit var binding: FragmentOpenBinding
    private lateinit var mAuth: FirebaseAuth
    private val weatherApiKey = "95e1d807e64e4be04be3fd6df303ec05"
    private var location = ""
    private var date = ""
    private lateinit var calendar:Calendar
    private lateinit var simpleDateFormat: SimpleDateFormat
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_open, container, false
        )
        binding.logoutButton.setOnClickListener {
            view?.findNavController()?.navigate(R.id.action_openFragment_to_loginFragment)
        }
        calendar = Calendar.getInstance()
        simpleDateFormat = SimpleDateFormat("dd MMMM yyyy HH:mm")
        date = simpleDateFormat.format(calendar.time)
        binding.updatedAt.text = date
        mAuth = Firebase.auth
        binding.submitButton.setOnClickListener {
            location =
                binding.enterCity.text.toString();weatherTask().execute()
           setVisibility()
        }
        return binding.root

    }
    private fun setVisibility(){
        binding.sunriseLayout.visibility=View.VISIBLE
        binding.sunsetLayout.visibility=View.VISIBLE
        binding.windLayout.visibility=View.VISIBLE
        binding.logoutButton.visibility=View.VISIBLE
    }
    inner class weatherTask() : AsyncTask<String, Void, String>() {
        override fun doInBackground(vararg params: String?): String? {
            var response: String?
            try {
                response =
                    URL("https://api.openweathermap.org/data/2.5/weather?q=$location&units=metric&appid=$weatherApiKey").readText(
                        Charsets.UTF_8
                    )
                Log.d("weather update", response)
            } catch (e: Exception) {
                response = null
            }
            return response
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            try {
                val jsonObj = JSONObject(result)

                val main = jsonObj.getJSONObject("main")
                val sys = jsonObj.getJSONObject("sys")
                val wind = jsonObj.getJSONObject("wind")
                val weather = jsonObj.getJSONArray("weather").getJSONObject(0)
                val updatedAt: Long = jsonObj.getLong("dt")
                val updatedAtText =
                    "Updated at: " + SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.GERMANY).format(
                        Date(updatedAt * 1000)
                    )
                val temp = main.getString("temp") + "°C"
                val tempMin = "Min Temp: " + main.getString("temp_min") + "°C"
                val tempMax = "Max Temp: " + main.getString("temp_max") + "°C"
                val sunrise: Long = sys.getLong("sunrise")
                val sunset: Long = sys.getLong("sunset")
                val windSpeed = wind.getString("speed")
                val weatherDescription = weather.getString("description")

                val address = jsonObj.getString("name") + ", " + sys.getString("country")
                binding.address.text = address
                binding.updatedAt.text = updatedAtText
                binding.status.text = weatherDescription.capitalize()
                binding.temp.text = temp
                binding.tempMin.text = tempMin
                binding.tempMax.text = tempMax
                binding.sunrise.text =
                    SimpleDateFormat("hh:mm a", Locale.GERMANY).format(Date(sunrise * 1000))
                binding.sunset.text =
                    SimpleDateFormat("hh:mm a", Locale.GERMANY).format(Date(sunset * 1000))
                binding.wind.text = windSpeed
            } catch (e: Exception) {
                println("error")
            }
        }
    }
}
