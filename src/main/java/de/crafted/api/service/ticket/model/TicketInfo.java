package de.crafted.api.service.ticket.model;

import de.crafted.api.service.common.model.Tag;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class TicketInfo {
    Ticket ticket;

    List<Tag> tags;
}
