package io.github.boukenijhuis.dynamicversionurl.versionmapping;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({VersionMappingController.class})
class VersionMappingControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testEndpointWithVersionMappingAnnotation() throws Exception {
        mockMvc.perform(get("/v1/version_mapping"))
                .andExpect(status().isNotFound());

        mockMvc.perform(get("/v2/version_mapping"))
                .andExpect(status().isOk())
                .andExpect(content().string("a2"));

        mockMvc.perform(get("/v3/version_mapping"))
                .andExpect(status().isNotFound());
    }
}
