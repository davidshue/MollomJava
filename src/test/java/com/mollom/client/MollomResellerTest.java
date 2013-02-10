/**
 * Copyright (c) 2010-2012 Mollom. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *   * Redistributions of source code must retain the above copyright notice,
 *     this list of conditions and the following disclaimer.
 *   * Redistributions in binary form must reproduce the above copyright notice,
 *     this list of conditions and the following disclaimer in the documentation
 *     and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
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
