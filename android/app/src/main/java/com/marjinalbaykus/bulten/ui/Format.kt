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

private val DAY_FMT = DateTimeFormatter.ofPattern("d MMMM yyyy", TR)
private val WEEKDAY_FMT = DateTimeFormatter.ofPattern("EEEE", TR)
private val CLOCK_FMT = DateTimeFormatter.ofPattern("HH:mm", TR)

/** "2026-11-24" -> ("24 Kasım 2026", "Salı") */
fun trDayParts(date: String): Pair<String, String> =
    runCatching { LocalDate.parse(date).let { it.format(DAY_FMT) to it.format(WEEKDAY_FMT) } }.getOrDefault(date to "")

/** Bülten tarihiyle aynı günse sadece saat ("08:31"), değilse gün ve saat ("23 Kasım 23:58"). */
fun trPrepared(utc: String, date: String): String =
    runCatching {
        val t = OffsetDateTime.parse(utc).atZoneSameInstant(ZoneId.of("Europe/Istanbul"))
        if (t.toLocalDate() == LocalDate.parse(date)) t.format(CLOCK_FMT) else t.format(TIME_FMT)
    }.getOrDefault(utc)
