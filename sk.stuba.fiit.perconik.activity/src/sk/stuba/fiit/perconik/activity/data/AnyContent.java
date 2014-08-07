package sk.stuba.fiit.perconik.activity.data;

import java.util.Map;

import javax.annotation.Nullable;

public interface AnyContent extends Content {
  public Map<String, Object> any();

  public void put(String key, @Nullable Object value);

  public Object get(String key);
}
