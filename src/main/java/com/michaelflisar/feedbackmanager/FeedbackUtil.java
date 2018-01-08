package com.michaelflisar.feedbackmanager;


import android.content.Context;
import android.net.Uri;

import com.michaelflisar.cachefileprovider.CacheFileProviderUtil;

import java.io.File;
import java.util.UUID;

public class FeedbackUtil {

    static String copyFileToCache(Context context, Object file) {
        if (file instanceof File) {
            return copyFileToCache(context, (File) file);
        } else if (file instanceof Uri) {
            return copyFileToCache(context, (Uri) file);
        } else {
            throw new RuntimeException("Unknown file object!");
        }
    }

    static String copyFileToCache(Context context, File file) {
        // 1) find out a free cache file name
        String cacheFileName = createCacheFileName(file);

        // 2) copy input file to cache file
        File cacheFile = CacheFileProviderUtil.copyFile(context, file, cacheFileName);

        // 3) return cache file name
        return cacheFileName;
    }

    static String copyFileToCache(Context context, Uri fileUri) {
        // 1) find out a free cache file name
        String cacheFileName = createCacheFileName(new File(fileUri.getPath()));

        // 2) copy input file to cache file
        Uri cacheFile = CacheFileProviderUtil.copyFile(context, fileUri, cacheFileName);

        // 3) return cache file name
        return cacheFileName;
    }

    static String createCacheFileName(File file) {
        // find out a free cache file name - simply use filename + _ + UUID
        String nameWithoutExtension = file.getName();
        String extension = "";
        int i = file.getName().lastIndexOf('.');
        if (i > 0) {
            extension = "." + file.getName().substring(i + 1);
            nameWithoutExtension = file.getName().substring(0, i);
        }
        return nameWithoutExtension + "_" + UUID.randomUUID().toString() + extension;
    }
}
