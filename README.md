MollomJava
==========

A Java client library for Mollom, using the RESTful API as described on http://mollom.com/api

Library API
-----------
The library API was inherited from the initial XMLRPC client library. For the 1.0 version, we
will try to keep this API as stable as possible. As an annoying side effect, there are some
conversions between different objects for the same data. For example the CheckContentResponse
response class is inherited and the Content and ContentResponse classes map directly to the
REST API. We thus have to convert the Content object to a CheckContentResponse object.

The 2.0 version will incorporate a library API that maps more directly to the REST API.

The most important class is the MollomClient class. It contains the functions to check content
and captcha's, and send feedback to Mollom. The MollomBlacklist class contains functionality
to manipulate your per-site blacklist.

**Note**: When you determine the IP address of the author, please verify that you're using the
right IP. Some reverse proxies put their own IP in the REMOTE_ADDR header and the real IP
in HTTP_X_FORWARDED_FOR or HTTP_X_CLUSTER_CLIENT_IP. For more information, see
http://mollom.com/api/handling-author-ip-addresses

```java
// create a new mollom client
MollomClient client = new MollomClient("public_key", "private_key");

// create a new request object
CheckContentRequest request = new CheckContentRequest();
request.authorName = "Thomas Meire";
request.postTitle = "Hello";
// TODO: add more fields...

CheckContentResponse contentResponse = client.checkContent(content);
if (contentResponse.isUnsure()) {
    GetCaptchaResponse captchaResponse = client.getImageCaptcha(contentResponse.session_id);
} else if (contentResponse.isHam()) {
    // store the post in the database
} else if (contentResponse.isSpam()) {
    // show a nice message to the user to explain the situation
}

// show the captcha to the user & get the response
String captchaSolution = "....";

boolean correct = client.checkCaptcha(captchaResponse.session_id, captchaSolution, "145.121.126.2");
if (correct) {
  // store the post in the database
} else {
  // retrieve a new captcha & show it again
}
```

Sub-API status
--------------

 - Supported:
  * Content API
  * Captcha API
  * Feedback API
  * Blacklist API

 - In-progress:
  * Whitelist API

 - Unsupported:
  * Site API
