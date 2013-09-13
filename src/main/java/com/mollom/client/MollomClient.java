package com.mollom.client;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Primary interaction point with all of the Mollom services.
 */
public class MollomClient {
  private final static Logger logger = Logger.getLogger("com.mollom.client.MollomClient");
  private final Client client;
  private final int retries;
  private final boolean acceptAllPostsOnError;
  private final boolean debugMode;

  private final Unmarshaller unmarshaller;
  private final DocumentBuilder documentBuilder;

  private final WebResource contentResource;
  private final WebResource captchaResource;
  private final WebResource feedbackResource;
  private final WebResource blacklistResource;

  /**
   * MollomClient instances are expensive resources. It is recommended that a single MollomClient
   * instance is shared between multiple threads. The building of requests and receiving of
   * responses is guaranteed to be thread safe.
   */
  MollomClient(Client client, WebResource contentResource, WebResource captchaResource, WebResource feedbackResource, WebResource blacklistResource,
      int retries, boolean acceptAllPostsOnError, boolean debugMode) {
    this.client = client;
    this.contentResource = contentResource;
    this.captchaResource = captchaResource;
    this.feedbackResource = feedbackResource;
    this.blacklistResource = blacklistResource;

    this.retries = retries;
    this.acceptAllPostsOnError = acceptAllPostsOnError;
    this.debugMode = debugMode;

    try {
      JAXBContext jaxbContext = JAXBContext.newInstance(Content.class, Captcha.class);
      this.unmarshaller = jaxbContext.createUnmarshaller();

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      this.documentBuilder = documentBuilderFactory.newDocumentBuilder();
    } catch (JAXBException | ParserConfigurationException e) {
      throw new MollomConfigurationException("Failed to initialize MollomClient.", e);
    }
  }

  /**
   * Take in a user-built Content object, make a request to the Mollom service to classify
   * the given Content, then inject the classification scores back into the original
   * Content object.
   * If mollom servers cannot be reached after all of the retries have been expended, classify
   * the content as either ham or spam (depending on the acceptAllPostsOnError setting
   * with the reason of "error".
   * 
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   */
  public void checkContent(Content content) {
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    postParams.putSingle("postTitle", content.getPostTitle());
    postParams.putSingle("postBody", content.getPostBody());
    postParams.putSingle("authorName", content.getAuthorName());
    postParams.putSingle("authorUrl", content.getAuthorUrl());
    postParams.putSingle("authorMail", content.getAuthorMail());
    postParams.putSingle("authorIp", content.getAuthorIp());
    postParams.putSingle("authorId", content.getAuthorId());

    // The Mollom service expects Openids as a space-separated list
    String openIds = "";
    if (content.getAuthorOpenIds() != null) {
      for (String authorOpenId : content.getAuthorOpenIds()) {
        openIds += authorOpenId += " ";
      }
    }
    postParams.putSingle("authorOpenid", openIds);

    // The Mollom service expects checks as a multi-param
    if (content.getChecks() != null) {
      List<String> checks = new ArrayList<>();
      for (Check check : content.getChecks()) {
        checks.add(check.toString().toLowerCase());
      }
      postParams.put("checks", checks);
    }

    postParams.putSingle("unsure", content.getPostTitle());
    postParams.putSingle("strictness", content.getStrictness().toString().toLowerCase());
    postParams.putSingle("rateLimit", Integer.toString(content.getRateLimit()));
    postParams.putSingle("honeypot", content.getHoneypot());
    postParams.putSingle("stored", content.isStored() ? "1" : "0");
    postParams.putSingle("url", content.getUrl());
    postParams.putSingle("contextUrl", content.getContextUrl());
    postParams.putSingle("contextTitle", content.getContextTitle());

    // If the user passes in a brand new Content object, map to the Create Content API
    // If the user passes in a previously checked Content object, map to the Update (and re-check) Content API
    ClientResponse response;
    if (content.getId() == null) { // Check new content
      response = request("post", contentResource, postParams);
    } else { // Recheck existing content
      response = request("post", contentResource.path(content.getId()), postParams);
    }
    if (response != null) { // Success
      // Get the returned content from the Mollom service
      Content returnedContent = parseBody(response.getEntity(String.class), "content", Content.class);

      // Inject the Mollom assigned ID
      content.setId(returnedContent.getId());

      // Merge classification results into the original Content object
      List<Check> requestedChecks = Arrays.asList(content.getChecks());
      if (requestedChecks.contains(Check.SPAM)) {
        content.setSpamClassification(returnedContent.getSpamClassification());
        content.setSpamScore(returnedContent.getSpamScore());
      }
      if (requestedChecks.contains(Check.QUALITY)) {
        content.setQualityScore(returnedContent.getQualityScore());
      }
      if (requestedChecks.contains(Check.PROFANITY)) {
        content.setProfanityScore(returnedContent.getProfanityScore());
      }
      if (requestedChecks.contains(Check.LANGUAGE)) {
        content.setLanguages(returnedContent.getLanguages());
      }
    } else { // Failure
      if (acceptAllPostsOnError) {
        content.setSpamClassification("ham");
        content.setSpamScore(0.0);
        content.setReason("error");
      } else {
        content.setSpamClassification("spam");
        content.setSpamScore(1.0);
        content.setReason("error");
      }
    }
  }

  /**
   * Creates a new Captcha object with a url to the captcha resource.
   * Use this to create a Captcha that isn't associated with any Content.
   * This is most commonly used for Captcha-only checks that don't involve Mollom text analysis.
   * 
   * @return The created Captcha object, or null if one could not be created
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   */
  public Captcha createCaptcha(CaptchaType captchaType, boolean ssl) {
    return createCaptcha(captchaType, ssl, null);
  }

  /**
   * Creates a new Captcha object with a url to the captcha resource.
   * A content should be passed in to associate the captcha with a previous unsure content request.
   * 
   * @return The created Captcha object, or null if one could not be created
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   */
  public Captcha createCaptcha(CaptchaType captchaType, boolean ssl, Content content) {
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    postParams.putSingle("type", captchaType.toString().toLowerCase());
    postParams.putSingle("ssl", ssl ? "1" : "0");
    postParams.putSingle("contentId", content.getId());
    
    ClientResponse response = request("post", captchaResource, postParams);
    if (response != null) { // Success
      return parseBody(response.getEntity(String.class), "captcha", Captcha.class);
    } else { // Failure
      return null;
    }
  }

  /**
   * Take the Captcha object (after the user has injected the solution and author information), make a
   * request to the Mollom service to determine whether or not the solution was correct, and inject the
   * result back into the Captcha object.
   * If mollom servers cannot be reached after all of the retries have been expended, mark as either
   * solved or not solved (depending on the acceptAllPostsOnError setting
   * with the reason of "error".
   * 
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   */
  public void checkCaptcha(Captcha captcha) {
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    postParams.putSingle("solution", captcha.getSolution());
    postParams.putSingle("authorIp", captcha.getAuthorIp());
    postParams.putSingle("authorId", captcha.getAuthorId());

    ClientResponse response = request("post", captchaResource.path(captcha.getId()), postParams);
    if (response != null) { // Success
      Captcha returnedCaptcha = parseBody(response.getEntity(String.class), "captcha", Captcha.class);
      captcha.setSolved(returnedCaptcha.isSolved() ? 1 : 0);
      captcha.setReason(returnedCaptcha.getReason());
    } else { // Failure
      if (acceptAllPostsOnError) {
        captcha.setSolved(1);
        captcha.setReason("error");
      } else {
        captcha.setSolved(0);
        captcha.setReason("error");
      }
    }
  }

  /**
   * Send feedback for a previously checked content.
   * This mechanism allows Mollom to learn from its mistakes. For example, if a spam message was erroneously classified as ham, Mollom will use that information
   * to fine-tune its spam detection mechanisms. The more feedback provided to Mollom, the more effective it becomes. If possible, this feedback mechanism
   * should be built into all Mollom applications.
   * Feedback also affects the reputation of the author of the content.
   */
  public void sendFeedback(Content content, FeedbackReason feedback) {
    sendFeedback(content, null, feedback);
  }

  /**
   * Send feedback for a previously checked captcha.
   * Feedback affects the reputation of the author of the content.
   * This call should be used only when only using captchas. It is not necessary to send feedback for a captcha if it was associated with a Content already.
   */
  public void sendFeedback(Captcha captcha, FeedbackReason feedback) {
    sendFeedback(null, captcha, feedback);
  }

  private void sendFeedback(Content content, Captcha captcha, FeedbackReason feedback) {
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    if (content != null) {
      postParams.putSingle("contentId", content.getId());
    }
    if (captcha != null) {
      postParams.putSingle("captchaId", captcha.getId());
    }
    postParams.putSingle("reason", feedback.toString().toLowerCase());
    request("post", feedbackResource, postParams);
  }

  /**
   * Notify Mollom that the content has been stored on the client-side.
   * Used to submit content to Mollom's Content Moderation Platform.
   * 
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   * @see MollomClient#markAsStored(Content, String, String, String)
   */
  public void markAsStored(Content content) {
    markAsStored(content, null, null, null);
  }

  /**
   * Notify Mollom that the content has been stored on the client-side.
   * Used to submit content to Mollom's Content Moderation Platform.
   * 
   * @throws MollomRequestException
   *           If the request failed and debug mode is enabled
   */
  public void markAsStored(Content content, String url, String contextUrl, String contextTitle) {
    content.setChecks(); // Don't re-check anything
    content.setStored(true);
    content.setUrl(url);
    content.setContextUrl(contextUrl);
    content.setContextTitle(contextTitle);
    checkContent(content);
  }

  /**
   * Notify Mollom that the content has been deleted on the client-side.
   * Used to remove content from Mollom's Content Moderation Platform.
   * 
   * @throws MollomRequestException
   *           If the request failed
   */
  public void markAsDeleted(Content content) {
    content.setChecks(); // Don't re-check anything
    content.setStored(false);
    checkContent(content);
  }

  /**
   * Saves a blacklist entry to Mollom.
   * If the blacklist entry already exists, update Mollom with the new properties.
   */
  public void saveBlacklistEntry(BlacklistEntry blacklistEntry) {
    MultivaluedMap<String, String> postParams = new MultivaluedMapImpl();
    postParams.putSingle("value", blacklistEntry.getValue());
    postParams.putSingle("reason", blacklistEntry.getReason());
    postParams.putSingle("context", blacklistEntry.getContext());
    postParams.putSingle("match", blacklistEntry.getMatch());
    postParams.putSingle("status", blacklistEntry.isEnabled() ? "1" : "0");
    postParams.putSingle("note", blacklistEntry.getNote());

    if (blacklistEntry.getId() != null) { // Update existing entry
      request("post", blacklistResource.path(blacklistEntry.getId()), postParams);
    } else { // Create new entry
      request("post", blacklistResource, postParams);
    }
  }

  /**
   * Deletes a blacklist entry from Mollom.
   */
  public void deleteBlacklistEntry(BlacklistEntry blacklistEntry) {
    request("post", blacklistResource.path(blacklistEntry.getId()).path("delete"), new MultivaluedMapImpl());
  }

  /**
   * @return A list of all of the blacklist entries (owned by this public key).
   */
  public List<BlacklistEntry> listBlacklistEntries() {
    ClientResponse response = request("get", blacklistResource, new MultivaluedMapImpl());
    if (response != null) { // Success
      return parseList(response.getEntity(String.class), "entry", BlacklistEntry.class);
    } else { // Failure
      return new ArrayList<>();
    }
  }

  /**
   * @return The blacklist entry wtih the given id. (or null if one doesn't exist)
   */
  public BlacklistEntry getBlacklistEntry(String blacklistEntryId) {
    ClientResponse response = request("get", blacklistResource.path(blacklistEntryId), new MultivaluedMapImpl());
    if (response != null) { // Success
      return parseBody(response.getEntity(String.class), "entry", BlacklistEntry.class);
    } else { // Failure
      return null;
    }
  }

  /**
   * Destroy the MollomClient object. Not doing this can cause connection leaks.
   * The MollomClient must not be reused after this method is called otherwise undefined behavior will occur.
   */
  public void destroy() {
    client.destroy();
  }

  private ClientResponse request(String method, WebResource resource, MultivaluedMap<String, String> params) {
    for (int retryAttemptNumber = 0; retryAttemptNumber <= retries; retryAttemptNumber++) {
      try {
        ClientResponse response = resource
            .accept(MediaType.APPLICATION_XML)
            .type(MediaType.APPLICATION_FORM_URLENCODED)
            .method(method, ClientResponse.class, params);
        if (response.getStatus() < 200 || response.getStatus() >= 300) {
          if (debugMode) {
            throw new MollomRequestException(response.getEntity(String.class));
          }
          return null;
        }
        return response;
      } catch (ClientHandlerException e) {
        logger.log(Level.SEVERE, "Failed to contact the Mollom server.");
      }
    }
    if (debugMode) {
      throw new MollomRequestException("Failed to contact the Mollom server after retries.");
    }
    return null;
  }
  
  /**
   * Expected xml in the format of:
   * <contentresponse> 
   *  <code>200</code> 
   *  <bodyTag>...</bodyTag>
   * </contentresponse>
   * 
   * @return JAXB unmarshalled expectedType object from the response.
   */
  private <T> T parseBody(String xml, String bodyTag, Class<T> expectedType) {
    try {
      // We have to parse the xml into a document first before passing it to JAXB to get the body
      // because the Mollom service returns the object wrapped in a <Response> object
      Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));
      Node bodyNode = document.getElementsByTagName(bodyTag).item(0);
      return unmarshaller.unmarshal(bodyNode, expectedType).getValue();
    } catch (SAXException | IOException | JAXBException e) {
      throw new MollomRequestException("Issue parsing response from Mollom server.", e);
    }
  }

  private <T> List<T> parseList(String xml, String bodyTag, Class<T> expectedType) {
    try {
      // We have to parse the xml into a document first before passing it to JAXB to get the body
      // because the Mollom service returns the object wrapped in a <Response> object
      Document document = documentBuilder.parse(new InputSource(new StringReader(xml)));

      List<T> list = new ArrayList<>();
      Node listNode = document.getElementsByTagName("list").item(0);
      NodeList bodyNodes = listNode.getChildNodes();
      for (int i = 0; i < bodyNodes.getLength(); i++) {
        Node bodyNode = bodyNodes.item(i);
        list.add(unmarshaller.unmarshal(bodyNode, expectedType).getValue());
      }
      return list;
    } catch (SAXException | IOException | JAXBException e) {
      throw new MollomRequestException("Issue parsing response from Mollom server.", e);
    }
  }
}
