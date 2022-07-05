package de.crafted.api.service.user.repository;

import static de.crafted.api.service.user.jooq.tables.User.USER;

import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DSLContext context;

    public List<UserRecord> findAll() {
        return context.selectFrom(USER)
                .orderBy(USER.ID.asc())
                .fetch();
    }

    public Optional<UserRecord> findById(long id) {
        return context.selectFrom(USER)
                .where(USER.ID.eq(id))
                .fetchOptional();
    }

    public Optional<UserRecord> findBySubject(String subject) {
        return context.selectFrom(USER)
                .where(USER.SUBJECT.eq(subject))
                .fetchOptional();
    }

    public UserRecord create(UserRecord userRecord) {
        return context.insertInto(USER)
                .set(userRecord)
                .returning()
                .fetchOne();
    }

    public UserRecord update(UserRecord userRecord) {
        return context.update(USER)
                .set(userRecord)
                .where(USER.ID.eq(userRecord.getId()))
                .returning()
                .fetchOne();
    }
}
