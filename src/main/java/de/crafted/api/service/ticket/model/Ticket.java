package de.crafted.api.service.ticket.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;

@Value
@Builder
public class Ticket {
    @Schema(example = "20")
    long id;

    @Schema(example = "Tisch aufbauen")
    String title;

    @Schema(example = "Mein neu gekaufter IKEA Tisch muss aufgebaut werden. Leider besitze ich dafür nicht das nötige Werkzeug.")
    String description;

    @Schema(example = "OPEN")
    Status status;

    @Schema(example = "2021-04-23T18:30:45.882987+02:00")
    ZonedDateTime createdDate;

    @Schema(example = "1")
    Long userId;

    @Schema(example = "")
    Long assignedTo;
}
