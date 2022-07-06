package de.crafted.api.service.user.mapper;

import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import de.crafted.api.service.user.model.User;

import java.time.ZoneId;

public class UserMapper {
    public UserMapper() {
        throw new IllegalStateException("UserMapper class");
    }

    public static User map(UserRecord record) {
        return User.builder()
                .id(record.getId())
                .username(record.getUsername())
                .description(record.getDescription())
                .subject(record.getSubject())
                .verified(record.getVerified())
                .userCreateDate(record.getUserCreateDate() == null ? null : record.getUserCreateDate().atZone(ZoneId.systemDefault()))
                .userLastModifiedDate(record.getUserLastModifiedDate() == null ? null : record.getUserLastModifiedDate().atZone(ZoneId.systemDefault()))
                .profilePhotoId(record.getImageId())
                .build();
    }
}
