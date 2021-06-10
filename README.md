### FeedbackManager [![Release](https://jitpack.io/v/MFlisar/FeedbackManager.svg)](https://jitpack.io/#MFlisar/FeedbackManager) <a href="http://www.methodscount.com/?lib=com.github.MFlisar%3AFeedbackManager%3A1.1"><img src="https://img.shields.io/badge/Methods and size-core: 63 | deps: 7572 | 7 KB-e91e63.svg"/></a>

### What is it / What does it do?
This is a very small library that allows you to send feedback from an app without internet connection via email, either directly or via an unintrusive notification. It only has 63 methods and one dependency (com.android.support:support-core-ui, necessary for the notification)
 
### Gradle (via [JitPack.io](https://jitpack.io/))

1. add jitpack to your project's `build.gradle`:

```groovy
repositories {
	maven { url "https://jitpack.io" }
}
```

2. add the compile statement to your module's `build.gradle`:

```groovy
dependencies {
	compile 'com.github.MFlisar:FeedbackManager:1.3'
}
```

### Usage - General

1. Create a feedback

```groovy
val files: List<File> = ...

val feedback = Feedback(
	listOf("admin@example.com"),
	"Feedback for My App v1.0"),
	// optional from here on
	text = "My email text..."
	attachments = files.map { FeedbackFile.DefaultName(it) } // files will be copied to the apps cache and provided via a simple cache file provider
)
```

2. Send the feedback mail

```groovy
String notificationChannel = ...;
int notificationId = ...;
int notificationIcon = ...;

// Variation 1:
// Show a notification, it can be clicked and then the user can select how he wants to send the feedback mail
feedback
	.startNotification(
		context,
		"Title of the email chooser dialog",
		"Notification Title", 
		"Notification Text",
		notificationIcon, 
		notificationChannel, 
		notificationId
	)

// Variation 2:
// Directly start the email chooser
feedback.startEmailChooser(context, "Title of the email chooser dialog")

// Variation 3:
// get the intent that can be started whenever desired
val emailIntent: Intent = feedback.buildIntent(context, "Title of the email chooser dialog")
```
