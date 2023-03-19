package com.betting.javawalletsystem.api;

import com.betting.javawalletsystem.dto.TransactionRequestDto;
import com.betting.javawalletsystem.dto.TransactionResponseDto;
import com.betting.javawalletsystem.service.AuthorisationService;
import com.betting.javawalletsystem.service.BetServiceImpl;;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BetController.class)
public class BetControllerTests {
    private static MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    @MockBean
    private BetServiceImpl betService;
    @MockBean
    private AuthorisationService authorisationService;

    @Autowired
    private static WebApplicationContext webApplicationContext;

    public BetControllerTests(@Autowired final MockMvc mockMvc,
                              @Autowired final ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

//    @BeforeClass
//    public static void setup(){
//        mockMvc = MockMvcBuilders
//                .webAppContextSetup(webApplicationContext)
//                .apply(springSecurity())
//                .build();
//    }

    @Test
    @WithMockUser(username = "lucky", password = "mypassword123")
    public void givenValidBetRequest_WhenPostBetIsCalled_ThenBetIsSubmitted() throws Exception {
        //given
        TransactionRequestDto request = TransactionRequestDto.builder()
                .amount(new BigDecimal(50))
                .playerId(1l)
                .transactionId(1l)
                .build();

        TransactionResponseDto response = TransactionResponseDto.builder()
                .cashBalance(new BigDecimal(50))
                .bonusBalance(BigDecimal.ZERO)
                .playerId(1l)
                .transactionId(1l)
                .build();

        //when
        doNothing().when(authorisationService).ensurePlayerAuthorised(1l);
        when(betService.processBet(request)).thenReturn(response);

        //then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/bet")
                        .with(user("lucky"))
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(objectMapper.writeValueAsString(response)));
    }
}
