package io.github.boukenijhuis.dynamicversionurl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.pattern.PathPattern;

import java.util.HashSet;
import java.util.Set;

@Component
public class VersionOverlapChecker implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Set<RequestMappingInfo> requestMappings = applicationContext.getBean(RequestMappingHandlerMapping.class).getHandlerMethods().keySet();
        var patterns = new HashSet<String>();

        // get all patterns
        for (RequestMappingInfo requestMapping : requestMappings) {
            if (requestMapping.getPathPatternsCondition() != null) {
                for (PathPattern pathPattern : requestMapping.getPathPatternsCondition().getPatterns()) {
                    findDuplicate(pathPattern, patterns);
                }
            }
        }
    }

    private static void findDuplicate(PathPattern pathPattern, HashSet<String> patterns) {
        // sometimes /error comes in more than once, but we are not interested in that url
        if (doesNotEqualError(pathPattern)) {
            boolean success = patterns.add(pathPattern.getPatternString());
            if (!success) {
                // duplicate found
                String message = String.format("Ambiguous version mapping found with the following URL: %s", pathPattern);
                throw new RuntimeException(message);
            }
        }
    }

    private static boolean doesNotEqualError(PathPattern pathPattern) {
        return !pathPattern.getPatternString().equals("/error");
    }
}
