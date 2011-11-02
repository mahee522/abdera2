package org.apache.abdera2.common.security;

import java.security.Key;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;

import com.google.common.base.Supplier;

/**
 * Utility Class used for Generating API Keys
 */
public class ApiKey extends KeyBase {

  public ApiKey(byte[] key, int size) {
    super(key, size);
  }

  public ApiKey(byte[] key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(Key key, int size) {
    super(key, size);
  }

  public ApiKey(Key key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(Key key) {
    super(key);
  }

  public ApiKey(String key, int size) {
    super(key, size);
  }

  public ApiKey(String key, String alg, int size) {
    super(key, alg, size);
  }

  public ApiKey(String key) {
    super(key);
  }

  /**
   * Generates a random string that can be used as an API Key.
   * The string is generated by creating a random array of 
   * bytes, generating an hmac, then base64 encoding those. 
   * All non alphanumeric characters in the base64 encoded
   * result are then replaced with periods ('.') to simplify
   * the result a bit more. The resulting API Key can be 
   * expected to be reasonably random and suitable for use
   * within a request URI (e.g. key={apikey}).
   */
  public String generateNext() {
    int len = Math.min(20, size);
    byte[] buf = hmac(randomBytes(len));
    buf = Base64.encodeBase64(buf, false, true);
    StringBuilder sb = new StringBuilder();
    for (byte b : buf)
      sb.append(
        Character.isLetterOrDigit(b)?(char)b:'.');
    return sb.toString();
  }
 
  public String generateNextHex() {
    int len = Math.min(20, size);
    byte[] buf = hmac(randomBytes(len));
    return Hex.encodeHexString(buf);
  }
  
  public Supplier<String> asSupplier() {
    return supplier(this);
  }
  
  public static ApiKey WEAK(byte[] key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey WEAK(Key key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey WEAK(String key) {
    return new ApiKey(key,"HmacSHA1",20);
  }
  
  public static ApiKey MEDIUM(byte[] key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey MEDIUM(Key key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey MEDIUM(String key) {
    return new ApiKey(key,"HmacSHA256",256);
  }
  
  public static ApiKey STRONG(byte[] key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
  
  public static ApiKey STRONG(Key key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
  
  public static ApiKey STRONG(String key) {
    return new ApiKey(key,"HmacSHA512",512);
  }
  
  public static Supplier<String> supplier(ApiKey key) {
    return new ApiKeySupplier(key);
  }
  
  public static Supplier<String> supplierHex(ApiKey key) {
    return new ApiKeySupplier(key,true);
  }
  
  public static Supplier<String> weakSupplier(byte[] key) {
    return new ApiKeySupplier(WEAK(key));
  }
  
  public static Supplier<String> weakSupplier(Key key) {
    return new ApiKeySupplier(WEAK(key));
  }
  
  public static Supplier<String> weakSupplier(String key) {
    return new ApiKeySupplier(WEAK(key));
  }
  
  public static Supplier<String> weakSupplierHex(byte[] key) {
    return new ApiKeySupplier(WEAK(key),true);
  }
  
  public static Supplier<String> weakSupplierHex(Key key) {
    return new ApiKeySupplier(WEAK(key),true);
  }
  
  public static Supplier<String> weakSupplierHex(String key) {
    return new ApiKeySupplier(WEAK(key),true);
  }
  
  public static Supplier<String> mediumSupplier(byte[] key) {
    return new ApiKeySupplier(MEDIUM(key));
  }
  
  public static Supplier<String> mediumSupplier(Key key) {
    return new ApiKeySupplier(MEDIUM(key));
  }
  
  public static Supplier<String> mediumSupplier(String key) {
    return new ApiKeySupplier(MEDIUM(key));
  }
  
  public static Supplier<String> mediumSupplierHex(byte[] key) {
    return new ApiKeySupplier(MEDIUM(key),true);
  }
  
  public static Supplier<String> mediumSupplierHex(Key key) {
    return new ApiKeySupplier(MEDIUM(key),true);
  }
  
  public static Supplier<String> mediumSupplierHex(String key) {
    return new ApiKeySupplier(MEDIUM(key),true);
  }
  
  public static Supplier<String> strongSupplier(byte[] key) {
    return new ApiKeySupplier(STRONG(key));
  }
  
  public static Supplier<String> strongSupplier(Key key) {
    return new ApiKeySupplier(STRONG(key));
  }
  
  public static Supplier<String> strongSupplier(String key) {
    return new ApiKeySupplier(STRONG(key));
  }
  
  public static Supplier<String> strongSupplierHex(byte[] key) {
    return new ApiKeySupplier(STRONG(key),true);
  }
  
  public static Supplier<String> strongSupplierHex(Key key) {
    return new ApiKeySupplier(STRONG(key),true);
  }
  
  public static Supplier<String> strongSupplierHex(String key) {
    return new ApiKeySupplier(STRONG(key),true);
  }
  
  private static class ApiKeySupplier 
    implements Supplier<String> {
    private final ApiKey key;
    private final boolean hex;
    ApiKeySupplier(ApiKey key) {
      this.key = key;
      this.hex = false;
    }
    ApiKeySupplier(ApiKey key, boolean hex) {
      this.key = key;
      this.hex = hex;
    }
    public String get() {
      return !hex ? key.generateNext() : key.generateNextHex();
    }
    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + (hex ? 1231 : 1237);
      result = prime * result + ((key == null) ? 0 : key.hashCode());
      return result;
    }
    @Override
    public boolean equals(Object obj) {
      if (this == obj)
        return true;
      if (obj == null)
        return false;
      if (getClass() != obj.getClass())
        return false;
      ApiKeySupplier other = (ApiKeySupplier) obj;
      if (hex != other.hex)
        return false;
      if (key == null) {
        if (other.key != null)
          return false;
      } else if (!key.equals(other.key))
        return false;
      return true;
    }
  }

}
