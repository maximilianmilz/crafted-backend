package de.crafted.api.service.ticket.model;

import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.image.model.Image;
import de.crafted.api.service.user.model.User;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TicketInfo {
    Ticket ticket;

    User user;

    List<Tag> tags;

    List<Image> images;
}
