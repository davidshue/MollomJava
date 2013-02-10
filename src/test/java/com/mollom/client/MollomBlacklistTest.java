package com.mollom.client;

import com.mollom.client.datastructures.BlacklistEntry;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Thomas Meire
 */
public class MollomBlacklistTest extends BaseMollomTest {

  /**
   * Cleanup all entries after the test.
   */
  @After
  public void tearDown() throws Exception {
    MollomBlacklist blacklist = new MollomBlacklist(PUBLIC_KEY, PRIVATE_KEY);

    BlacklistEntry[] entries = blacklist.list();

    for (BlacklistEntry entry : entries) {
      blacklist.remove(entry);
    }

    blacklist = new MollomBlacklist("954b488186222a25496eac28d09f0239", "ff7de1635189cbe5772052977af29755");

    entries = blacklist.list();

    for (BlacklistEntry entry : entries) {
      blacklist.remove(entry);
    }
  }

  /**
   * Test of all methods, of class MollomBlacklist.
   */
  @Test
  public void testMethods() throws Exception {
    MollomBlacklist blacklist = new MollomBlacklist(PUBLIC_KEY, PRIVATE_KEY);

    BlacklistEntry entry = new BlacklistEntry();
    entry.text = "zever";
    entry.context = MollomBlacklist.Context.ALL_FIELDS;
    entry.reason = MollomBlacklist.Reason.QUALITY;
    entry.match = MollomBlacklist.Match.CONTAINS;

    BlacklistEntry[] entries1 = blacklist.list();

    // add the text and get a new list
    entry = blacklist.add(entry);
    BlacklistEntry[] entries2 = blacklist.list();

    // assert that there's one extra entry
    assertTrue(entries2.length == entries1.length + 1);

    // assert the added text was in the list
    int i = 0;
    while (i < entries2.length && !(entries2[i].text.equals(entry.text)
            && entries2[i].reason == entry.reason
            && entries2[i].context == entry.context
            && entries2[i].match == entry.match)) {
      i++;
    }
    assertTrue(i < entries2.length);

    // remove the text and get a new list
    blacklist.remove(entry);
    BlacklistEntry[] entries3 = blacklist.list();

    // assert the list contains one entry less than before removing
    assertTrue(entries2.length == entries3.length + 1);

    // assert the text was removed from the list
    i = 0;
    while (i < entries3.length && !(entries3[i].text.equals(entry.text)
            && entries3[i].reason == entry.reason
            && entries3[i].context == entry.context
            && entries3[i].match == entry.match)) {
      i++;
    }
    assertTrue(i == entries3.length);

    // make sure we get an exception for a missing text parameter
    entry = new BlacklistEntry();
    entry.context = MollomBlacklist.Context.ALL_FIELDS;
    entry.reason = MollomBlacklist.Reason.QUALITY;
    entry.match = MollomBlacklist.Match.CONTAINS;

    try {
      blacklist.add(entry);
      fail("No exception was thrown for missing text");
    } catch (Exception e) {
      System.err.println("Caught exception for text");
    }

    // make sure we get an exception for a missing context parameter
    entry = new BlacklistEntry();
    entry.text = "zever";
    entry.reason = MollomBlacklist.Reason.QUALITY;
    entry.match = MollomBlacklist.Match.CONTAINS;

    try {
      blacklist.add(entry);
    } catch (Exception e) {
      fail("Exception was thrown for optional context");
    }

    // make sure we get an exception for a missing reason parameter
    entry = new BlacklistEntry();
    entry.text = "zever";
    entry.context = MollomBlacklist.Context.ALL_FIELDS;
    entry.match = MollomBlacklist.Match.CONTAINS;

    try {
      blacklist.add(entry);
    } catch (Exception e) {
      fail("Exception was thrown for optional reason");
    }

    // make sure we get an exception for a missing match parameter
    entry = new BlacklistEntry();
    entry.text = "zever";
    entry.context = MollomBlacklist.Context.ALL_FIELDS;
    entry.reason = MollomBlacklist.Reason.QUALITY;

    try {
      blacklist.add(entry);
    } catch (Exception e) {
      fail("Exception was thrown for optional match");
    }
  }

  /**
   * Test of all methods, of class MollomBlacklist.
   *
	@Test
	public void testProfanity() throws Exception {
    CheckContentResponse response;

    // Use key which is not in developer mode:
    MollomClient client = new MollomClient("954b488186222a25496eac28d09f0239", "ff7de1635189cbe5772052977af29755");
    MollomBlacklist blacklist = new MollomBlacklist("954b488186222a25496eac28d09f0239", "ff7de1635189cbe5772052977af29755");

    CheckContentRequest profanityRequest = new CheckContentRequest();
    profanityRequest.postBody = "foobar";

    CheckContentRequest request = new CheckContentRequest();
    request.checks = new ContentCheck[]{ ContentCheck.PROFANITY };
    request.postBody = "foobar";

    // Test standard blacklist:
    response = client.checkContent(profanityRequest);
    assertTrue(response.profanity == 0);

    // Profanity is off by default:
    request.postBody = "nigger";

    response = client.checkContent(request);
    assertTrue(response.profanity == 0);

    profanityRequest.postBody = "nigger";

    response = client.checkContent(profanityRequest);
    assertFalse(response.profanity == 0);

    // Not "contains":
    profanityRequest.postBody = "tralaniggertrala";

    response = client.checkContent(profanityRequest);
    assertTrue(response.profanity == 0);

    profanityRequest.postBody = "bla bla nigger bla bla";

    response = client.checkContent(profanityRequest);
    assertFalse(response.profanity == 0);

    profanityRequest.postBody = "nigger";

    response = client.checkContent(profanityRequest);
    assertFalse(response.profanity == 0);

    profanityRequest.postTitle = "foo";
    profanityRequest.postBody = "bar";
    profanityRequest.authorName = "nigger";

    response = client.checkContent(request);
    assertFalse(response.profanity == 0);

    // Add a custom blacklist item:
		BlacklistEntry entry = new BlacklistEntry();

    entry.text = "nigzuar";
		entry.context = MollomBlacklist.Context.ALL_FIELDS;
		entry.reason = MollomBlacklist.Reason.PROFANITY;
		entry.match = MollomBlacklist.Match.CONTAINS;

		blacklist.add(entry);

    profanityRequest.postTitle = null;
    profanityRequest.postBody = "nigzuar";
    profanityRequest.authorName = null;

    // Test what happens if we edit the post:
    response = client.checkContent(profanityRequest);
    assertNotNull(response.session_id);
    assertFalse(response.profanity == 0);

    profanityRequest.sessionID = response.session_id;

    response = client.checkContent(response.session_id, null, "nigzuar", null, null, null, null, null, null, null, null, checks);
    assertFalse(response.profanity == 0);

    response = client.checkContent(response.session_id, null, "n-word", null, null, null, null, null, null, null, null, checks);
    assertTrue(response.profanity == 0);

    // Test "contains":
    response = client.checkContent(response.session_id, null, "trala-nigzuar-trala", null, null, null, null, null, null, null, null, checks);
    assertFalse(response.profanity == 0);

    // Test combination of local and global terms:
    response = client.checkContent(response.session_id, null, "nigger nigzuar", null, null, null, null, null, null, null, null, checks);
    assertFalse(response.profanity == 0);
	}*/
}
