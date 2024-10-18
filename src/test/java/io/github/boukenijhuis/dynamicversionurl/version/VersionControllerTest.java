package io.github.boukenijhuis.dynamicversionurl.version;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GetVersionController.class)
class VersionControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testA() throws Exception {
        testEndpoint("/api/v1/a", "a1");
        testEndpoint("/api/v2/a", "a2");
        testEndpoint("/api/v3/a", "a2");
        testEndpoint("/api/v4/a", "a2");
        testEndpoint("/api/v5/a", "a2");
        testEndpoint("/api/v6/a", "a2");
        testEndpoint("/api/v7/a", "a2");
        testEndpoint("/api/v8/a", "a2");
        testEndpoint("/api/v9/a", "a2");
    }

    @Test
    public void testB() throws Exception {
        testEndpoint("/api/v1/b", "b1");
        testEndpoint("/api/v2/b", "b1");
        testEndpoint("/api/v3/b", "b1");
        testEndpoint("/api/v4/b", "b4");
        testEndpoint("/api/v5/b", "b4");
        testEndpoint("/api/v6/b", "b4");
        testEndpoint("/api/v7/b", "b7");
        testEndpoint("/api/v8/b", "b7");
        testEndpoint("/api/v9/b", "b7");

    }

    @Test
    public void testC() throws Exception {
        testEndpoint("/api/v1/c", "c1");
        testEndpoint("/api/v2/c", "c1");
        testEndpoint("/api/v3/c", "c1");
        testEndpoint("/api/v4/c", "c1");
        testEndpoint("/api/v5/c", "c1");
        testEndpoint("/api/v6/c", "c1");
        testEndpoint("/api/v7/c", "c1");
        testEndpoint("/api/v8/c", "c1");
        testEndpoint("/api/v9/c", "c1");
    }

    @Test
    public void testEndpointWithoutVersioning() throws Exception {
        mockMvc.perform(get("/api/d"))
                .andExpect(status().isOk())
                .andExpect(content().string("d"))
                .andReturn();
    }

    @Test
    public void testEndpointWithSingleVersion() throws Exception {
        mockMvc.perform(get("/api/v1/single-version"))
                .andExpect(status().isOk())
                .andExpect(content().string("single-version"))
                .andReturn();
    }

    private void testEndpoint(String path, String response) throws Exception {
        mockMvc.perform(get(path))
                .andExpect(status().isOk())
                .andExpect(content().string(response))
                .andReturn();
    }
}
