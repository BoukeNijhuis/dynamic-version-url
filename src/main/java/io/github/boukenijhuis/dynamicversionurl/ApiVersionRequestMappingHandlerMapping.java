package io.github.boukenijhuis.dynamicversionurl;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private final String prefix;

    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    @Override
    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        VersionMapping versionMapping = AnnotationUtils.findAnnotation(method, VersionMapping.class);

        if (versionMapping != null) {
            int[] versions = null;
            String[] paths = null;

            // ugly if statement, because annotations do not use inheritance
            if (versionMapping.value().equals(GetVersionMapping.class)) {
                GetVersionMapping annotation = method.getAnnotation(GetVersionMapping.class);
                // TODO introduce generic class that holds method, versions, paths and send this to updatePaths
                versions = annotation.versions();
                paths = returnValueOrPath(annotation.value(), annotation.path());
            } else if (versionMapping.value().equals(PostVersionMapping.class)) {
                PostVersionMapping annotation = method.getAnnotation(PostVersionMapping.class);
                versions = annotation.versions();
                paths = returnValueOrPath(annotation.value(), annotation.path());
            } else if (versionMapping.value().equals(PutVersionMapping.class)) {
                PutVersionMapping annotation = method.getAnnotation(PutVersionMapping.class);
                versions = annotation.versions();
                paths = returnValueOrPath(annotation.value(), annotation.path());
            } else if (versionMapping.value().equals(DeleteVersionMapping.class)) {
                DeleteVersionMapping annotation = method.getAnnotation(DeleteVersionMapping.class);
                versions = annotation.versions();
                paths = returnValueOrPath(annotation.value(), annotation.path());
            } else if (versionMapping.value().equals(PatchVersionMapping.class)) {
                PatchVersionMapping annotation = method.getAnnotation(PatchVersionMapping.class);
                versions = annotation.versions();
                paths = returnValueOrPath(annotation.value(), annotation.path());
            }

            if (info != null && versions != null) {
                String[] versionPaths = updatePaths(info.getPatternValues(), versions, paths);
                // update the request mapping info
                info = info.mutate().paths(versionPaths).build();
            }
        }

        // always return info
        return info;
    }

    private String[] returnValueOrPath(String[] value, String[] path) {
        if (path != null && path.length > 0) {
            return path;
        } else return value;
    }

    private String[] updatePaths(Set<String> patternValues, @NonNull int[] versions, @NonNull String[] paths) {

        List<String> versionedPaths = new ArrayList<>();

        int oldestVersion = versions[0];
        int newestVersion;

        // only one version specified
        if (versions.length == 1) {
            newestVersion = versions[0];
        }
        // multiple versions specified
        else if (versions.length == 2){
            newestVersion = versions[1];
        }
        // too many versions specified
        else {
            // TODO add the request method
            String message = String.format("Too many versions (%s) specified on %s.", Arrays.toString(versions), Arrays.toString(paths));
            throw new RuntimeException(message);
        }

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
}
