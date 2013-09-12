package com.mollom.client;

/**
 * Denotes the strictness of Mollom spaminess checks.
 * - STRICT: more likely to classify content as spam
 * - NORMAL: normal behavior (default)
 * - RELAXED: more likely to classify content as ham
 */
public enum Strictness {
  STRICT, NORMAL, RELAXED
}
