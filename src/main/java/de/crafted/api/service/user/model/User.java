package de.crafted.api.service.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class User {
    @Schema(example = "20")
    long id;

    @Schema(example = "Julia")
    String username;

    @Schema(example = "Ausgebildete Elektrikerin")
    String description;

    @Schema(example = "aaaaaaaa-bbbb-cccc-dddd-eeeeeeeeeeee")
    String subject;

    @Schema(example = "true")
    Boolean verified;

    @Schema(example = "2021-04-23T18:30:45.882987+02:00")
    ZonedDateTime userCreateDate;

    @Schema(example = "2021-07-01T13:24:12.882335+02:00")
    ZonedDateTime userLastModifiedDate;

    @Schema(example = "3")
    Integer rating;

    Long profilePhotoId;
}
