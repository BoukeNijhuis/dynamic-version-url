package nl.boukenijhuis.dynamicversionurl;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ApiVersionRequestMappingHandlerMapping extends RequestMappingHandlerMapping {

    private String prefix;

    public ApiVersionRequestMappingHandlerMapping(String prefix) {
        this.prefix = prefix;
    }

    @Override
    @Nullable
    protected RequestMappingInfo getMappingForMethod(Method method, Class<?> handlerType) {

        RequestMappingInfo info = super.getMappingForMethod(method, handlerType);

        Annotation vehicleAnnotation = AnnotationUtils.findAnnotation (method, GetVersionMapping.class);



        // update the paths when necessary
        if (method.isAnnotationPresent(GetVersionMapping.class)) {
            GetVersionMapping annotation = method.getAnnotation(GetVersionMapping.class);
            String[] versionPaths = updatePaths(info.getPatternValues(), annotation);
            info = info.mutate().paths(versionPaths).build();
        }
        return info;

    }

    private String[] updatePaths(Set<String> patternValues, GetVersionMapping annotation) {

        // get versions
        int[] versions = annotation.versions();

        List<String> versionedPaths = new ArrayList<>();

        // TODO: solvable by with streams?
        // for every version
        for (int v = versions[0]; v <= versions[1]; v++) {
            // for every path (in the annotation)
            for (String annotationPath : annotation.path())
                // for every pattern value (from the existing request mapping info)
                for (String patternValue : patternValues) {
                    String replacement = "/" + prefix + v + annotationPath;
                    String versionedPath = patternValue.replaceFirst(annotationPath + "$", replacement);
                    versionedPaths.add(versionedPath);
                }
        }

        // return the array with versioned paths
        return versionedPaths.toArray(new String[versionedPaths.size()]);

    }
}
