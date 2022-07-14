package de.crafted.api.service.image.repository;

import static de.crafted.api.service.image.jooq.tables.Image.IMAGE;
import static de.crafted.api.service.image.jooq.tables.TicketImage.TICKET_IMAGE;
import static de.crafted.api.service.user.jooq.tables.User.USER;

import de.crafted.api.service.image.jooq.tables.records.ImageRecord;
import de.crafted.api.service.image.jooq.tables.records.TicketImageRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ImageRepository {
    private final DSLContext context;

    public Optional<ImageRecord> findById(long id) {
        return context.selectFrom(IMAGE)
                .where(IMAGE.ID.eq(id))
                .fetchOptional();
    }

    public List<ImageRecord> findByTicketId(long ticketId) {
        return context.select().from(IMAGE)
                .leftJoin(TICKET_IMAGE).on(TICKET_IMAGE.IMAGE_ID.eq(IMAGE.ID))
                .where(TICKET_IMAGE.TICKET_ID.eq(ticketId))
                .fetchInto(IMAGE);
    }

    public ImageRecord create(ImageRecord record) {
        return context.insertInto(IMAGE)
                .set(record)
                .returning()
                .fetchOne();
    }

    public TicketImageRecord createTicketImage(TicketImageRecord record) {
        return context.insertInto(TICKET_IMAGE)
                .set(record)
                .returning()
                .fetchOne();
    }

    public Optional<ImageRecord> findByUserId(long userId) {
        return context.select().from(IMAGE)
                .leftJoin(USER).on(IMAGE.ID.eq(USER.ID))
                .where(USER.ID.eq(userId))
                .fetchOptionalInto(IMAGE);
    }
}
