package com.mollom.client;

import static com.mollom.client.BaseMollomTest.PRIVATE_KEY;
import static com.mollom.client.BaseMollomTest.PUBLIC_KEY;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.SecureRandom;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import java.security.cert.X509Certificate;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import com.mollom.client.datastructures.CheckContentRequest;
import com.mollom.client.MollomClient.ContentCheck;
import com.mollom.client.datastructures.CheckContentResponse;
import com.mollom.client.datastructures.GetCaptchaResponse;
import com.mollom.client.datastructures.Language;
import com.mollom.client.datastructures.ReputationResponse;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.BeforeClass;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Meire
 */
public class MollomClientTest extends BaseMollomTest {

  private static TrustManager[] get_trust_mgr() {
    TrustManager[] certs = new TrustManager[]{
      new X509TrustManager() {

        public X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        public void checkClientTrusted(X509Certificate[] certs, String t) {
        }

        public void checkServerTrusted(X509Certificate[] certs, String t) {
        }
      }
    };
    return certs;
  }

  @BeforeClass
  public static void beforeClass() {
    try {
      // Create a context that doesn't check certificates. DO NOT USE IN PRODUCTION!
      SSLContext ssl_ctx = SSLContext.getInstance("TLS");
      TrustManager[] trust_mgr = get_trust_mgr();
      ssl_ctx.init(null, trust_mgr, new SecureRandom());
      HttpsURLConnection.setDefaultSSLSocketFactory(ssl_ctx.getSocketFactory());
      // Set a hostname verifier that always returns true. DO NOT USE IN PRODUCTION!
      HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {

        public boolean verify(String string, SSLSession ssls) {
          return true;
        }
      });
    } catch (Exception e) {
      System.err.println("Warning: didn't succeed in disabling https certificate verification!");
    }
  }

  /**
   * Test of checkContent method, of class MollomClient.
   */
  @Ignore
  @Test
  public void testCheckContentTestMode() throws Exception {
    CheckContentResponse response;
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY, true);

    CheckContentRequest request = new CheckContentRequest();

    request.postBody = "ham";
    response = client.checkContent(request);
    assertEquals(response.spam, CheckContentResponse.HAM);
    assertTrue(response.quality == 1);

    request.postBody = "spam";
    response = client.checkContent(request);
    assertEquals(response.spam, CheckContentResponse.SPAM);
    assertTrue(response.quality == 0);

    request.postBody = "unsure";
    response = client.checkContent(request);
    assertEquals(response.spam, CheckContentResponse.UNSURE);
  }

  @Test
  public void testEmptyCheckContent() {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);
    try {
      // this should not fail in production mode
      client.checkContent(new CheckContentRequest());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception while checking an empty post!");
    }

    client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY, true);
    try {
      // should also not fail in testing mode
      client.checkContent(new CheckContentRequest());
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception while checking an empty post!");
    }
  }

  @Test
  public void testCheckContentLiveMode() throws Exception {
    CheckContentResponse response;
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    CheckContentRequest request = new CheckContentRequest();
    request.postBody = "test string";

    // test the default checks
    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam != 0);
    assertTrue(response.quality == 0);
    assertTrue(response.profanity == 0);
    assertNull(response.language);
    String hash = response.session_id;

    request.sessionID = response.session_id;

    response = client.checkContent(request);
    assertEquals(hash, response.session_id);

    request.checks = new ContentCheck[]{ContentCheck.SPAM};

    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam != 0);
    assertTrue(response.quality == 0);
    assertTrue(response.profanity == 0);
    assertNull(response.language);
    assertEquals(hash, response.session_id);

    request.checks[0] = ContentCheck.QUALITY;
    request.postBody = "Dit is een mooi stukje tekst met een zekere kwaliteit.";

    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam == 0);
    assertTrue(0 <= response.quality && response.quality <= 1);
    assertTrue(response.profanity == 0);
    assertNull(response.language);
    assertEquals(hash, response.session_id);

    request.checks[0] = ContentCheck.PROFANITY;
    request.postBody = "This text really is one hell of a piece of shit! Fuck!";

    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam == 0);
    assertTrue(response.quality == 0);
    assertTrue(0 <= response.profanity && response.profanity <= 1);
    assertNull(response.language);
    assertEquals(hash, response.session_id);

    request.checks[0] = ContentCheck.LANGUAGE;
    request.postBody = "Dit is een stukje tekst om te zien of mollom de taal kan bepalen. Laat ons hopen van wel.";

    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam == 0);
    assertTrue(response.quality == 0);
    assertTrue(response.profanity == 0);
    assertNotNull(response.language);
    assertTrue(response.language.length > 0);
    assertTrue(response.language[0].language != null);
    assertTrue(0 <= response.language[0].confidence && response.language[0].confidence <= 1);
    assertEquals(hash, response.session_id);

    // test with multiple tests
    request.checks = new ContentCheck[]{
      ContentCheck.SPAM,
      ContentCheck.QUALITY,
      ContentCheck.PROFANITY,
      ContentCheck.LANGUAGE};
    request.postBody = "Dit is een stukje tekst om te zien of mollom de taal kan bepalen. Laat ons hopen van wel.";

    response = client.checkContent(request);
    assertNotNull(response.session_id);
    assertTrue(response.spam != 0);
    assertTrue(0 <= response.quality && response.quality <= 1);
    assertTrue(0 <= response.profanity && response.profanity <= 1);
    assertNotNull(response.language);
    assertTrue(response.language.length > 0);
    assertTrue(response.language[0].language != null);
    assertTrue(0 <= response.language[0].confidence && response.language[0].confidence <= 1);
    assertEquals(hash, response.session_id);

    // test the honeypot field
    request = new CheckContentRequest();
    request.postBody = "Dit is een stukje tekst om te zien of mollom de taal kan bepalen. Laat ons hopen van wel.";
    request.honeypot = "trapped";
    response = client.checkContent(request);
    assertTrue(response.isSpam());
    assertEquals(hash, response.session_id);
  }

  /**
   * Test of getImageCaptcha method, of class MollomClient.
   */
  @Ignore
  @Test
  public void testCheckCaptcha() throws Exception {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    assertTrue(client.checkCaptcha("unknown-session", "correct", null));
    assertFalse(client.checkCaptcha("unknown-session", "incorrect", null));
  }

  /**
   * Make sure the session_id doesn't change when we're not doing any load balancing.
   * Breaks sometime on xmlrpc.mollom.com, can't reproduce locally...
   * @throws Exception
   */
  @Test
  public void testContentSessionIDConsistency() throws Exception {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    CheckContentRequest request = new CheckContentRequest();
    request.postBody = "Simple text message";

    CheckContentResponse response = client.checkContent(request);
    String sessionId = response.session_id;
    for (int i = 0; i < 10; i++) {
      request.sessionID = sessionId;
      response = client.checkContent(request);
      assertEquals(sessionId, response.session_id);
    }
  }

  /**
   * Make sure the session_id doesn't change when we're not doing any load balancing.
   * Breaks sometime on xmlrpc.mollom.com, can't reproduce locally...
   * @throws Exception
   */
  @Test
  public void testCaptchaSessionIDConsistency() throws Exception {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    CheckContentRequest request = new CheckContentRequest();
    request.postBody = "Simple text message";

    CheckContentResponse response = client.checkContent(request);
    String sessionId = response.session_id;

    GetCaptchaResponse captcha;
    for (int i = 0; i < 10; i++) {
      captcha = client.getImageCaptcha(sessionId);
      assertEquals(sessionId, captcha.session_id);
    }
  }

  @Test
  public void checkContentUTF8 () {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    CheckContentRequest content = new CheckContentRequest();
    content.authorName = "¤†×÷  ¶    ‡    ±   —   §";
    content.postBody = "alala";

    try {
      CheckContentResponse response = client.checkContent(content);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  private void checkImageCaptchaData(String url) throws Exception {
    try {
      BufferedImage image = ImageIO.read(new URL(url));
      assertNotNull("Image at " + url + " is null!", image);
      assertTrue(image.getHeight() > 0);
      assertTrue(image.getWidth() > 0);
    } catch (Exception e) {
      e.printStackTrace();
      fail("Unexpected exception while checking captcha data.");
    }
  }

  /**
   * Test of get*Captcha methods, of class MollomClient.
   */
  //@Test
  public void testGetCaptcha() throws Exception {
    GetCaptchaResponse response;
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    response = client.getImageCaptcha();
    System.err.println("Fetched captcha: " + response.url);
    assertNotNull(response);
    assertTrue(response.url.endsWith(".png"));
    assertTrue(response.url.startsWith("http://"));
    assertNotNull(response.session_id);
    checkImageCaptchaData(response.url);

    response = client.getImageCaptcha(true);
    System.err.println("Fetched captcha: " + response.url);
    assertNotNull(response);
    assertTrue(response.url.endsWith(".png"));
    System.err.println(response.url);
    assertTrue(response.url.startsWith("https://"));
    assertNotNull(response.session_id);
    checkImageCaptchaData(response.url);

    response = client.getAudioCaptcha();
    System.err.println("Fetched captcha: " + response.url);
    assertNotNull(response);
    assertTrue(response.url.endsWith(".mp3"));
    assertTrue(response.url.startsWith("http://"));
    assertNotNull(response.session_id);
    //checkAudioCaptchaData(response.url);

    response = client.getAudioCaptcha(true);
    System.err.println("Fetched captcha: " + response.url);
    assertNotNull(response);
    assertTrue(response.url.endsWith(".mp3"));
    assertTrue(response.url.startsWith("https://"));
    assertNotNull(response.session_id);
    //checkAudioCaptchaData(response.url);
  }

  @Test
  public void testRealContentCaptcha() {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    try {
      CheckContentRequest request = new CheckContentRequest();
      request.postBody = "Hi dad, how are those pills working out? Do I have to buy you some new ones?";
      request.authorIP = "10.0.0.2";
      CheckContentResponse response = client.checkContent(request);

      // Normally, you should only request a captcha for unsure status;
      // To test the flow, we'll ignore that for this test
      // assertEquals(CheckContentResponse.UNSURE, response.spam);

      GetCaptchaResponse captcha = client.getImageCaptcha(response.session_id);
      assertNotNull(captcha.url);
      assertEquals(response.session_id, captcha.session_id);

      assertFalse(client.checkCaptcha(captcha.session_id, "something-wrong", "10.0.0.2"));
    } catch (Exception e) {
      fail("Unexpected exception while checking content: " + e.getMessage());
    }
  }

  /**
   * Test of sendFeedback method, of class MollomClient.
   */
  @Test
  public void testSendFeedback() throws Exception {
    MollomClient client = new MollomClient(PUBLIC_KEY, PRIVATE_KEY);

    // there's nothing we can check; but we don't expect any exceptions
    client.sendFeedback("session-id", null, MollomClient.Feedback.SPAM);
    client.sendFeedback("session-id", null, MollomClient.Feedback.UNWANTED);
    client.sendFeedback("session-id", null, MollomClient.Feedback.PROFANITY);
    client.sendFeedback("session-id", null, MollomClient.Feedback.QUALITY);
  }
}
