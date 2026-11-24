package com.marjinalbaykus.bulten.notify

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.job.JobInfo
import android.app.job.JobParameters
import android.app.job.JobScheduler
import android.app.job.JobService
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import com.marjinalbaykus.bulten.MainActivity
import com.marjinalbaykus.bulten.R
import com.marjinalbaykus.bulten.data.Repo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

/**
 * Yeni bülten bildirimi, pil dostu: gün boyu düzenli kontrol YOK.
 * - Her gün 08:35'te bir kez uyanır ve sitedeki küçük index.json dosyasına bakar.
 * - Bülten hazırsa bildirir ve ertesi sabaha kadar bir daha çalışmaz.
 * - Henüz hazır değilse 10 dakika sonra tekrar bakar (öğleden sonra 30 dakikada bir, 23:00'te bırakır).
 * - Bugünün bülteni uygulamada zaten açılıp görüldüyse internete hiç çıkmadan yarına kurulur.
 * Normal bir günde bu 1-2 küçük istek demektir. Telefon uzun süre hareketsiz ve ekranı kapalıysa
 * Android çalışmayı birkaç dakika geciktirebilir.
 */
class NewsJob : JobService() {
    private var work: Job? = null

    override fun onStartJob(params: JobParameters): Boolean {
        work = CoroutineScope(Dispatchers.IO).launch {
            val ctx = applicationContext
            val done = runCatching { check(ctx) }.getOrDefault(false)
            runCatching { scheduleNext(ctx, done) }
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        work?.cancel()
        // Sistem işi yarıda keserse bir sonraki deneme yine kurulsun
        runCatching { scheduleNext(applicationContext, todayDone(Repo(applicationContext))) }
        return false
    }

    companion object {
        private const val JOB_ID = 1001
        private const val CHANNEL = "bulten"
        private const val NOTIFICATION_ID = 1
        private val TZ: ZoneId = ZoneId.of("Europe/Istanbul")
        private val FIRST_CHECK: LocalTime = LocalTime.of(8, 35)
        private val GIVE_UP: LocalTime = LocalTime.of(23, 0)

        private fun today(): String = LocalDate.now(TZ).toString()

        private fun todayDone(repo: Repo): Boolean =
            repo.lastSeenDate >= today() || repo.lastNotifiedDate >= today()

        /** Uygulama her açıldığında çağrılır; kurulu tek seferlik iş varsa dokunmaz. */
        fun schedule(context: Context) {
            val js = context.getSystemService(JobScheduler::class.java)
            val pending = js.getPendingJob(JOB_ID)
            if (pending != null && !pending.isPeriodic) return
            // Eski sürümden kalan "30 dakikada bir" işi kaldırılır
            if (pending != null) js.cancel(JOB_ID)
            scheduleNext(context, todayDone(Repo(context)))
        }

        /** Bir sonraki tek seferlik kontrolü kurar. */
        private fun scheduleNext(context: Context, doneToday: Boolean) {
            val now = ZonedDateTime.now(TZ)
            val todayFirst = now.with(FIRST_CHECK)
            val next = when {
                doneToday || now.toLocalTime() >= GIVE_UP -> todayFirst.plusDays(1)
                now.isBefore(todayFirst) -> todayFirst
                now.toLocalTime() < LocalTime.NOON -> now.plusMinutes(10)
                else -> now.plusMinutes(30)
            }
            val delay = Duration.between(now, next).toMillis().coerceAtLeast(0)
            context.getSystemService(JobScheduler::class.java).schedule(
                JobInfo.Builder(JOB_ID, ComponentName(context, NewsJob::class.java))
                    .setMinimumLatency(delay)
                    .setOverrideDeadline(delay + 5 * 60_000L)   // en geç 5 dakika sonra çalışsın
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)   // telefon yeniden başlayınca da kalır
                    .build(),
            )
        }

        /** Bugünün işi bittiyse (bildirildi, görüldü veya kapalı) true döner. */
        private suspend fun check(context: Context): Boolean {
            val repo = Repo(context)
            if (!repo.notificationsOn) return true
            if (todayDone(repo)) return true   // internete çıkmadan
            val latest = repo.index().value.bulletins.firstOrNull() ?: return false
            if (latest.date < today()) return false   // bugünün bülteni henüz yok, sonra tekrar bak
            if (latest.date > repo.lastSeenDate && latest.date > repo.lastNotifiedDate) {
                val top = runCatching {
                    repo.bulletin(latest.date).value.topics.flatMap { it.stories }.maxByOrNull { it.importance }?.headline
                }.getOrNull()
                show(context, "Günün bülteni hazır (${latest.count} haber)", top ?: "Açmak için dokun.")
            }
            repo.lastNotifiedDate = latest.date
            return true
        }

        private fun show(context: Context, title: String, text: String) {
            if (Build.VERSION.SDK_INT >= 33 &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED
            ) return
            val nm = context.getSystemService(NotificationManager::class.java)
            nm.createNotificationChannel(
                NotificationChannel(CHANNEL, "Günlük bülten", NotificationManager.IMPORTANCE_DEFAULT),
            )
            val open = PendingIntent.getActivity(
                context, 0,
                Intent(context, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP),
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT,
            )
            val n = android.app.Notification.Builder(context, CHANNEL)
                .setSmallIcon(R.drawable.ic_stat_bulten)
                .setContentTitle(title)
                .setContentText(text)
                .setStyle(android.app.Notification.BigTextStyle().bigText(text))
                .setContentIntent(open)
                .setAutoCancel(true)
                .build()
            nm.notify(NOTIFICATION_ID, n)
        }
    }
}
