package de.crafted.api.service.ticket.mapper;

import de.crafted.api.service.ticket.jooq.tables.records.TicketRecord;
import de.crafted.api.service.ticket.model.Ticket;

import java.time.ZoneId;

public class TicketMapper {
    public TicketMapper() {
        throw new IllegalStateException("TicketMapper class");
    }

    public static Ticket map(TicketRecord record) {
        return Ticket.builder()
                .id(record.getId())
                .title(record.getTitle())
                .description(record.getDescription())
                .status(record.getStatus() != null ? StatusMapper.map(record.getStatus()) : null)
                .createdDate(record.getCreated().atZone(ZoneId.systemDefault()))
                .userId(record.getUserId())
                .assignedTo(record.getAssignedTo())
                .build();
    }

}
