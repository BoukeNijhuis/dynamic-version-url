package io.github.boukenijhuis.dynamicversionurl.pathvariable;

import io.github.boukenijhuis.dynamicversionurl.WebMvcConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {WebMvcConfig.class, PathVariableController.class})
@AutoConfigureMockMvc
class PathVariableControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testEndpointWithPathVariable() throws Exception {
        mockMvc.perform(get("/v2/pathVariable/aap"))
                .andExpect(status().isOk())
                .andExpect(content().string("aap"));
    }
}
