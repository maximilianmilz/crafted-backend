package de.crafted.api.controller.model;

import de.crafted.api.service.common.model.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TicketInput {
    @Schema(example = "Tisch aufbauen.")
    String title;

    @Schema(example = "Mein neuer IKEA Tisch muss aufgebaut werden. Leider besitze ich dafür kein Werkzeug.")
    String description;

    List<Tag> tags;
}
