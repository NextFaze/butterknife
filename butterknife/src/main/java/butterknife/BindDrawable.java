package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified drawable resource ID or name.
 *
 * <pre><code>
 * {@literal @}BindDrawable(R.drawable.placeholder)
 * or {@literal @}BindDrawable(res = "placeholder")
 * Drawable placeholder;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindDrawable {
  /** Drawable resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Drawable resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
