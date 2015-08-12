package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified color resource ID or name. Type can be {@code int} or
 * {@link android.content.res.ColorStateList}.
 *
 * <pre><code>
 * {@literal @}BindColor(R.color.background_green)
 * or @literal @}BindColor(res = "background_green")
 * int backgroundGreen;
 *
 * {@literal @}BindColor(R.color.background_green_selector)
 * or @literal @}BindColor(res = "background_green_selector")
 * ColorStateList backgroundGreenSelector;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindColor {
  /** Color resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Color resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
