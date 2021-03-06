package de.crafted.api.service.user.repository;

import static de.crafted.api.service.user.jooq.tables.User.USER;
import static de.crafted.api.service.common.jooq.tables.UserTag.USER_TAG;

import de.crafted.api.service.common.jooq.enums.Tag;
import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import de.crafted.api.service.common.jooq.tables.records.UserTagRecord;
import lombok.RequiredArgsConstructor;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.impl.DSL;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final DSLContext context;

    public List<UserRecord> findAll(Optional<String> searchTerm, Optional<Boolean> verified, Optional<List<Tag>> tags, Optional<Boolean> bestRatingOrder) {
        Condition condition = createFilterCondition(searchTerm, verified, tags);


        return context.selectFrom(USER)
                .where(condition)
                .orderBy(bestRatingOrder.isPresent() && bestRatingOrder.get() ? USER.RATING.desc() : USER.ID.asc())
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

    public List<UserTagRecord> findTagsByUserId(long userId) {
        return context.selectFrom(USER_TAG)
                .where(USER_TAG.USER_ID.eq(userId))
                .fetch();
    }

    public UserRecord create(UserRecord userRecord) {
        return context.insertInto(USER)
                .set(userRecord)
                .returning()
                .fetchOne();
    }

    public Optional<UserRecord> updateDescription(long userId, String description) {
        return context.update(USER)
                .set(USER.DESCRIPTION, description)
                .where(USER.ID.eq(userId))
                .returning()
                .fetchOptional();
    }

    public UserTagRecord createTag(long userId, Tag tag) {
        return context.insertInto(USER_TAG)
                .set(USER_TAG.USER_ID, userId)
                .set(USER_TAG.TAG, tag)
                .returning()
                .fetchOne();
    }

    public void createTags(long userId, List<Tag> tags) {
        tags.forEach(tag -> createTag(userId, tag));
    }

    public void deleteTags(long userId) {
        context.delete(USER_TAG)
                .where(USER_TAG.USER_ID.eq(userId))
                .returning()
                .fetch();
    }

    private Condition createFilterCondition(Optional<String> searchTerm, Optional<Boolean> verified, Optional<List<Tag>> tags) {
        Condition condition = DSL.noCondition();

        if (searchTerm.isPresent()) {
            condition = condition.and(USER.USERNAME.containsIgnoreCase(searchTerm.get())
                    .or(USER.DESCRIPTION.containsIgnoreCase(searchTerm.get())));
        }
        if (verified.isPresent()) {
            condition = condition.and(USER.VERIFIED.eq(verified.get()));
        }
        if (tags.isPresent()) {
            Condition tagCondition = DSL.noCondition();
            for (Tag tag : tags.get()) {
                tagCondition = tagCondition.or(USER_TAG.TAG.eq(tag));
            }
            condition = condition.and(tagCondition);
        }

        return condition;
    }
}
