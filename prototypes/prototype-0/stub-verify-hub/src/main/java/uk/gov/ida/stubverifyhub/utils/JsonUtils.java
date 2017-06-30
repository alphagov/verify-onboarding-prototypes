package uk.gov.ida.stubverifyhub.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JsonUtils {

    public final static ObjectMapper objectMapper = new ObjectMapper() {{
        registerModule(new JavaTimeModule());
        registerModule(new Jdk8Module());
    }};

}
