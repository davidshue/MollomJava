MollomJava
==========

A Java client library for Mollom, using the RESTful API as described on http://mollom.com/api

Library API
-----------
**Note**: When you determine the IP address of the author, please verify that you're using the
right IP. Some reverse proxies put their own IP in the REMOTE_ADDR header and the real IP
in HTTP_X_FORWARDED_FOR or HTTP_X_CLUSTER_CLIENT_IP. For more information, see
http://mollom.com/api/handling-author-ip-addresses

Checking content
```java
// create a new mollom client
MollomClient client = MollomClientBuilder.create()
    .withPlatformName("Example Application")
    .withPlatformVersion("1.0")
    // ... More client configurations ...
    .build("publicKey", "privateKey");

// create a new Content to check
Content content = new Content();
content.setAuthorIp("192.168.1.1");
content.setPostBody("This is a test comment.");
// ... More content properties ...

client.checkContent(content);

if (content.isHam()) {
    // Accept content
} else if(content.isUnsure()) {
    // Present captcha to the user for this content because Mollom was unsure
    Captcha captcha = client.createCaptcha(CaptchaType.IMAGE, false, content);
    String solution; // Present captcha.getUrl() to the user to get solution
    captcha.setSolution(solution);
    catpcha.setAuthorIp("192.168.1.1");
    client.checkCaptcha(captcha);
    
    if (captcha.isSolved()) {
        // Accept content
    } else {
        // Reject content
    }
} else {
    // Reject content
}

// Cleanup
client.destroy();
```

Adding blacklist entry
```java
// create a new mollom client
MollomClient client = MollomClientBuilder.create()
    // ... More client configurations ...
    .build("publicKey", "privateKey");
    
BlacklistEntry entry = new BlacklistEntry();
entry.setValue("spammyspamspam");
client.saveBlacklistEntry(entry);

// Cleanup
client.destroy();
```

Updating blacklist entry
```java
MollomClient client = MollomClientBuilder.create()
    // ... More client configurations ...
    .build("publicKey", "privateKey");
    
// Id of blacklist entry to update;
// Can also just list all of the blacklist entries using client.listBlacklistEntries();
String blacklistEntryId; 
BlacklistEntry entry = client.getBlacklistEntry(blacklistEntryId);
entry.setValue("newspammyspam");
client.saveBlacklistEntry(entry);

// Cleanup
client.destroy();
```

Adding whitelist entry
```java
// create a new mollom client
MollomClient client = MollomClientBuilder.create()
    // ... More client configurations ...
    .build("publicKey", "privateKey");
    
WhitelistEntry entry = new WhitelistEntry();
entry.setValue("nice_guy");
entry.setContext(Context.AUTHORID);
client.saveWhitelistEntry(entry);

// Cleanup
client.destroy();
```

Updating whitelist entry
```java
MollomClient client = MollomClientBuilder.create()
    // ... More client configurations ...
    .build("publicKey", "privateKey");
    
// Id of whitelist entry to update;
// Can also just list all of the whitelist entries using client.listWhitelistEntries();
String whitelistEntryId; 
WhitelistEntry entry = client.getWhitelistEntry(whitelistEntryId);
entry.setValue("nice_girl");
client.saveWhitelistEntry(entry);

// Cleanup
client.destroy();
```

Sending feedback
```java
MollomClient client = MollomClientBuilder.create()
    // ... More client configurations ...
    .build("publicKey", "privateKey");

// Previously checked content that was incorrectly classified
Content content; 

client.sendFeedback(content, FeedbackReason.SPAM);

// Cleanup
client.destroy();
```

Sub-API status
--------------

 - Supported:
  * Content API
  * Captcha API
  * Feedback API
  * Blacklist API
  * Whitelist API

 - Unsupported:
  * Site API (Resellers only)
