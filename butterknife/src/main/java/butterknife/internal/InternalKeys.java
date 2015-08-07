package butterknife.internal;

/**
 * Contains shared constants between the annotation processor and {@link butterknife.ButterKnife}
 */
public final class InternalKeys {
  public static final String BINDING_CLASS_SUFFIX = "$$ViewBinder";
  public static final String ANDROID_PREFIX = "android.";
  public static final String JAVA_PREFIX = "java.";

  public static final int NO_ID = android.view.View.NO_ID;
  public static final String NO_RESOURCE = "";

  private InternalKeys() {
    throw new AssertionError("No instances.");
  }
}
