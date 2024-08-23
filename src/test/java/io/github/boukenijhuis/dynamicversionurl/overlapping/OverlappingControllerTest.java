package io.github.boukenijhuis.dynamicversionurl.overlapping;

import io.github.boukenijhuis.dynamicversionurl.TestApplication;
import io.github.boukenijhuis.dynamicversionurl.WebMvcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OverlappingControllerTest {

    // this test is a little flaky
    @Test
    public void testEndpointWithOverlappingVersioning() {

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Class<?>[] classes = {TestApplication.class, WebMvcConfig.class, OverlappingController.class};
            ApplicationContext app = SpringApplication.run(classes, new String[]{});
        });

        assertEquals("Ambiguous version mapping found with the following URL: /v3/a", exception.getMessage());
    }
}
