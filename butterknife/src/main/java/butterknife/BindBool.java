package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified boolean resource ID or name.
 *
 * <pre><code>
 * {@literal @}BindBool(R.bool.is_tablet) or {@literal @}BindBool(res = "is_tablet")
 * boolean isTablet;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindBool {
  /** Boolean resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Boolean resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
