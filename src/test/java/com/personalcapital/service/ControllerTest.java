package com.personalcapital.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {ApplicationController.class })
@WebAppConfiguration
public class ControllerTest {
    private MockMvc mockMvc;

    @InjectMocks
    private ApplicationController applicationController;

    @Before
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(applicationController).build();
    }

    @Test
    public void testSuccessful() throws Exception {

        mockMvc.perform(
                get("/planname?planName=PARKER CORNEA, INC. DB(K) PLAN")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andExpect(status().is2xxSuccessful());
    }

    @Test(expected = NestedServletException.class)
    public void testEmpty() throws Exception {

        mockMvc.perform(
                get("/sponsorname?sponsorName=")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testNullResult() throws Exception {

        MvcResult result = mockMvc.perform(
                get("/sponsorstate?sponsorState=NI")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)).andReturn();

        assert result.getResponse().getContentLength() == 0;

    }

}