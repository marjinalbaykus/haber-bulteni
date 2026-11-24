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
import java.time.LocalTime
import java.time.ZoneId

/**
 * Yeni bülten bildirimi. Sunucu (Firebase) gerektirmez: Android yaklaşık yarım saatte bir bu işi çalıştırır,
 * iş sitedeki index.json'a bakar; uygulamada henüz görülmemiş yeni bir bülten varsa bildirim gösterir.
 * Android pil tasarrufu için çalışmayı biraz geciktirebilir; bülten 08:30 civarı çıkar, bildirim genelde 09:00'a kadar gelir.
 */
class NewsJob : JobService() {
    private var work: Job? = null

    override fun onStartJob(params: JobParameters): Boolean {
        work = CoroutineScope(Dispatchers.IO).launch {
            runCatching { check(applicationContext) }
            jobFinished(params, false)
        }
        return true
    }

    override fun onStopJob(params: JobParameters): Boolean {
        work?.cancel()
        return true
    }

    companion object {
        private const val JOB_ID = 1001
        private const val CHANNEL = "bulten"
        private const val NOTIFICATION_ID = 1

        /** Uygulama her açıldığında çağrılır; iş zaten kuruluysa dokunmaz. */
        fun schedule(context: Context) {
            val js = context.getSystemService(JobScheduler::class.java)
            if (js.getPendingJob(JOB_ID) != null) return
            js.schedule(
                JobInfo.Builder(JOB_ID, ComponentName(context, NewsJob::class.java))
                    .setPeriodic(30 * 60_000L, 10 * 60_000L)
                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                    .setPersisted(true)   // telefon yeniden başlayınca da sürer
                    .build(),
            )
        }

        suspend fun check(context: Context) {
            val repo = Repo(context)
            if (!repo.notificationsOn) return
            // Gece boyunca boşuna internete çıkma: bülten sabah 08:30 civarı hazırlanır
            val now = LocalTime.now(ZoneId.of("Europe/Istanbul"))
            if (now.isBefore(LocalTime.of(8, 15))) return
            val latest = repo.index().value.bulletins.firstOrNull() ?: return
            if (latest.date <= repo.lastSeenDate || latest.date <= repo.lastNotifiedDate) return
            if (repo.lastNotifiedDate.isEmpty() && repo.lastSeenDate.isEmpty()) {
                // İlk kurulum: geçmişi bildirme, sadece başlangıç noktasını kaydet
                repo.lastNotifiedDate = latest.date
                return
            }
            val top = runCatching {
                repo.bulletin(latest.date).value.topics.flatMap { it.stories }.maxByOrNull { it.importance }?.headline
            }.getOrNull()
            show(context, "Günün bülteni hazır (${latest.count} haber)", top ?: "Açmak için dokun.")
            repo.lastNotifiedDate = latest.date
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
