package com.michaelflisar.feedbackmanager

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import com.michaelflisar.cachefileprovider.CachedFileProvider

class Feedback(
    private val receivers: List<String>,
    private val subject: String,
    private val text: String? = null,
    private val textIsHtml: Boolean = false,
    private val attachments: List<FeedbackFile> = emptyList()
) {

    fun buildIntent(
        context: Context,
        chooserTitle: String
    ): Intent {
        val single = attachments.size == 1
        val intent = Intent(if (single) Intent.ACTION_SEND else Intent.ACTION_SEND_MULTIPLE)
        intent.type = if (single) "message/rfc822" else "text/plain"
        intent.putExtra(Intent.EXTRA_EMAIL, receivers.toTypedArray())
        intent.putExtra(Intent.EXTRA_SUBJECT, subject)
        if (text != null) {
            intent.putExtra(Intent.EXTRA_TEXT, if (textIsHtml) HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY) else text)
        }

        if (attachments.size == 1) {
            val cacheFileName = copyFileToCache(context, attachments[0])
            val uri = CachedFileProvider.getCacheFileUri(context, cacheFileName)
            intent.putExtra(Intent.EXTRA_STREAM, uri)
        } else if (attachments.size > 1) {
            val uris = ArrayList<Uri>()
            for (i in attachments.indices) {
                val cacheFileName = copyFileToCache(context, attachments[i])
                val uri = CachedFileProvider.getCacheFileUri(context, cacheFileName)
                uris.add(uri)
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
        }

        var flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (attachments.isNotEmpty())
            flags = flags or Intent.FLAG_GRANT_READ_URI_PERMISSION

        intent.flags = flags
        return Intent.createChooser(intent, chooserTitle).apply {
            this.flags = flags
        }
    }

    fun startEmailChooser(context: Context, chooserTitle: String) {
        val intent = buildIntent(context, chooserTitle)
        context.startActivity(intent)
    }

    fun startNotification(
        context: Context,
        chooserTitle: String,
        notificationTitle: String,
        notificationText: String,
        notificationIcon: Int,
        notificationChannel: String,
        notificationId: Int
    ) {
        val intent = buildIntent(context, chooserTitle)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        } else PendingIntent.FLAG_UPDATE_CURRENT
        val pendingIntent = PendingIntent.getActivity(
            context,
            1111 /* unused */,
            intent,
            flags
        )

        val builder = NotificationCompat.Builder(
            context,
            notificationChannel
        )
            .setSmallIcon(notificationIcon)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setContentIntent(pendingIntent)
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(notificationId, builder.build())
    }

    private fun copyFileToCache(context: Context, feedbackFile: FeedbackFile): String {

        // 1) copy input file to cache file
        val cacheFile = CachedFileProvider.copyFileToCache(
            context,
            feedbackFile.uri,
            feedbackFile.cacheFileName
        )

        // 2) return cache file name
        return cacheFile.name
    }
}