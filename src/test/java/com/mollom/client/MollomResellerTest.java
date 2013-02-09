package com.mollom.client;

import com.mollom.client.MollomReseller.SiteInfo;
import static org.junit.Assert.*;
import org.junit.Test;

/**
 *
 * @author Thomas Meire
 */
public class MollomResellerTest extends BaseMollomTest {

  /**
   * Test of list method, of class MollomReseller.
   *
  @Test
  public void testAllMethods() throws Exception {
    // we're also a reseller, so use the same keys
    MollomReseller reseller = new MollomReseller(PUBLIC_KEY, PRIVATE_KEY);

    // add a new site
    SiteInfo info = new SiteInfo();
    info.url = "qa.mollom.com";
    info.mail = "thomas@mollom.com";
    info.language = "nl";
    info.status = true;
    info.testing = false;
    info.type = "non-profit";

    info = reseller.create(info);

    // make sure the public & private key are filled out
    assertNotNull(info.getPubkey());
    assertNotNull(info.getPrivkey());
    assertFalse(info.getPubkey().equals(info.getPrivkey()));

    String[] current = reseller.list();

    // make sure the new site is in the list
    boolean contains = false;
    for (String site : current) {
      if (site.equals(info.getPubkey())) {
        contains = true;
        continue;
      }
    }
    assertTrue("The list of sites doesn't contain the new site!", contains);

    // make sure we can get a copy of the site info
    SiteInfo fetched = reseller.get(info.getPubkey());
    assertEquals(info.language, fetched.language);
    assertEquals(info.mail, fetched.mail);
    assertEquals(info.status, fetched.status);
    assertEquals(info.testing, fetched.testing);
    assertEquals(info.type, fetched.type);
    assertEquals(info.url, fetched.url);

    // update the site we created earlier
    info.url = "qa2.mollom.com";
    info.mail = "johan@mollom.com";
    info.language = "en";
    info.testing = true;
    info.type = "company";
    reseller.update(info);

    // check whether the update was successful
    SiteInfo updated = reseller.get(info.getPubkey());
    assertEquals(info.language, updated.language);
    assertEquals(info.mail, updated.mail);
    assertEquals(info.status, updated.status);
    assertEquals(info.testing, updated.testing);
    assertEquals(info.type, updated.type);
    assertEquals(info.url, updated.url);

    // delete the site
    reseller.delete(info.getPubkey());
    try {
      SiteInfo deleted = reseller.get(info.getPubkey());
      System.err.println(deleted);
      fail("Excpected an exception!");
    } catch (Exception e) {}
  }
  */
}
