package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified dimension resource ID or name. Type can be {@code int} for pixel
 * size or {@code float} for exact amount.
 *
 * <pre><code>
 * {@literal @}BindDimen(R.dimen.horizontal_gap) or {@literal @}BindDimen(res = "horizontal_gap")
 * int horizontalGap;
 *
 * {@literal @}BindDimen(R.dimen.horizontal_gap) or {@literal @}BindDimen(res = "horizontal_gap")
 * float horizontalGap;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindDimen {
  /** Dimension resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Dimension resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
