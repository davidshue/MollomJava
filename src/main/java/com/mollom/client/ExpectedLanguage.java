package com.mollom.client;

public enum ExpectedLanguage {
  AFRIKAANS("af"),
  ARABIC("ar"),
  BULGARIAN("bg"),
  BENGALI("bn"),
  CZECH("cs"),
  DANISH("da"),
  GERMAN("de"),
  GREEK("el"),
  ENGLISH("en"),
  SPANISH("es"),
  ESTONIAN("et"),
  PERSIAN("fa"),
  FINNISH("fi"),
  FRENCH("fr"),
  GUJARATI("gu"),
  HEBREW("he"),
  HINDI("hi"),
  CROATIAN("hr"),
  HUNGARIAN("hu"),
  INDONESIAN("id"),
  ITALIAN("it"),
  JAPANESE("ja"),
  KANNADA("kn"),
  KOREAN("ko"),
  LITHUANIAN("lt"),
  LATVIAN("lv"),
  MACEDONIAN("mk"),
  MALAYALAM("ml"),
  MARATHI("mr"),
  NEPALI("ne"),
  DUTCH("nl"),
  NORWEGIAN("no"),
  PUNJABI("pa"),
  POLISH("pl"),
  PORTUGUESE("pt"),
  ROMANIAN("ro"),
  RUSSIAN("ru"),
  SLOVAK("sk"),
  SLOVENE("sl"),
  SOMALI("so"),
  ALBANIAN("sq"),
  SWEDISH("sv"),
  SWAHILI("sw"),
  TAMIL("ta"),
  TELUGU("te"),
  THAI("th"),
  TAGALOG("tl"),
  TURKISH("tr"),
  UKRAINIAN("uk"),
  URDU("ur"),
  VIETNAMESE("vi"),
  CHINESE_SIMPLIFIED("zh-cn"),
  CHINESE_TRADITIONAL("zh-tw");
  
  private String code;

  private ExpectedLanguage(String code) {
    this.code = code;
  }

  public String getCode() {
    return code;
  }
}
