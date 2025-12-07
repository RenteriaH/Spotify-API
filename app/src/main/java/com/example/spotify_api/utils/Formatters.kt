package com.example.spotify_api.utils

import java.text.NumberFormat
import java.util.Locale
import java.util.concurrent.TimeUnit

/**
 * Formatea un número entero en una cadena con separadores de miles (comas).
 * @param number El número a formatear.
 * @return Una cadena con el número formateado, o "0" si el número es nulo.
 */
fun formatNumberWithCommas(number: Int?): String {
    if (number == null) return "0"
    return NumberFormat.getNumberInstance(Locale.US).format(number)
}

/**
 * Formatea una duración en milisegundos a un formato H:MM:SS o MM:SS.
 * @param ms La duración en milisegundos.
 * @return Una cadena con la duración formateada.
 */
fun formatDurationWithHours(ms: Long?): String { // <--- ¡¡¡TIPO CORREGIDO A LONG!!!
    if (ms == null) return "0:00"
    val totalSeconds = TimeUnit.MILLISECONDS.toSeconds(ms)
    val hours = TimeUnit.SECONDS.toHours(totalSeconds)
    val minutes = TimeUnit.SECONDS.toMinutes(totalSeconds) % 60
    val seconds = totalSeconds % 60

    return if (hours > 0) {
        String.format("%d:%02d:%02d", hours, minutes, seconds)
    } else {
        String.format("%d:%02d", minutes, seconds)
    }
}
