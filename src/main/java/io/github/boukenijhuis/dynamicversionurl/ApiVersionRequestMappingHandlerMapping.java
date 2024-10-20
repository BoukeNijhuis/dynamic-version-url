package io.github.boukenijhuis.dynamicversionurl;

import io.github.boukenijhuis.dynamicversionurl.annotation.VersionMapping;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final String prefix;

    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    /***
     * Creates an updated request mapping info object. This used to map an URL to a Java method. It is updated in
     * such a way that it supports specified version numbers.
     * @param method the method that will be mapped to
     * @param handlerType the class where the method is found
     * @return the updated request mapping info object
     */
    @Override
    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        // get the exiting request mapping info
        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        // find the first version mapping annotation
        Annotation annotation = getFirstAnnotation(method, getClass().getPackageName());

        if (annotation != null) {
            AnnotationValues annotationValues = getAnnotationValues(method, annotation);

            if (info != null && annotationValues.versions() != null) {
                String[] versionPaths = updatePaths(info.getPatternValues(), annotationValues, annotation.getClass());
                // update the request mapping info
                info = info.mutate().paths(versionPaths).build();
            }
        }

        // always return info
        return info;
    }

    /**
     * Build an AnnotationValues object containing values for versions, value and path.
     * @param method
     * @param annotation
     * @return
     */
    private AnnotationValues getAnnotationValues(Method method, Annotation annotation) {

        Map<String, Object> versionAttributes = AnnotationUtils.getAnnotationAttributes(annotation);

        // special case: VersionMapping
        if (annotation.annotationType().equals(VersionMapping.class)) {
            Annotation mappingAnnotation = getFirstAnnotation(method, GetMapping.class.getPackageName());
            Map<String, Object> mappingAttributes = AnnotationUtils.getAnnotationAttributes(mappingAnnotation);

            return AnnotationValues.of(
                    versionAttributes.get("value"),
                    mappingAttributes.get("value"),
                    mappingAttributes.get("path")
            );
        } else {
            return AnnotationValues.of(
                    versionAttributes.get("versions"),
                    versionAttributes.get("value"),
                    versionAttributes.get("path")
            );
        }
    }

    /**
     * Find the first version annotation whose package name start with a given string on a given method.
     *
     * @param method     the method that is checked for the first version annotation
     * @param startsWith the package name of the annotation should start with
     * @return the first version annotation or null (when not found)
     */
    private Annotation getFirstAnnotation(Method method, String startsWith) {
        Annotation[] declaredAnnotations = method.getDeclaredAnnotations();
        for (Annotation annotation : declaredAnnotations) {
            String packageName = annotation.annotationType().getPackageName();
            if (packageName.startsWith(startsWith)) {
                return annotation;
            }
        }

        // no version annotation found
        return null;
    }

    /***
     * Calculates the dynamic URLs based upon exiting patternValues, versions and paths.
     * @param patternValues the existing patternValues calculated by Spring Boot.
     * @param annotationValues the attribute values from the annotation
     * @param clazz the annotation where version and path were specified
     * @return the updated patternValues
     */
    private String[] updatePaths(Set<String> patternValues, AnnotationValues annotationValues, Class<? extends Annotation> clazz) {

        int[] versions = annotationValues.versions();
        String[] paths = annotationValues.path();

        List<String> versionedPaths = new ArrayList<>();

        int oldestVersion = versions[0];
        int newestVersion = getNewestVersion(versions, paths, clazz);

        // for every version
        for (int version = oldestVersion; version <= newestVersion; version++) {
            // for every path (in the annotation)
            for (String annotationPath : paths)
                // for every pattern value (from the existing request mapping info)
                for (String patternValue : patternValues) {
                    // TODO add a way to postfix the version number (default to prefix)
                    // escape accolades from path variables
                    // TODO should we escape more special characters?
                    String escapedAnnotationPath = annotationPath.replace("{", "\\{").replace("}", "\\}");
                    String replacement = "/" + prefix + version + annotationPath;
                    String versionedPath = patternValue.replaceFirst(escapedAnnotationPath + "$", replacement);
                    versionedPaths.add(versionedPath);
                }
        }

        // TODO check for overlapping versions!

        // return the array with versioned paths
        return versionedPaths.toArray(new String[0]);

    }

    /***
     * Get the newest version. This depends on the number of versions. Throws an exception when there are more
     * than two versions.
     * @param versions the versions from the annotation
     * @param path the path from the annotation
     * @param clazz the class of the annotation
     * @return the newest version
     */
    private static int getNewestVersion(int[] versions, String[] path, Class<? extends Annotation> clazz) {
        int newestVersion;
        // only one version specified
        if (versions.length == 1) {
            newestVersion = versions[0];
        }
        // multiple versions specified
        else if (versions.length == 2) {
            newestVersion = versions[1];
        }
        // too many versions specified
        else {
            String message = String.format("Too many versions (%s) specified on @%s with path %s.",
                    Arrays.toString(versions), clazz.toString(), Arrays.toString(path));
            throw new RuntimeException(message);
        }
        return newestVersion;
    }
}
