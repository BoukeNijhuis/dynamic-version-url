package io.github.boukenijhuis.dynamicversionurl.readme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/***
 * This class is used to test the code samples in the README.md.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ReadMeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void testEndpointWithoutVersioning() throws Exception {
        mockMvc.perform(get("/v1/a"))
                .andExpect(status().isOk());
    }

}
