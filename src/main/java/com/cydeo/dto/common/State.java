package com.cydeo.dto.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class State {

    @JsonProperty("state_name")
    private String stateName;


}
/*
{
        "state_name": "Alabama"
    },
 */