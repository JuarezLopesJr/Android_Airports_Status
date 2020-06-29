package com.example.airports.viewmodel

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.airports.R
import com.example.airports.model.Airport
import kotlinx.android.synthetic.main.airport_info.view.*

class AirportAdapter : RecyclerView.Adapter<AirportViewHolder>() {

    private val airports = mutableListOf<Airport>()

    /* will produce a view holder for each row */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AirportViewHolder {

        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.airport_info, parent, false)

        return AirportViewHolder(view)
    }

    /* + 1 is to display a header row */
    override fun getItemCount(): Int = airports.size + 1

    /* bind the data to be displayed with the view holder */
    override fun onBindViewHolder(holder: AirportViewHolder, position: Int) {
        if (position > 0) holder.bind(airports[position - 1])
    }

    /* responsible for taking the updated/new airport statuses and modifying
       the mutable list stored as a field within the adapter */
    fun updateAirportsStatus(updatedAirports: List<Airport>) {
        airports.apply {
            clear()
            addAll(updatedAirports)
        }
        notifyDataSetChanged()
    }
}

class AirportViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    fun bind(airport: Airport) {
        val (code, name, delay, weather) = airport
        val clock = if (delay) "\uD83D\uDD52" else ""

        itemView.apply {
            airportCode.text = code
            airportName.text = name
            airportTemperature.text = weather.temperature.firstOrNull()
            airportDelay.text = clock
        }
    }
}
