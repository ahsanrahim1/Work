package com.ubs.wmap.eisl.housekeeping.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ubs.wmap.eisl.housekeeping.TokenService;
import com.ubs.wmap.eisl.housekeeping.TokenServiceConfiguration;
import com.ubs.wmap.eisl.housekeeping.component.exceptions.TokenExpireException;
import com.ubs.wmap.eisl.housekeeping.exception.BadRequestException;
import com.ubs.wmap.eisl.housekeeping.exception.InvalidEISLTokenException;
import java.util.HashMap;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.mockito.ArgumentMatcher;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Slf4j
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@EnableAutoConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Import(TokenServiceConfiguration.class)
public class HouseKeepingSecurityControllerTest {

    @MockBean
    private TokenService tokenService;
  
    @Autowired
    HouseKeepingSecurityController houseKeepingSecurityController;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Value("${app.message.INTERNAL_SERVER_ERROR_MSG}")
    private String INTERNAL_SERVER_ERROR_MSG;

    @Value("${service.testingEndPointUnwrap}")
    private String UNWRAP_END_POINT;



    @Test
    public void uwrapEisl() throws Exception{
          String testEisl = "eisl";
          Map<String,Object> claims = new HashMap<>();
          claims.put("claims", "claims");
          Mockito.when(tokenService.init(ArgumentMatchers.anyString())).thenReturn(claims);
          assertEquals("success",claims,houseKeepingSecurityController.unWrapEislToken(testEisl).getBody()); 
    }
    
    @Test(expected = BadRequestException.class)
    public void uwrapEislEmptyParam() throws Exception{     
         houseKeepingSecurityController.unWrapEislToken(""); 
    }
    
    @Test
    public void servicePreConditionTest() throws InvalidEISLTokenException, BadRequestException{
        String testEisl = "eisl";
        Map<String, Object> testClaims =  new HashMap<>();
        testClaims.put("claims", "claims");
        Mockito.when(tokenService.init(ArgumentMatchers.anyString())).thenReturn(testClaims);
        assertEquals("success",testClaims,houseKeepingSecurityController.servicePreconditions(testEisl));
    }
    
    @Test(expected = InvalidEISLTokenException.class)
    public void servicePreConditionInvalidEislTest() throws InvalidEISLTokenException, BadRequestException{
        String testEisl = "eisl";
        Mockito.when(tokenService.init(ArgumentMatchers.anyString())).thenThrow(TokenExpireException.class);
        houseKeepingSecurityController.servicePreconditions(testEisl);
    }
    
     @Test(expected = BadRequestException.class)
    public void servicePreConditionBadReqTest() throws InvalidEISLTokenException, BadRequestException{
        String testEisl = "eisl";
        Mockito.when(tokenService.init(ArgumentMatchers.anyString())).thenThrow(RuntimeException.class);
        houseKeepingSecurityController.servicePreconditions(testEisl);
    }
}