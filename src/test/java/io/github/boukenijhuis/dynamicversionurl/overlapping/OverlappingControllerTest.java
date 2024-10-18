package io.github.boukenijhuis.dynamicversionurl.overlapping;

import io.github.boukenijhuis.dynamicversionurl.TestApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OverlappingControllerTest {

    @Test
    public void testEndpointWithOverlappingVersioning() {

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            Class<?>[] classes = {TestApplication.class, OverlappingController.class};
            SpringApplication.run(classes, new String[]{});
        });

        assertEquals("Ambiguous version mapping found with the following URL: /v3/overlap", exception.getMessage());
    }
}
