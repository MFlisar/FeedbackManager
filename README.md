### FeedbackManager [![Release](https://jitpack.io/v/MFlisar/FeedbackManager.svg)](https://jitpack.io/#MFlisar/FeedbackManager) <a href="http://www.methodscount.com/?lib=com.github.MFlisar%3AFeedbackManager%3A1.0"><img src="https://img.shields.io/badge/Methods and size-core: 63 | deps: 7572 | 7 KB-e91e63.svg"/></a>

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
	compile 'com.github.MFlisar:FeedbackManager:1.1'
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

2. Adding files - customise behaviour

Files are exposed via a `ContentProvider` that copies the file to the app's cache directory. You can add files in those ways:

```groovy
File file = ...;
Uri fileUri = ...;
builder
        // 1) copy file to cache and use the original file name
	.addFile(file)
	.addFile(fileUri)
	.addFile(new FeedbackFile(file))
	.addFile(new FeedbackFile(fileUri))
	// 2) copy file to cache and use custom file name
	.addFile(new FeedbackFile(file).withCustomCacheFileName("my_file_name.txt"))
	.addFile(new FeedbackFile(fileUri).withCustomCacheFileName("my_second_file_name.txt"))
	// 3) copy file to cache and generate a unique name (uses the oringal name and adds a "_" + UUID before the file extension) 
	.addFile(new FeedbackFile(file).withGenerateUniqueName)
	.addFile(new FeedbackFile(fileUri).withGenerateUniqueName)
```

3. Send the feedback mail

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
