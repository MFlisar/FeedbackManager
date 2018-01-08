package com.michaelflisar.feedbackmanager;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.text.Html;

import com.michaelflisar.cachefileprovider.CachedFileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FeedbackBuilder {

    private List<String> mReceivers = null;
    private String mSubject = null;
    private String mText = null;
    private boolean mTextIsHtml = false;
    private List<Object> mAttachments = null;

    FeedbackBuilder() {
    }

    public static FeedbackBuilder create() {
        return new FeedbackBuilder();
    }

    public FeedbackBuilder addReceiver(String mail) {
        if (mReceivers == null) {
            mReceivers = new ArrayList<>();
        }
        mReceivers.add(mail);
        return this;
    }

    public FeedbackBuilder withSubject(String subject) {
        mSubject = subject;
        return this;
    }

    public FeedbackBuilder withText(String text) {
        mText = text;
        mTextIsHtml = false;
        return this;
    }

    public FeedbackBuilder withText(String text, boolean textIsHtml) {
        mText = text;
        mTextIsHtml = textIsHtml;
        return this;
    }

    public FeedbackBuilder addFile(File file) {
        if (mAttachments == null) {
            mAttachments = new ArrayList<>();
        }
        mAttachments.add(Uri.fromFile(file));
        return this;
    }

    public FeedbackBuilder addFile(Uri uri) {
        if (mAttachments == null) {
            mAttachments = new ArrayList<>();
        }
        mAttachments.add(uri);
        return this;
    }

    public Intent buildIntent(Context context, String chooserTitle) {

        if (mReceivers == null) {
            throw new RuntimeException("FeedbackBuilder is missing a receiver!");
        }

        if (mText == null && mAttachments == null && mSubject == null) {
            throw new RuntimeException("FeedbackBuilder - seems like you are trying to send an empty feedback, add a subject, attachment or some text!");
        }

        boolean single = mAttachments == null || mAttachments.size() == 1;

        Intent intent = new Intent(single ? Intent.ACTION_SEND : Intent.ACTION_SEND_MULTIPLE);
        intent.setType(single ? "message/rfc822" : "text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, mReceivers.toArray(new String[mReceivers.size()]));
        intent.putExtra(Intent.EXTRA_SUBJECT, mSubject);
        if (mText != null) {
            intent.putExtra(Intent.EXTRA_TEXT, mTextIsHtml ? Html.fromHtml(mText) : mText);
        }

        if (mAttachments != null) {
            String cacheFileName = null;
            if (mAttachments.size() == 1) {
                cacheFileName = FeedbackUtil.copyFileToCache(context, mAttachments.get(0));
                intent.putExtra(Intent.EXTRA_STREAM, CachedFileProvider.getCacheFileUri(context, cacheFileName));
            } else {
                ArrayList<Uri> uris = new ArrayList<>();
                for (Object attachment : mAttachments) {
                    cacheFileName = FeedbackUtil.copyFileToCache(context, attachment);
                    uris.add(CachedFileProvider.getCacheFileUri(context, cacheFileName));
                }
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            }
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        return Intent.createChooser(intent, chooserTitle);
    }

    public void startEmailChooser(Context context, String chooserTitle) {
        Intent intent = buildIntent(context, chooserTitle);
        context.startActivity(intent);
    }

    public void startNotification(Context context, String chooserTitle, String notificationTitle, String notificationText, int notificationIcon, String notificationChannel, int notificationId) {
        Intent intent = buildIntent(context, chooserTitle);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 1111 /* unused */, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, notificationChannel)
                .setSmallIcon(notificationIcon)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(pendingIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId, builder.build());
    }
}
