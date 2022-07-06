package de.crafted.api.controller.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class ImageInput {
    String url;

    String altText;
}
