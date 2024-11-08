---
icon: material/keyboard
---

#### Basic Usage

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