package butterknife.internal;

import butterknife.Bind;
import butterknife.BindArray;
import butterknife.BindBitmap;
import butterknife.BindBool;
import butterknife.BindColor;
import butterknife.BindDimen;
import butterknife.BindDrawable;
import butterknife.BindInt;
import butterknife.BindString;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnEditorAction;
import butterknife.OnFocusChange;
import butterknife.OnItemClick;
import butterknife.OnItemLongClick;
import butterknife.OnItemSelected;
import butterknife.OnLongClick;
import butterknife.OnPageChange;
import butterknife.OnTextChanged;
import butterknife.OnTouch;
import org.w3c.dom.Document;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.Diagnostic.Kind;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static javax.tools.Diagnostic.Kind.ERROR;

abstract class ButterKnifeAbstractProcessor extends AbstractProcessor {

    public static final String NO_RESOURCE = InternalKeys.NO_RESOURCE;
    public static final String MANIFEST_FILE = "androidManifestFile";
    public static final String RESOURCE_PACKAGE_NAME = "resourcePackageName";

    private static final String[] SUPPORTED_OPTIONS =
            {MANIFEST_FILE, RESOURCE_PACKAGE_NAME};

    private static final String NULL = "null";

    private static final String PACKAGE = "package";
    private static final String VALUE = "value";
    private static final String RESOURCE = "res";

    private static final String NONE_DECLARED_ERROR = "@%s has no resource id or name declared. You must declare " +
            "exactly one value for field (%s.%s)";
    private static final String BOTH_DECLARED_ERROR = "@%s is declaring both id and resource name. You can only declare " +
            "one value type per field. (%s.%s)";
    private static final String INVALID_ANNOTATION_ERROR = "@%s is not a valid resource annotation. (%s.%s)";
    private static final String UNRECOGNISED_ANNOTATION_ERROR = "@%s is not a recognised annotation. (%s.%s)";

    private static final Map<Class<? extends Annotation>, String> BINDING_CLASS_TYPE_MAP = new HashMap<>();

    static {
        BINDING_CLASS_TYPE_MAP.put(Bind.class, "id");
        BINDING_CLASS_TYPE_MAP.put(BindArray.class, "array");
        BINDING_CLASS_TYPE_MAP.put(BindBitmap.class, "drawable");
        BINDING_CLASS_TYPE_MAP.put(BindBool.class, "bool");
        BINDING_CLASS_TYPE_MAP.put(BindColor.class, "color");
        BINDING_CLASS_TYPE_MAP.put(BindDimen.class, "dimen");
        BINDING_CLASS_TYPE_MAP.put(BindDrawable.class, "drawable");
        BINDING_CLASS_TYPE_MAP.put(BindInt.class, "integer");
        BINDING_CLASS_TYPE_MAP.put(BindString.class, "string");
        BINDING_CLASS_TYPE_MAP.put(OnCheckedChanged.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnClick.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnEditorAction.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnFocusChange.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnItemClick.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnItemLongClick.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnItemSelected.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnLongClick.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnPageChange.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnTextChanged.class, "id");
        BINDING_CLASS_TYPE_MAP.put(OnTouch.class, "id");
    }

    private String mResourcePackageName;

    @Override
    public Set<String> getSupportedOptions() {
        Set<String> options = new HashSet<>();
        Collections.addAll(options, SUPPORTED_OPTIONS);
        return options;
    }

    @Override
    public synchronized void init(ProcessingEnvironment env) {
        super.init(env);
        setResourcePackageName(env.getOptions().get(RESOURCE_PACKAGE_NAME));
        setManifestFile(env.getOptions().get(MANIFEST_FILE));
    }

    protected void setManifestFile(String manifestFile) {
        if (!emptyOrNull(manifestFile)) {
            File androidManifestFile = new File(manifestFile);
            if (androidManifestFile.exists()) {
                String packageName = parsePackageName(androidManifestFile);
                if (!emptyOrNull(packageName)) {
                    setResourcePackageName(packageName);
                } else {
                    printError("Could not parse package name from manifest file (%s). " +
                            "Consider passing the resource package directly as an apt argument " +
                            "[resourcePackageName]", manifestFile);
                }
            } else {
                printError("Manifest file (%s) not found. " +
                        "Consider passing the resource package directly as an apt argument " +
                        "[resourcePackageName]", manifestFile);
            }
        }
    }

    private String parsePackageName(File manifestFile) {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();

        Document doc;
        try {
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            doc = docBuilder.parse(manifestFile);
        } catch (Exception e) {
            return null;
        }

        org.w3c.dom.Element documentElement = doc.getDocumentElement();
        documentElement.normalize();

        return documentElement.getAttribute(PACKAGE);
    }

    protected void setResourcePackageName(String packageName) {
        if (emptyOrNull(mResourcePackageName)) {
            if (!emptyOrNull(packageName)) {
                mResourcePackageName = packageName + ".R";
            }
        } else if (!emptyOrNull(packageName)) {
            printError("Resource package name has already been set [%s], " +
                    "remove duplicate declaration [%s].", mResourcePackageName, packageName);
        }
    }

    protected String getPackageName() {
        if (!emptyOrNull(mResourcePackageName)) {
            return mResourcePackageName;
        } else {
            processingEnv.getMessager().printMessage(ERROR, "Resource package name is null. " +
                            "Make sure to add the [resourcePackageName] apt argument to your projects " +
                            "build.gradle if you wish to reference resources via name.");
            return null;
        }
    }

    protected String[] getViewIds(Element element) {
        // Assemble information on the field
        String type = BINDING_CLASS_TYPE_MAP.get(Bind.class);
        String[] ids = getValidResourceIds(element.getAnnotation(Bind.class).value());
        String[] resources = getValidResourceNames(element.getAnnotation(Bind.class).res(), type);
        if (ids.length <= 0 && resources.length <= 0) {
            printError(element, NONE_DECLARED_ERROR, Bind.class.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
        } else if (ids.length > 0 && resources.length > 0) {
            printError(element, BOTH_DECLARED_ERROR, Bind.class.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            return new String[]{};
        }
        // At this point, only one of the values can possible be set
        return ids.length > 0 ? ids : resources;
    }

    protected String[] getListenerIds(Element element, Class<? extends Annotation> annotationClass) {
        // Assemble information on the method.
        Annotation annotation = element.getAnnotation(annotationClass);
        String type = BINDING_CLASS_TYPE_MAP.get(annotationClass);
        if (type == null) {
            printError(element, UNRECOGNISED_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            return new String[]{};
        }
        try {
            Method annotationValue = annotationClass.getDeclaredMethod(VALUE);
            Method annotationResource = annotationClass.getDeclaredMethod(RESOURCE);
            if (annotationValue.getReturnType() != int[].class || annotationResource.getReturnType() != String[].class) {
                throw new IllegalStateException(
                        String.format(INVALID_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                                ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName()));
            }
            String[] ids = getValidResourceIds((int[]) annotationValue.invoke(annotation));
            String[] resources = getValidResourceNames((String[]) annotationResource.invoke(annotation), type);

            if (ids.length <= 0 && resources.length <= 0) {
                // No ID means we're declaring our own listener, ButterKnife expects this value
                resources = new String[]{NO_RESOURCE};
            } else if (ids.length > 0 && resources.length > 0) {
                printError(element, BOTH_DECLARED_ERROR, annotationClass,
                        ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
                return new String[]{};
            }
            // At this point, only one of the values can possible be set
            return ids.length > 0 ? ids : resources;
        } catch (Exception e) {
            printError(element, INVALID_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            return new String[]{};
        }
    }

    protected String getResourceId(Element element, Class<? extends Annotation> annotationClass) {
        Annotation annotation = element.getAnnotation(annotationClass);
        String type = BINDING_CLASS_TYPE_MAP.get(annotationClass);
        if (type == null) {
            printError(element, UNRECOGNISED_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            return NO_RESOURCE;
        }
        try {
            Method annotationValue = annotationClass.getDeclaredMethod(VALUE);
            Method annotationResource = annotationClass.getDeclaredMethod(RESOURCE);
            if (annotationValue.getReturnType() != int.class || annotationResource.getReturnType() != String.class) {
                throw new IllegalStateException(
                        String.format(INVALID_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                                ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName()));
            }
            int value = (Integer) annotationValue.invoke(annotation);
            String resource = (String) annotationResource.invoke(annotation);
            return getActualResourceId(element, value, resource, annotationClass, type);
        } catch (Exception e) {
            printError(element, INVALID_ANNOTATION_ERROR, annotationClass.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
            return NO_RESOURCE;
        }
    }

    protected String getActualResourceId(Element element, int value, String resource, Class annotation,
                                         String type) {
        if (value <= 0 && (resource == null || resource.length() <= 0)) {
            printError(element, NONE_DECLARED_ERROR, annotation.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
        } else if (value > 0 && resource != null && resource.length() > 0) {
            printError(element, BOTH_DECLARED_ERROR, annotation.getSimpleName(),
                    ((TypeElement) element.getEnclosingElement()).getQualifiedName(), element.getSimpleName());
        }
        // At this point, only one of the values can possible be set
        if (value > 0) {
            return String.valueOf(value);
        } else {
            return getPackageName() + "." + type + "." + resource;
        }
    }

    String[] getValidResourceIds(int[] ids) {
        List<String> validIds = new ArrayList<>();
        for (int i : ids) {
            if (i > 0) {
                validIds.add(String.valueOf(i));
            }
        }
        String[] tempIds = new String[validIds.size()];
        return validIds.toArray(tempIds);
    }

    String[] getValidResourceNames(String[] names, String type) {
        List<String> validIds = new ArrayList<>();
        for (String name : names) {
            if (name != null && name.length() > 0) {
                validIds.add(getPackageName() + "." + type + "." + name);
            }
        }
        String[] tempIds = new String[validIds.size()];
        return validIds.toArray(tempIds);
    }

    protected void printError(String message, Object... args) {
        printError(null, message, args);
    }

    protected void printError(Element element, String message, Object... args) {
        printMessage(element, message, ERROR, args);
    }

    protected void printMessage(Element element, String message, Kind kind, Object... args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        if (element != null) {
            processingEnv.getMessager().printMessage(kind, message, element);
        } else {
            processingEnv.getMessager().printMessage(kind, message);
        }
    }

    protected boolean emptyOrNull(String string) {
        return string == null || NULL.equals(string);
    }

}
