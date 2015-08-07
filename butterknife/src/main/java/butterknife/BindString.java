package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified string resource ID or name.
 *
 * <pre><code>
 * {@literal @}BindString(R.string.username_error)
 * or {@literal @}BindString(res = "username_error")
 * String usernameError;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindString {
  /** String resource ID to which the field will be bound. */
  int value() default butterknife.internal.InternalKeys.NO_ID;
  /** String resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default butterknife.internal.InternalKeys.NO_RESOURCE;
}
