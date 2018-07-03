package com.flowingcode.fixture.repository;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class SFGRestTemplate extends RestTemplate {

    public SFGRestTemplate() {
        getMessageConverters().stream().filter(e -> e instanceof MappingJackson2HttpMessageConverter).findFirst()
                .ifPresent(e -> configureObjectMapper(((MappingJackson2HttpMessageConverter) e).getObjectMapper()));
    }

    private void configureObjectMapper(final ObjectMapper objectMapper) {
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }


}
