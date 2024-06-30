package com.pam_228779.weatherapppro.utils

import android.content.SharedPreferences
import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.time.DurationUnit
import kotlin.time.toDuration

private const val MINUTES_TO_REFRESH_DEFAULT = 15
private const val LAST_WEATHERS_UPDATE_KEY = "lastWeathersUpdate"
private const val REFRESH_INTERVAL_KEY = "refresh_interval"

class WeatherRefresher(
    private val preferences: SharedPreferences,
    private val refreshCallback: () -> Unit
) {
    private val TAG = "WeatherRefresher"

    private var refreshJob: Job? = null
    private val scope = CoroutineScope(Dispatchers.IO)

    fun restart() {
        stop()
        val refreshIntervalMinutes =
            preferences.getString(REFRESH_INTERVAL_KEY, MINUTES_TO_REFRESH_DEFAULT.toString())!!
                .toLong()
        val refreshIntervalMillis = refreshIntervalMinutes.toDuration(DurationUnit.MINUTES).inWholeMilliseconds
        refreshJob = scope.launch {
            while (isActive) {
                val lastUpdate = preferences.getLong(LAST_WEATHERS_UPDATE_KEY, 0)
                val lastUpdateInstant = Instant.ofEpochSecond(lastUpdate)
                val now = Instant.now()
                Log.i(TAG, "lastRefresh: ${lastUpdateInstant}\nrefreshInterval: $refreshIntervalMinutes\nInstantNow: $now")
                val nextUpdate = lastUpdateInstant.plus(refreshIntervalMinutes, ChronoUnit.MINUTES)
                if (nextUpdate.isAfter(now)) {
                    val delayMillis = now.until(nextUpdate, ChronoUnit.MILLIS)
                    delay(delayMillis)
                } else {
                    refreshCallback.invoke()
                    delay(refreshIntervalMillis)
                }
            }
        }
    }

    fun stop() {
        refreshJob?.cancel()
    }







//
//    private val scheduler = Executors.newSingleThreadScheduledExecutor()
//    private var scheduledFuture: ScheduledFuture<*>? = null
//
//
//    fun start(runnable: Runnable) {
//        scheduledFuture?.cancel(true)
//        val refreshInterval =
//            preferences.getString(REFRESH_INTERVAL_KEY, MINUTES_TO_REFRESH_DEFAULT.toString())!!
//                .toLong()
//        val lastUpdate = preferences.getLong(LAST_WEATHERS_UPDATE_KEY, 0)
//        val lastUpdateInstant = Instant.ofEpochSecond(lastUpdate)
//        val now = Instant.now()
//        Log.i(TAG, "lastRefresh: ${lastUpdateInstant}\nrefreshInterval: $refreshInterval\nInstantNow: $now")
//        val initDelay = if (now.isBefore(lastUpdateInstant.plus(refreshInterval, ChronoUnit.MINUTES)))
//            lastUpdateInstant.until(now, ChronoUnit.SECONDS) else 0
//        scheduledFuture = scheduler.scheduleWithFixedDelay(
//            runnable,
//            initDelay,
//            refreshInterval,
//            TimeUnit.SECONDS
//        )
//    }
//
//    fun stop() {
//        scheduledFuture?.cancel(true)
//    }

}