# MollomJava

A Java client library for the [Mollom REST API](https://mollom.com/api).

## Requirements

* Jersey client 1.16 with Jersey OAuth library.

## Usage

For most API calls, you should provide an author IP address.  Ensure that your client determines the correct IP address of all site visitors.  HTTP-level caches and reverse-proxies typically forward the actual IP address in the `X-Forwarded-For` or `X-Cluster-Client-IP` HTTP request header.

For more information, see http://mollom.com/api/handling-author-ip-addresses


### Content

#### Checking content

```java
// Create a new Mollom client.
MollomClient client = MollomClientBuilder.create()
    .withPlatformName("Example Application")
    .withPlatformVersion("1.0")
    // ... more client configuration ...
    .build("publicKey", "privateKey");

// Create a new Content to check.
Content content = new Content();
content.setAuthorIp("192.168.1.1");
content.setPostBody("This is a test comment.");
// ... more content properties ...

try {
    client.checkContent(content);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
    // Trigger fail-over/fallback strategy.
}

if (content.isHam()) {
    // Accept the post.
} else if (content.isUnsure()) {
    // Ask the user to solve a CAPTCHA.
    Captcha captcha = client.createCaptcha(CaptchaType.IMAGE, false, content);
    catpcha.setAuthorIp("192.168.1.1");

    int captchaRetries = 3;
    String solution;

    for (int i = 0; i < captchaRetries; i++) {
        // Output the image from captcha.getUrl() to the user to get solution.
        solution = getUserSolution(captcha.getUrl());
        captcha.setSolution(solution);
        client.checkCaptcha(captcha);

        // If the user is able to change the post while solving the CAPTCHA,
        // ensure to re-check the content. If the CAPTCHA was solved, a
        // subsequent checkContent() call will return "ham" -- unless the user
        // edited the post to turn it into spam.
        client.checkContent(content)

        if (content.isHam()) {
            // Accept the post.
            return;
        }
    }

    // Unsure without solved CAPTCHA: Reject the post.
} else {
    // Spam: Reject the post.
}

// Cleanup.
client.destroy();
```

### Feedback

Your client should send feedback for a previously checked content or a CAPTCHA
that was inappropriately classified by Mollom, so it can learn from its
mistakes.

For example, if a spam message was erroneously classified as ham, the Mollom
service will use that information to adjust its future spam classifications.
Feedback also affects the reputation of the author of a post.

The more feedback is provided to Mollom, the more effective it becomes.


#### Sending feedback

```java
MollomClient client = MollomClientBuilder.create()
    // ...
    .build("publicKey", "privateKey");

// Previously checked content that was incorrectly classified.
Content content;

try {
    client.sendFeedback(content, FeedbackReason.SPAM);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}

client.destroy();
```

### Blacklist

Mollom automatically blocks unwanted content and learns from all participating
sites to improve its filters.  On top of automatic filtering, you can define a
custom blacklist.  Upon matching a blacklist entry, content is blocked.

#### Adding a blacklist entry

```java
MollomClient client = MollomClientBuilder.create()
    // ...
    .build("publicKey", "privateKey");

BlacklistEntry entry = new BlacklistEntry();
entry.setReason("spam");
entry.setValue("viagra");

try {
    client.saveBlacklistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}

client.destroy();
```

#### Updating a blacklist entry

```java
MollomClient client = MollomClientBuilder.create()
    // ...
    .build("publicKey", "privateKey");

// ID of a blacklist entry to update.
// You can list all blacklist entries using client.listBlacklistEntries().
String blacklistEntryId;

BlacklistEntry entry = client.getBlacklistEntry(blacklistEntryId);
entry.setValue("cialis");

try {
    client.saveBlacklistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}

client.destroy();
```

### Whitelist

Next to a custom blacklist, you can define a custom whitelist, which is checked
first.

Upon a positive whitelist match, no other checks are performed:
* Content API's spam check returns ham.
* Content API's profanity check returns non-profane.
* Blacklist entries are not checked.

#### Adding a whitelist entry

```java
MollomClient client = MollomClientBuilder.create()
    // ...
    .build("publicKey", "privateKey");

WhitelistEntry entry = new WhitelistEntry();
entry.setValue(123);
entry.setContext(Context.AUTHORID);

try {
    client.saveWhitelistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}

client.destroy();
```

#### Updating a whitelist entry

```java
MollomClient client = MollomClientBuilder.create()
    // ...
    .build("publicKey", "privateKey");

// ID of a whitelist entry to update.
// You can list all whitelist entries using client.listWhitelistEntries().
String whitelistEntryId;

WhitelistEntry entry = client.getWhitelistEntry(whitelistEntryId);
entry.setValue(321);

try {
    client.saveWhitelistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}

client.destroy();
```

## Known issues

* Site API is not supported yet. (Mollom Resellers)


## License

Copyright (c) 2010-2013 Mollom BVBA, https://mollom.com/  
Licensed under the MIT/Expat license:

Permission is hereby granted, free of charge, to any person obtaining
a copy of this software and associated documentation files (the
"Software"), to deal in the Software without restriction, including
without limitation the rights to use, copy, modify, merge, publish,
distribute, sublicense, and/or sell copies of the Software, and to
permit persons to whom the Software is furnished to do so, subject to
the following conditions:

The above copyright notice and this permission notice shall be included
in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

