package de.crafted.api.service.user.model;

import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.ticket.model.Ticket;
import lombok.Builder;
import lombok.Value;

import java.time.ZonedDateTime;
import java.util.List;

@Value
@Builder
public class UserProfile {
    Long id;
    String username;
    Boolean verified;
    ZonedDateTime createdDate;
    List<Ticket> ownTickets;
    List<Ticket> assignedTickets;
    List<Tag> tags;
    String imageUrl;
}
