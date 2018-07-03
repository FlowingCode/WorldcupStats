package com.flowingcode.fixture.repository.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public final class Team {
    public final long id;
    public final String country;
    public final String fifa_code;
    public final long group_id;
    public final String group_letter;

    @JsonCreator
    public Team(@JsonProperty("id") final long id, @JsonProperty("country") final String country, @JsonProperty("fifa_code") final String fifa_code,
            @JsonProperty("group_id") final long group_id, @JsonProperty("group_letter") final String group_letter) {
        this.id = id;
        this.country = country;
        this.fifa_code = fifa_code;
        this.group_id = group_id;
        this.group_letter = group_letter;
    }
}