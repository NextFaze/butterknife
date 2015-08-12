package butterknife;

import android.graphics.Bitmap;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * Bind a field to a {@link Bitmap} from the specified drawable resource ID or name.
 *
 * <pre><code>
 * {@literal @}BindBitmap(R.drawable.logo) or {@literal @}BindBitmap(res = "logo")
 * Bitmap logo;
 * </code></pre>
 */
@Retention(CLASS) @Target(FIELD)
public @interface BindBitmap {
  /** Drawable resource ID from which the {@link Bitmap} will be created. */
  int value() default ButterKnife.NO_ID;
  /** Drawable resource ID from which the {@link Bitmap} will be created,
   * expressed as a {@link String}.
   */
  String res() default ButterKnife.NO_RESOURCE;
}
