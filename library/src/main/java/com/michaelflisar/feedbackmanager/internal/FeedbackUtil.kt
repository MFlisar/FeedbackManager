package com.michaelflisar.feedbackmanager.internal

import android.content.Context
import com.michaelflisar.cachefileprovider.CacheFileProviderUtil
import com.michaelflisar.feedbackmanager.FeedbackFile

internal object FeedbackUtil {

    fun copyFileToCache(context: Context?, feedbackFile: FeedbackFile): String {

        // 1) find out a free cache file name
        val cacheFileName = feedbackFile.cacheFileName

        // 2) copy input file to cache file
        val cacheFile = CacheFileProviderUtil.copyFile(
            context,
            feedbackFile.uri,
            cacheFileName,
            feedbackFile.checkIfFileIsInCache
        )

        // 3) return cache file name
        return cacheFileName
    }
}