[![Maven](https://img.shields.io/maven-central/v/io.github.mflisar.feedbackmanager/library?style=for-the-badge&color=blue)](https://central.sonatype.com/namespace/io.github.mflisar.feedbackmanager)
[![API](https://img.shields.io/badge/api-21%2B-brightgreen.svg?style=for-the-badge)](https://android-arsenal.com/api?level=21)
[![Kotlin](https://img.shields.io/github/languages/top/mflisar/feedbackmanager.svg?style=for-the-badge&color=blueviolet)](https://kotlinlang.org/)
[![License](https://img.shields.io/github/license/MFlisar/FeedbackManager?style=for-the-badge)](LICENSE)

<h1 align="center">FeedbackManager</h1>

This is a very small library that allows you to **send feedback** from an app **without internet permission** via email, either directly or via an unintrusive notification.

## :heavy_check_mark: Features

* send feedback mail via a single function
* show a feedback notification via a single function
* no internet permission needed - everything is done via a shared Intent which then can by handled by an installed email app
* minimal size - only a **few functions** inside **2 classes**


## :camera: Screenshots

--

## :link: Dependencies

| Dependency                                                        | Version |
|-------------------------------------------------------------------|---------|
| [CacheFileProvider](https://github.com/MFlisar/CacheFileProvider) | 0.4.0   |

## :elephant: Gradle

This library is distributed via [maven central](https://central.sonatype.com/).

*build.gradle.kts*

```kts
val feedbackmanager = "<LATEST-VERSION>"

implementation("io.github.mflisar.feedbackmanager:library:$feedbackmanager")
```

## </> Basic Usage

```kotlin
val feedback = Feedback(
    receivers = listOf("some.email@gmail.com"),
    subject = "Subject of mail",
    text = "Some text",
    textIsHtml = false, // indicates if text is holding html text or plain texz
    attachments = listOf(
        FeedbackFile(uri),
        FeedbackFile(file)
    )
)
feedback.startEmailChooser(context, titleForChooser)
```

#### Create an `Intent` for later usage

```kotlin
val feedback = Feedback(...)
val intent = feedback.buildIntent(
    context = context,
    chooserTitle = "Title of the Email Chooser"
)
```

#### Notification

This allows you to show a notification which will start the email chooser if the user clicks on the notification only.

```kotlin
val feedback = Feedback(...)
feedback.startNotification(
    context = context,
    chooserTitle = "Title of the Email Chooser"
    notificationTitle = "Notification",
    notificationText = "Message",
    notificationIcon = R.mipmap.icon,
    notificationChannel = "ChannelName",
    notificationId = 1234 /* channel id */
)
```