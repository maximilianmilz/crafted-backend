package de.crafted.api.service.ticket.repository;

import static de.crafted.api.service.ticket.jooq.tables.Ticket.TICKET;
import static de.crafted.api.service.user.jooq.tables.User.USER;
import static de.crafted.api.service.common.jooq.tables.TicketTag.TICKET_TAG;

import de.crafted.api.service.common.jooq.enums.Tag;
import de.crafted.api.service.common.jooq.tables.records.TicketTagRecord;
import de.crafted.api.service.ticket.jooq.enums.Status;
import de.crafted.api.service.ticket.jooq.tables.records.TicketRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TicketRepository {
    private final DSLContext context;

    public List<TicketRecord> findAll(Optional<String> searchTerm,
                                      Optional<String> userName,
                                      Optional<List<Tag>> tags,
                                      Optional<Boolean> verified,
                                      Optional<Status> status) {
        Condition condition = createFilterCondition(searchTerm, userName, tags, verified, status);

        return context.select().from(TICKET)
                .leftJoin(USER).on(TICKET.USER_ID.eq(USER.ID))
                .leftJoin(TICKET_TAG).on(TICKET.ID.eq(TICKET_TAG.TICKET_ID))
                .where(condition)
                .orderBy(TICKET.ID.asc())
                .fetchInto(TICKET);
    }

    public Optional<TicketRecord> findById(long id) {
        return context.selectFrom(TICKET)
                .where(TICKET.ID.eq(id))
                .fetchOptional();
    }

    public List<TicketTagRecord> findTagsByTicketId(long ticketId) {
        return context.selectFrom(TICKET_TAG)
                .where(TICKET_TAG.TICKET_ID.eq(ticketId))
                .fetch();
    }

    public List<TicketRecord> findByUserId(long userId) {
        return context.selectFrom(TICKET)
                .where(TICKET.USER_ID.eq(userId))
                .fetch();
    }

    public List<TicketRecord> findByAssignedTo(long assignedTo) {
        return context.selectFrom(TICKET)
                .where(TICKET.ASSIGNED_TO.eq(assignedTo))
                .fetch();
    }

    public TicketRecord create(TicketRecord record) {
        return context.insertInto(TICKET)
                .set(record)
                .returning()
                .fetchOne();
    }

    public TicketTagRecord createTag(long ticketId, Tag tag) {
        return context.insertInto(TICKET_TAG)
                .set(TICKET_TAG.TICKET_ID, ticketId)
                .set(TICKET_TAG.TAG, tag)
                .returning()
                .fetchOne();
    }

    public void createTags(Long ticketId, List<Tag> tags) {
        tags.forEach(tag -> createTag(ticketId, tag));
    }

    public Optional<TicketRecord> update(long id, String title, String description) {
        return context.update(TICKET)
                .set(TICKET.TITLE, title)
                .set(TICKET.DESCRIPTION, description)
                .where(TICKET.ID.eq(id))
                .returning()
                .fetchOptional();
    }

    private Condition createFilterCondition(Optional<String> searchTerm,
                                            Optional<String> userName,
                                            Optional<List<Tag>> tags,
                                            Optional<Boolean> verified,
                                            Optional<Status> status) {
        Condition condition = DSL.noCondition();

        if (searchTerm.isPresent()) {
            condition = condition.and(TICKET.TITLE.containsIgnoreCase(searchTerm.get())
                    .or(TICKET.DESCRIPTION.containsIgnoreCase(searchTerm.get())));
        }
        if (userName.isPresent()) {
            condition = condition.and(USER.USERNAME.contains(userName.get()));
        }
        if (verified.isPresent()) {
            condition = condition.and(USER.VERIFIED.eq(verified.get()));
        }
        if (status.isPresent()) {
            condition = condition.and(TICKET.STATUS.eq(status.get()));
        }
        if (tags.isPresent()) {
            for (Tag tag : tags.get()) {
                condition = condition.and(TICKET_TAG.TAG.eq(tag));
            }
        }

        return condition;
    }
}
