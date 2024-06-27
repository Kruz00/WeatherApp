package com.pam_228779.weatherapppro.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pam_228779.weatherapppro.R
import com.pam_228779.weatherapppro.data.model.WeatherData
import com.pam_228779.weatherapppro.utils.temperatureUnit
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import kotlin.math.roundToInt

class DailyForecastAdapter(
    private val context: Context,
    private val weatherForecast: WeatherData?
) :
    RecyclerView.Adapter<DailyForecastAdapter.ViewHolder>() {

//    private val dailyForecastList: List<Daily> = weatherForecast.daily
    private val dailyFormat = DateTimeFormatter.ofPattern("eee d.M").withLocale(Locale("pl"))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_daily_forecast, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if(weatherForecast != null) {
            val forecast = weatherForecast.daily[position]
            holder.dailyDate.text = LocalDateTime.ofInstant(
                Instant.ofEpochSecond(forecast.dt.toLong()),
                ZoneId.of(weatherForecast.timezone)
            ).format(dailyFormat)
            val temperature = "${forecast.temp.day.roundToInt()}${temperatureUnit[weatherForecast.units]}" +
                    "/" + "${forecast.temp.night.roundToInt()}${temperatureUnit[weatherForecast.units]}"
            holder.dailyTemp.text = temperature
            val iconResId = context.resources.getIdentifier(
                "weather_icon_${forecast.weather.first().icon}",
                "drawable",
                context.packageName
            )
            holder.dailyIcon.setImageResource(iconResId)
        }
    }

    override fun getItemCount(): Int {
        return weatherForecast?.daily?.size ?: 0
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dailyDate: TextView = itemView.findViewById(R.id.daily_date)
        val dailyIcon: ImageView = itemView.findViewById(R.id.daily_icon)
        val dailyTemp: TextView = itemView.findViewById(R.id.daily_temp)
    }
}