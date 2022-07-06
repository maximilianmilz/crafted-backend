package de.crafted.api.controller.model;

import de.crafted.api.service.common.model.Tag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserProfileInput {
    @Schema(example = "Ich bin ein verifizierter Elektriker aus Brandenburg an der Havel.")
    String description;

    @Schema(example = "https://www.example.com/image.png/")
    String imageUrl;

    List<Tag> tags;
}
