package com.michaelflisar.feedbackmanager;

import android.net.Uri;

import java.io.File;

/**
 * Created by flisar on 08.01.2018.
 */
public class FeedbackFile {
    Uri attachment;
    boolean generateUniqueName;
    String customCacheFileName;

    public FeedbackFile(File file) {
        attachment = Uri.fromFile(file);
        generateUniqueName = false;
        customCacheFileName = null;
    }

    public FeedbackFile(Uri fileUri) {
        attachment = fileUri;
        generateUniqueName = false;
        customCacheFileName = null;
    }

    public boolean useOriginalName() {
        return !generateUniqueName && customCacheFileName == null;
    }

    public File getFile() {
        return new File(attachment.getPath());
    }

    public FeedbackFile withCustomCacheFileName(String cacheFileName) {
        customCacheFileName = cacheFileName;
        generateUniqueName = false;
        return this;
    }

    public FeedbackFile withGenerateUniqueName() {
        customCacheFileName = null;
        generateUniqueName = true;
        return this;
    }
}
