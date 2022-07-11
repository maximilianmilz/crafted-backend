package de.crafted.api.controller.model;

import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.image.model.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserProfileInput {
    @Schema(example = "Ich bin ein verifizierter Elektriker aus Brandenburg an der Havel.")
    String description;

    List<Tag> tags;

    ImageInput image;
}
