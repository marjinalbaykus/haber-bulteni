package com.marjinalbaykus.bulten.ui

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

private val TR = Locale.forLanguageTag("tr-TR")
private val DATE_FMT = DateTimeFormatter.ofPattern("d MMMM yyyy, EEEE", TR)
private val SHORT_FMT = DateTimeFormatter.ofPattern("d MMMM EEEE", TR)
private val TIME_FMT = DateTimeFormatter.ofPattern("d MMMM HH:mm", TR)

fun trDate(date: String, short: Boolean = false): String =
    runCatching { LocalDate.parse(date).format(if (short) SHORT_FMT else DATE_FMT) }.getOrDefault(date)

/** "2026-11-23T23:58Z" -> "24 Kasım 02:58" (Türkiye saati) */
fun trTime(utc: String): String =
    runCatching {
        OffsetDateTime.parse(utc).atZoneSameInstant(ZoneId.of("Europe/Istanbul")).format(TIME_FMT)
    }.getOrDefault(utc)
