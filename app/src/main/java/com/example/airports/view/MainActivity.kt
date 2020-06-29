package com.example.airports.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.airports.R
import com.example.airports.viewmodel.AirportAdapter
import com.example.airports.viewmodel.getAirportStatus
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivity : AppCompatActivity(), CoroutineScope {

    private val airportCodes = mutableListOf<String>()

    /* enabling coroutine scope within the UI thread to call suspend functions */
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /* disabling button when there's no code to make the request */
        btnAddCode.isEnabled = false

        airportCode.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                btnAddCode.isEnabled = airportCode.text.isNotBlank()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

        })

        btnAddCode.setOnClickListener {
            airportCodes.add(airportCode.text.toString())
            airportCode.setText("")

            /* running in the CoroutineScope Dispatchers.Main, defined above */
            launch {
                updateSingleAirportStatus()
            }
        }
        /* setting the RecyclerView params to correctly display the data */
        airportStatus.apply {
            setHasFixedSize(true)

            /* managing the child widgets */
            layoutManager = LinearLayoutManager(this@MainActivity)

            /* managing the display of the airport statuses*/
            adapter = AirportAdapter()
        }

    }

    /* will trigger the redisplay of the RecyclerView when new airport statuses are
     received from getAirportStatus() */
    private suspend fun updateSingleAirportStatus() {
        val airports = getAirportStatus(airportCodes)
        val airportAdapter = airportStatus.adapter as AirportAdapter
        airportAdapter.updateAirportsStatus(airports)
    }
}
