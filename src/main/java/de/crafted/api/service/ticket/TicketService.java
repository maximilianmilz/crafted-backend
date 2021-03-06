package de.crafted.api.service.ticket;

import de.crafted.api.controller.execption.ForbiddenRequestException;
import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.controller.model.TicketInput;
import de.crafted.api.service.common.mapper.TagMapper;
import de.crafted.api.service.common.model.Order;
import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.image.ImageService;
import de.crafted.api.service.ticket.jooq.enums.Status;
import de.crafted.api.service.ticket.jooq.tables.records.TicketRecord;
import de.crafted.api.service.ticket.mapper.StatusMapper;
import de.crafted.api.service.ticket.mapper.TicketMapper;
import de.crafted.api.service.ticket.model.Ticket;
import de.crafted.api.service.ticket.model.TicketInfo;
import de.crafted.api.service.ticket.repository.TicketRepository;
import de.crafted.api.service.user.mapper.UserMapper;
import de.crafted.api.service.user.repository.UserRepository;
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
    private final UserRepository userRepository;
    private final ImageService imageService;

    public List<Ticket> findAll(Optional<String> searchTerm,
                                Optional<String> userName,
                                Optional<List<de.crafted.api.service.common.jooq.enums.Tag>> tags,
                                Optional<Boolean> verified,
                                Optional<Status> status,
                                Optional<Order> createdOrder) {
        return repository.findAll(searchTerm, userName, tags, verified, status, createdOrder).stream()
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

    public List<TicketInfo> getTicketInfos(Optional<String> searchTerm,
                                           Optional<String> userName,
                                           Optional<List<Tag>> tags,
                                           Optional<Boolean> verified,
                                           Optional<de.crafted.api.service.ticket.model.Status> status,
                                           Optional<Order> createdOrder) {
        Optional<List<de.crafted.api.service.common.jooq.enums.Tag>> mappedTags = Optional.empty();
        Optional<Status> mappedStatus = Optional.empty();

        if (tags.isPresent()) {
            mappedTags = Optional.of(tags.get().stream()
                    .map(TagMapper::map)
                    .toList());
        }

        if (status.isPresent()) {
            mappedStatus = Optional.of(StatusMapper.map(status.get()));
        }


        return findAll(searchTerm, userName, mappedTags, verified, mappedStatus, createdOrder).stream()
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

        var user = ticket.getUserId() != null ? userRepository.findById(ticket.getUserId()).map(UserMapper::map)
                .orElseThrow(ResourceNotFoundException::new) : null;

        var images = imageService.findByTicketId(ticket.getId());

        return TicketInfo.builder()
                .ticket(ticket)
                .user(user)
                .tags(tags)
                .images(images)
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

        input.getImages().forEach(entry -> {
            var image = imageService.create(entry.getUrl(), entry.getAltText());
            imageService.createTicketImage(ticket.getId(), image.getId());
        });


        input.getTags().forEach(entry -> repository.createTag(ticket.getId(), TagMapper.map(entry)));

        return getTicketInfo(TicketMapper.map(ticket));
    }

    public TicketInfo update(long userId, long ticketId, TicketInput ticketInput) {
        if (!findById(ticketId).orElseThrow(ResourceNotFoundException::new).getUserId().equals(userId)) {
            throw new ForbiddenRequestException();
        }

        repository.update(ticketId, ticketInput.getTitle(), ticketInput.getDescription())
                .orElseThrow(ResourceNotFoundException::new);

        var tags = ticketInput.getTags().stream()
                .map(TagMapper::map)
                .toList();

        repository.createTags(ticketId, tags);

        return getTicketInfo(ticketId);
    }

    public TicketInfo markAsAssigned(long ticketId, long userId) {
        var ticket = findById(ticketId)
                .orElseThrow(ResourceNotFoundException::new);

        if (ticket.getUserId().equals(userId)) {
            throw new ForbiddenRequestException();
        }

        var patchedTicket = repository.markAsAssigned(ticketId, userId)
                .map(TicketMapper::map)
                .orElseThrow(ResourceNotFoundException::new);

        return getTicketInfo(patchedTicket);
    }

    public TicketInfo markAsDone(long ticketId, long userId) {
        var ticket = findById(ticketId)
                .orElseThrow(ResourceNotFoundException::new);

        if (!ticket.getUserId().equals(userId)) {
            throw new ForbiddenRequestException();
        }

        var patchedTicket = repository.markAsDone(ticketId)
                .map(TicketMapper::map)
                .orElseThrow(ResourceNotFoundException::new);

        return getTicketInfo(patchedTicket);
    }
}
