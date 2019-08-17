package org.robolectric.shadows;

import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureRequest.Key;
import android.os.Build.VERSION_CODES;
import android.view.Surface;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;

/**
 * Shadow class for {@link CaptureRequest.Builder}.
 *
 * <p>Original implementation would store its state in a local CameraMetadataNative object. Trying
 * to set these values causes issues while testing as that starts to involve native code.
 */
@Implements(value = CaptureRequest.Builder.class, minSdk = VERSION_CODES.LOLLIPOP)
public class ShadowCaptureRequestBuilder {

  private final Set<Surface> surface = Collections.synchronizedSet(new LinkedHashSet<>());
  private final Map<Key<?>, Object> characteristics = Collections.synchronizedMap(new HashMap<>());

  @Implementation
  public <T> void set(CaptureRequest.Key<T> key, T value) {
    characteristics.put(key, value);
  }

  @SuppressWarnings("unchecked")
  @Implementation
  public <T> T get(CaptureRequest.Key<T> key) {
    return (T) characteristics.get(key);
  }

  @Implementation
  public void addTarget(Surface outputTarget) {
    surface.add(outputTarget);
  }

  @Implementation
  public void removeTarget(Surface outputTarget) {
    surface.remove(outputTarget);
  }

  /** Fetch the set of surfaces configured in this builder. */
  public Set<Surface> getSurfaces() {
    return surface;
  }
}
