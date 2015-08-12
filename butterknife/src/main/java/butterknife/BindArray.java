package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the specified array resource ID or name. The type of array will be inferred
 * from the annotated element.
 *
 * String array:
 * <pre><code>
 * {@literal @}BindArray(R.array.countries) or {@literal @}BindArray(res = "countries")
 * String[] countries;
 * </code></pre>
 *
 * Int array:
 * <pre><code>
 * {@literal @}BindArray(R.array.phones) or {@literal @}BindArray(res = "phones")
 * int[] phones;
 * </code></pre>
 *
 * Text array:
 * <pre><code>
 * {@literal @}BindArray(R.array.options) or {@literal @}BindArray(res = "options")
 * CharSequence[] options;
 * </code></pre>
 *
 * {@link android.content.res.TypedArray}:
 * <pre><code>
 * {@literal @}BindArray(R.array.icons) or {@literal @}BindArray(res = "icons")
 * TypedArray icons;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindArray {
  /** Array resource ID to which the field will be bound. */
  int value() default ButterKnife.NO_ID;
  /** Array resource ID to which the field will be bound, expressed as a {@link String}. */
  String res() default ButterKnife.NO_RESOURCE;
}
