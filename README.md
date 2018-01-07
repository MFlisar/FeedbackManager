### FeedbackManager [![Release](https://jitpack.io/v/MFlisar/FeedbackManager.svg)](https://jitpack.io/#MFlisar/FeedbackManager)

### What is it / What does it do?
This is a very small library that allows you to send feedback from an app without internet connection via email, either directly or via an unintrusive notification
 
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
	compile 'com.github.MFlisar:FeedbackManager:0.1'
}
```

### Usage - General

1. Create a builder

```groovy
File file = ...;
FeedbackBuilder builder = FeedbackBuilder.create()
	// required!
	.addReceiver("admin@example.com")
	// following things are optional
	.withSubject("Feedback for My App v1.0")
	.withText("My email text...")
	.addFile(file); // files will be copied to the apps cache and provided via a simple cache file provider
```

2. Send the feedback mail

```groovy
String notificationChannel = ...;
int notificationId = ...;
int notificationIcon = ...;

// Variation 1:
// Show a notification, it can be clicked and then the user can select how he wants to send the feedback mail
builder.startNotification(context, 
	"Title of the email chooser dialog", 
	"Notification Title", 
	"Notification Text", 
	notificationIcon, 
	notificationChannel, 
	notificationId);

// Variation 2:
// Directly start the email chooser
builder.startEmailChooser(context, "Title of the email chooser dialog");

// Variation 3:
// get the intent that can be started whenever desired
Intent emailIntent = builder.buildIntent(context, "Title of the email chooser dialog");
```
