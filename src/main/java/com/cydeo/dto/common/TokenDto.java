package com.cydeo.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TokenDto {
    @JsonProperty("auth_token")
    private String authToken;
}
