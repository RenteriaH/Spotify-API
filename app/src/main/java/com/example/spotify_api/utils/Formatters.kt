package com.example.spotify_api.utils

import java.text.NumberFormat
import java.util.Locale

/**
 * Formatea un número entero en una cadena con separadores de miles (comas).
 * @param number El número a formatear.
 * @return Una cadena con el número formateado, o "0" si el número es nulo.
 */
fun formatNumberWithCommas(number: Int?): String {
    if (number == null) return "0"
    return NumberFormat.getNumberInstance(Locale.US).format(number)
}
