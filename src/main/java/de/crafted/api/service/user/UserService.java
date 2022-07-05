package de.crafted.api.service.user;

import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import de.crafted.api.service.user.model.User;
import de.crafted.api.service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public User createUser(String subject, String username) {
        var record = new UserRecord();

        record.setSubject(subject);
        record.setUsername(username);
        record.setVerified(false);
        record.setUserCreateDate(ZonedDateTime.now().toLocalDateTime());

        return map(repository.create(record));
    }

    public Optional<User> getUser(String subject) {
        return repository.findBySubject(subject).map(this::map);
    }

    public Optional<User> getUser(long id) {
        return repository.findById(id).map(this::map);
    }

    public List<User> getUsers() {
        return repository.findAll()
                .stream()
                .map(this::map)
                .collect(Collectors.toList());
    }

    private User map(UserRecord record) {
        return User.builder()
                .id(record.getId())
                .username(record.getUsername())
                .verified(record.getVerified())
                .userCreateDate(record.getUserCreateDate() == null ? null : record.getUserCreateDate().atZone(ZoneId.systemDefault()))
                .userLastModifiedDate(record.getUserLastModifiedDate() == null ? null : record.getUserLastModifiedDate().atZone(ZoneId.systemDefault()))
                .build();
    }
}
