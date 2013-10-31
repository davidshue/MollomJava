package com.mollom.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Language of the content.
 *
 * Given a very limited amount of text (minimum of 15 characters), Mollom can
 * detect its probable language (out of approximately 75 languages) with a high
 * degree of accuracy.
 */
@XmlRootElement(name = "language")
@XmlAccessorType(XmlAccessType.FIELD)
public class Language {
  private String languageCode;
  private double languageScore;

  /**
   * String representing either a two-character ISO-639-1 code (if no ISO-639-1
   * code is available, a ISO-639-3 three letter language code is returned)
   */
  public String getLanguageCode() {
    return languageCode;
  }

  /**
   * A numeric double representing Mollom's confidence in the accuracy of its
   * assessment.
   */
  public double getLanguageScore() {
    return languageScore;
  }
}
