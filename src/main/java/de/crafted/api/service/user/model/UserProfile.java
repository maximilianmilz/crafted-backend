package de.crafted.api.service.user.model;

import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.image.model.Image;
import de.crafted.api.service.ticket.model.TicketInfo;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class UserProfile {
    User user;

    List<TicketInfo> tickets;

    List<TicketInfo> projects;

    List<Tag> tags;

    Image profilePhoto;
}
