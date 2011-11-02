package org.apache.abdera2.examples.activities;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.abdera2.activities.io.gson.AdaptedType;
import org.apache.abdera2.activities.io.gson.SimpleAdapter;
import org.apache.abdera2.activities.model.ASObject;
import org.apache.abdera2.activities.model.IO;
import org.apache.abdera2.common.anno.Name;
import org.apache.abdera2.common.http.EntityTag;

public class ExtensionExample {

  public static void main(String... args) throws Exception {
    
    // create the io with our custom type adapter
    IO io = IO.get(new BarAdapter());
    io.addPropertyMapping("bar", Bar.class);
    io.addPropertyMapping("etag", EntityTag.class);
    
    ASObject as = new ASObject("foo");
    Foo foo = as.extend(Foo.class);
    foo.setETag(new EntityTag("test",true));
    foo.setBar(new Bar("foobarbaz"));
        
    Map<Bar,String> map = new HashMap<Bar,String>();
    map.put(new Bar("z"),"a");
    map.put(new Bar("y"), "b");
    as.setProperty("map",map);
    
    // outputs: foo
    System.out.println(as.getObjectType());
    
    // outputs: {"etag":"W/\"test\"","map":{"y":"b","z":"a"},"bar":"foobarbaz","objectType":"foo"}
    as.writeTo(io,System.out);
    
    // now try reading it
    StringReader sr = new StringReader(io.write(as));
    as = io.readObject(sr);
    foo = as.extend(Foo.class);

    System.out.println();
    
    // check to make sure the etag was deserialized properly
    System.out.println(foo.getETag().getClass());
    
    System.out.println(foo.getBar().getClass());
    
    // map will deserialize as an asobject
    System.out.println(as.getProperty("map").getClass());
    
  }
  
  // the extension interface... defines the additional properties 
  // we're going to use on our ASObject... 
  public static interface Foo {
    @Name("etag") EntityTag getETag();
    @Name("etag") void setETag(EntityTag etag);
    Bar getBar();
    void setBar(Bar bar);
  }
  
  // Some new class that we want to use as a value.. need to tell 
  // the serializer how to handle it!
  public static class Bar {
    private final String s;
    public Bar(String s) {
      this.s = s;
    }
    public String toString() {
      return s;
    }
  }
  
  // The (de)serializer for the Bar class.. SimpleAdapter uses 
  // toString to serialize.. other TypeAdapter implementations
  // can use any strategy for serialization
  @AdaptedType(Bar.class)
  public static class BarAdapter 
    extends SimpleAdapter<Bar> {
    protected Bar deserialize(String v) {
      return new Bar(v);
    }
  }
}
