package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified integer resource ID or name.
 *
 * <pre><code>
 * {@literal @}BindInt(R.int.columns) or {@literal @}BindInt(res = "columns")
 * int columns;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindInt {
  /** Integer resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Integer resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
