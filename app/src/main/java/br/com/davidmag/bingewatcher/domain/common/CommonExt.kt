package br.com.davidmag.bingewatcher.domain.common

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.abs

private const val DOUBLE_PIVOT = 0.00000001
private const val DEF_DECIMAL_SCALE = 2

fun Double?.isNullOrZero() = orZero().isZero()
fun Double?.isNotNullOrZero() = orZero().isNotZero()
fun Double?.orZero() = this ?: 0.0
fun Double.isZero() = abs(this) < DOUBLE_PIVOT
fun Double.isNotZero() = abs(this) >= DOUBLE_PIVOT
fun Double.format(value: String): String = DecimalFormat(value).format(this)
fun Double.scale(
    value: Int = DEF_DECIMAL_SCALE,
    roundingMode: RoundingMode = RoundingMode.HALF_EVEN
) = BigDecimal(this).setScale(value, roundingMode).toDouble()

fun Int?.isNullOrZero() = orZero().isZero()
fun Int?.isNotNullOrZero() = orZero().isNotZero()
fun Int?.orZero() = this ?: 0
fun Int.isZero() = this == 0
fun Int.isNotZero() = this != 0

fun Long?.isNullOrZero() = orZero().isZero()
fun Long?.isNotNullOrZero() = orZero().isNotZero()
fun Long?.orZero() = this ?: 0
fun Long.isZero() = this == 0L
fun Long.isNotZero() = this != 0L

fun Boolean?.orFalse() = this ?: false