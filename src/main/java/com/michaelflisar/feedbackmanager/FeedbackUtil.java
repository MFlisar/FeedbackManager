package com.michaelflisar.feedbackmanager;


import android.content.Context;
import android.net.Uri;

import com.michaelflisar.cachefileprovider.CacheFileProviderUtil;

import java.io.File;
import java.util.UUID;

public class FeedbackUtil {

    static String copyFileToCache(Context context, FeedbackFile feedbackFile) {
        // 1) find out a free cache file name
        String cacheFileName = createCacheFileName(feedbackFile);

        // 2) copy input file to cache file
        Uri cacheFile = CacheFileProviderUtil.copyFile(context, feedbackFile.attachment, cacheFileName, feedbackFile.useOriginalName());

        // 3) return cache file name
        return cacheFileName;
    }

    static String createCacheFileName(FeedbackFile feedbackFile) {

        // Case 1: we want a unique cache file name => we generate it: filename + "_" + UUID
        if (feedbackFile.generateUniqueName) {
            File file = new File(feedbackFile.attachment.getPath());
            String nameWithoutExtension = file.getName();
            String extension = "";
            int i = file.getName().lastIndexOf('.');
            if (i > 0) {
                extension = "." + file.getName().substring(i + 1);
                nameWithoutExtension = file.getName().substring(0, i);
            }
            return nameWithoutExtension + "_" + UUID.randomUUID().toString() + extension;
        }
        // Case 2: we want to use a custom name
        else if (feedbackFile.customCacheFileName != null) {
            return feedbackFile.customCacheFileName;
        }
        // Case 3: we want to use the original name
        else {
            File file = new File(feedbackFile.attachment.getPath());
            return file.getName();
        }
    }
}
