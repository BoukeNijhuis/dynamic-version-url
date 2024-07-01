package io.github.boukenijhuis.dynamicversionurl;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

        // find the version mapping annotation
        VersionMapping versionMapping = AnnotationUtils.findAnnotation(method, VersionMapping.class);

        if (versionMapping != null) {

            Annotation annotation = method.getAnnotation(versionMapping.value());
            Map<String, Object> annotationAttributes = AnnotationUtils.getAnnotationAttributes(annotation);
            int[] versions = (int[]) annotationAttributes.get("versions");
            String[] value = (String[]) annotationAttributes.get("value");
            String[] path = (String[]) annotationAttributes.get("path");
            path = returnValueOrPath(value, path);

            if (info != null && versions != null) {
                String[] versionPaths = updatePaths(info.getPatternValues(), versions, path, versionMapping.value());
                // update the request mapping info
                info = info.mutate().paths(versionPaths).build();
            }
        }

        // always return info
        return info;
    }

    /***
     * Returns the path array if it contains values, otherwise it will return the value array.
     * @param value the value array (from a RequestMapping)
     * @param path the path array (from a RequestMapping)
     * @return the path array if it contains values, otherwise the value array
     */
    private String[] returnValueOrPath(String[] value, String[] path) {
        if (path != null && path.length > 0) {
            return path;
        } else return value;
    }

    /***
     * Calculates the dynamic URLs based upon exiting patternValues, versions and paths.
     * @param patternValues the existing patternValues calculated by Spring Boot.
     * @param versions the versions (as specified by the annotation)
     * @param paths the paths (as specified by the annotation)
     * @param clazz the annotation where version and path were specified
     * @return the updated patternValues
     */
    private String[] updatePaths(Set<String> patternValues, @NonNull int[] versions, @NonNull String[] paths, Class<? extends Annotation> clazz) {

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
                    String replacement = "/" + prefix + version + annotationPath;
                    String versionedPath = patternValue.replaceFirst(annotationPath + "$", replacement);
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
