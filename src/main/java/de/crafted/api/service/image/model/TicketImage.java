package de.crafted.api.service.image.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TicketImage {
    long ticketId;

    long imageId;
}
