# MollomJava

A Java client library for the [Mollom REST API](https://mollom.com/api).

## Requirements

* Jersey client 1.16 with Jersey OAuth library.

Pre-built JARs are available for all [releases](https://github.com/Mollom/MollomJava/releases).

## Usage

For most API calls, you should provide an author IP address.  Ensure that your client determines the correct IP address of all site visitors.  HTTP-level caches and reverse-proxies typically forward the actual IP address in the `X-Forwarded-For` or `X-Cluster-Client-IP` HTTP request header.

For more information, see http://mollom.com/api/handling-author-ip-addresses

### Instantiation

The initial entry point for the Mollom API library is MollomClientBuilder, which builds a new MollomClient instance to interact with the Mollom service:

```java
// Create a new Mollom client.
MollomClient client = MollomClientBuilder.create()
    .withPlatformName("Spring")
    .withPlatformVersion("3.2.4")
    // ... more client configuration ...
    .build("publicKey", "privateKey");
```

MollomClient instances are expensive resources and a single MollomClient instance should be shared between multiple threads.  Requests and responses are thread-safe.

Before shutting down your application, ensure to destroy the MollomClient instance to prevent connection leaks:

```java
// Cleanup.
client.destroy();
```

Note: Every new instance automatically performs an initial request to verify your API keys.  This is expected to happen frequently, but no more than once per day.  If Mollom encounters too many/excessive API key verification calls, your API keys will be disabled.

### Testing Mollom

When testing your API implementation, it is recommended to use switch the client to testing mode, which uses the dev.mollom.com endpoint.
```java
// Create a new Mollom client in testing mode.
MollomClient client = MollomClientBuilder.create()
    .withPlatformName("Spring")
    .withPlatformVersion("3.2.4")
    .withTesting(true)
    // ... more client configuration ...
    .build("publicKey", "privateKey");
```
Testing mode differences:
- checkContent() only reacts to the literal strings "spam", "ham", and "unsure" in the postTitle and postBody parameters.
- If none of the literal strings is contained, and no blacklist/whitelist entries matched, the final spamClassification will be "unsure".
- checkCaptcha() only accepts "correct" for image CAPTCHAs and "demo" for audio CAPTCHAs as the correct solution.

### Content

#### Checking content

```java
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
    captcha.setAuthorIp("192.168.1.1");

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
// Previously checked content that was incorrectly classified.
Content content;

try {
    client.sendFeedback(content, FeedbackReason.SPAM);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}
```

### Blacklist

Mollom automatically blocks unwanted content and learns from all participating
sites to improve its filters.  On top of automatic filtering, you can define a
custom blacklist.  Upon matching a blacklist entry, content is blocked.

#### Adding a blacklist entry

```java
BlacklistEntry entry = new BlacklistEntry();
entry.setReason("spam");
entry.setValue("argaiv");

try {
    client.saveBlacklistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}
```

#### Updating a blacklist entry

```java
// ID of a blacklist entry to update.
// You can list all blacklist entries using client.listBlacklistEntries().
String blacklistEntryId;

BlacklistEntry entry = client.getBlacklistEntry(blacklistEntryId);
entry.setValue("silaic");

try {
    client.saveBlacklistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}
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
WhitelistEntry entry = new WhitelistEntry();
entry.setValue(123);
entry.setContext(Context.AUTHORID);

try {
    client.saveWhitelistEntry(entry);
} catch (MollomException e) {
    // Invalid request or Mollom service downtime.
}
```

#### Updating a whitelist entry

```java
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

