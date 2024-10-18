package io.github.boukenijhuis.dynamicversionurl.pathvariable;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest({PathVariableController.class})
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
