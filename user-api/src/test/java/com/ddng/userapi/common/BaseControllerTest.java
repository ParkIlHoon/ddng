package com.ddng.userapi.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Disabled;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@Disabled
public class BaseControllerTest
{
    /**
     * 테스트용 요청 처리를 위한 mocking
     */
    @Autowired
    protected MockMvc mockMvc;

    /**
     * client -> server 매퍼 처리
     */
    @Autowired
    protected ObjectMapper objectMapper;

    /**
     * DTO -> Entity 매퍼 처리
     */
    @Autowired
    ModelMapper modelMapper;
}
