package butterknife;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to the view for the specified ID or resource name. The view will automatically be
 * cast to the field type.
 *
 * <pre><code>
 * {@literal @}Bind(R.id.title) or {@literal @}Bind(res = "title")
 * TextView title;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface Bind {
  /** View ID to which the field will be bound. */
  int[] value() default { ButterKnife.NO_ID };
  /** View ID to which the field will be bound, expressed as a {@link String}. */
  String[] res() default { ButterKnife.NO_RESOURCE };
}
