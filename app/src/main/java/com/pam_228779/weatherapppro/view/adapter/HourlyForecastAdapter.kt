package com.pam_228779.weatherapppro.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.model.Hourly
import com.pam_228779.weatherapppro.data.model.WeatherData
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class HourlyForecastAdapter(
    private val context: Context,
    private val weatherForecast: WeatherData?
) :
    RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder>() {

//    private val hourlyForecastList: List<Hourly> = weatherForecast.hourly
    private val hourlyFormat = DateTimeFormatter.ofPattern("HH:mm")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_hourly_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(weatherForecast != null) {
            val forecast = weatherForecast.hourly[position]
            holder.hourlyTime.text = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(forecast.dt.toLong()),
                ZoneId.of(weatherForecast.timezone)
            ).format(hourlyFormat)
            holder.hourlyTemp.text = forecast.temp.roundToInt().toString()
            val iconResId = context.resources.getIdentifier(
                "weather_icon_${forecast.weather.first().icon}",
                "drawable",
                context.packageName
            )
            holder.hourlyIcon.setImageResource(iconResId)
        }
    }

    override fun getItemCount(): Int {
        return weatherForecast?.hourly?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hourlyTime: TextView = itemView.findViewById(R.id.hourly_time)
        val hourlyIcon: ImageView = itemView.findViewById(R.id.hourly_icon)
        val hourlyTemp: TextView = itemView.findViewById(R.id.hourly_temp)
    }
}
