/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package hello;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.hamcrest.CoreMatchers;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.TestMethodOrder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EmbarcadorTests {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void testTable(){
        Embarcador embarcador = new Embarcador();
        embarcador.tableExists();
    }

    @Test
    @Order(1)
    public void noParamEmbarcadoresShouldReturnPostMessage() throws Exception {
        this.mockMvc.perform(post("/embarcadores"))
            .andDo(print())
            .andExpect(status()
                .isOk())
            .andExpect(content()
                .string(CoreMatchers.containsString("{create}")
                )
            );
    }

    @Test
    @Order(2)
    public void noParamEmbarcadoresShouldReturnGetMessage() throws Exception {
        this.mockMvc.perform(get("/embarcadores"))
            .andDo(print()).andExpect(
            status()
                .isOk())
            .andExpect(content()
                .string(CoreMatchers.containsString("{read}")
                )
            );
    }

    @Test
    @Order(3)
    public void noParamEmbarcadoresShouldReturnPutMessage() throws Exception {
        this.mockMvc.perform(put("/embarcadores"))
            .andDo(print())
            .andExpect(status()
                .isOk())
            .andExpect(content()
                .string(CoreMatchers.containsString("{update}")));
    }

    @Test
    @Order(4)
    public void noParamEmbarcadoresShouldReturnDeleteMessage() throws Exception {
        this.mockMvc.perform(delete("/embarcadores"))
            .andDo(print())
            .andExpect(status()
                .isOk())
            .andExpect(content()
                .string(CoreMatchers.containsString("{delete}")));
    }

}
