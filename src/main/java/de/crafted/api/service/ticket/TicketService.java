package de.crafted.api.service.ticket;

import de.crafted.api.controller.execption.ForbiddenRequestException;
import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.controller.model.TicketInput;
import de.crafted.api.service.common.mapper.TagMapper;
import de.crafted.api.service.ticket.jooq.enums.Status;
import de.crafted.api.service.ticket.jooq.tables.records.TicketRecord;
import de.crafted.api.service.ticket.mapper.TicketMapper;
import de.crafted.api.service.ticket.model.Ticket;
import de.crafted.api.service.ticket.model.TicketInfo;
import de.crafted.api.service.ticket.repository.TicketRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.lang.module.ResolutionException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository repository;

    public List<Ticket> findAll() {
        return repository.findAll().stream()
                .map(TicketMapper::map)
                .toList();
    }

    private Optional<Ticket> findById(long id) {
        return repository.findById(id)
                .map(TicketMapper::map);
    }

    public TicketInfo getTicketInfo(long id) {
        var ticket = findById(id)
                .orElseThrow(ResolutionException::new);

        return getTicketInfo(ticket);
    }

    public List<TicketInfo> getTicketInfos() {
        return findAll().stream()
                .map(this::getTicketInfo)
                .toList();
    }

    public List<TicketInfo> findByUserId(long userId) {
        return repository.findByUserId(userId).stream()
                .map(TicketMapper::map)
                .map(this::getTicketInfo)
                .toList();
    }

    public List<TicketInfo> findByAssignedTo(long assignedTo) {
        return repository.findByAssignedTo(assignedTo).stream()
                .map(TicketMapper::map)
                .map(this::getTicketInfo)
                .toList();
    }

    private TicketInfo getTicketInfo(Ticket ticket) {
        var tags = repository.findTagsByTicketId(ticket.getId()).stream()
                .map(entry -> TagMapper.map(entry.getTag()))
                .toList();

        return TicketInfo.builder()
                .ticket(ticket)
                .tags(tags)
                .build();
    }

    public TicketInfo create(long userId, TicketInput input) {
        TicketRecord record = new TicketRecord();

        record.setUserId(userId);
        record.setTitle(input.getTitle());
        record.setDescription(input.getDescription());
        record.setCreated(LocalDateTime.now());
        record.setStatus(Status.open);

        var ticket = repository.create(record);

        input.getTags().forEach(entry -> repository.createTag(ticket.getId(), TagMapper.map(entry)));

        return getTicketInfo(TicketMapper.map(ticket));
    }

    public TicketInfo update(long userId, Long ticketId, TicketInput ticketInput) {
        if (!findById(ticketId).orElseThrow(ResourceNotFoundException::new).getUserId().equals(userId)) {
            throw new ForbiddenRequestException();
        }

        var ticket = repository.update(ticketId, ticketInput.getTitle(), ticketInput.getDescription())
                .orElseThrow(ResourceNotFoundException::new);

        var tags = ticketInput.getTags().stream()
                .map(TagMapper::map)
                .toList();

        repository.createTags(ticketId, tags);

        return getTicketInfo(ticketId);
    }
}
