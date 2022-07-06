package de.crafted.api.service.ticket.mapper;

import de.crafted.api.service.ticket.jooq.enums.Status;

public class StatusMapper {
    public StatusMapper() {
        throw new IllegalStateException("StatusMapper class");
    }

    public static Status map(de.crafted.api.service.ticket.model.Status status) {
        return switch (status) {
            case OPEN -> Status.open;
            case ASSIGNED -> Status.assign;
            case DONE -> Status.done;
        };
    }

    public static de.crafted.api.service.ticket.model.Status map(Status status) {
        return switch (status) {
            case open -> de.crafted.api.service.ticket.model.Status.OPEN;
            case assign -> de.crafted.api.service.ticket.model.Status.ASSIGNED;
            case done -> de.crafted.api.service.ticket.model.Status.DONE;
        };
    }
}
