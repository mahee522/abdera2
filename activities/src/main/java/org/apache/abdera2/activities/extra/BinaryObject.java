package org.apache.abdera2.activities.extra;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.activation.DataHandler;
import javax.activation.MimeType;

import org.apache.abdera2.activities.io.gson.Properties;
import org.apache.abdera2.activities.io.gson.Property;
import org.apache.abdera2.activities.model.objects.FileObject;
import org.apache.abdera2.common.anno.Name;
import org.apache.commons.codec.binary.Base64InputStream;
import org.apache.commons.codec.binary.Base64OutputStream;

/**
 * The BinaryObject is an extension object type that allows 
 * for an Activity Streams object representing arbitrary 
 * Base64 encoded data. It is an extension of the FileObject 
 * type so it inherits the fileUrl and mimeType properties
 * to communicate the permanent url of the data and the 
 * MIME media type. The content property is used to contain
 * the Base64 encoded data. This object is particularly 
 * useful in conjunction with the "attachments" property 
 * to attach arbitrary binary resources to another ASObject.
 * For instance, attaching a VCALENDAR file to an Event Object.
 */
@Name("binary")
@Properties({
  @Property(name="mimeType",to=MimeType.class)
})
public class BinaryObject extends FileObject {
  
  private static final long serialVersionUID = 5120608845229587281L;

  public BinaryObject() {
    setMimeType("application/octet-stream");
  }
  
  public BinaryObject(String displayName) {
    this();
    setDisplayName(displayName);
  }
    
  public void setContent(DataHandler data) throws IOException {
    setContent(data.getInputStream());
    setMimeType(data.getContentType());
  }
  
  public void setContent(InputStream data) throws IOException {
    ByteArrayOutputStream out = 
      new ByteArrayOutputStream();
    Base64OutputStream bout = 
      new Base64OutputStream(out,true,0,null);
    byte[] d = new byte[100];
    int r = -1;
    while((r = data.read(d)) > -1) { 
      bout.write(d, 0, r);
      bout.flush();
    }
    bout.close();
    String c = new String(out.toByteArray(),"UTF-8");
    super.setContent(c);
  }
  
  public InputStream getInputStream() throws IOException {
    String content = super.getContent();
    if (content == null) return null;
    ByteArrayInputStream in = 
      new ByteArrayInputStream(
        content.getBytes("UTF-8"));
    Base64InputStream bin = 
      new Base64InputStream(in);
    return bin;
  }
  
  public void setContent(byte[] data) throws IOException {
    setContent(new ByteArrayInputStream(data));
  }
  
  public void setContent(byte[] data, int s, int e) throws IOException {
    setContent(new ByteArrayInputStream(data,s,e));
  }
}
