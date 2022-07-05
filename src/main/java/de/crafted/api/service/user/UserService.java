package de.crafted.api.service.user;

import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.service.common.mapper.TagMapper;
import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import de.crafted.api.service.user.model.User;
import de.crafted.api.service.user.model.UserProfile;
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

    public UserProfile getProfile(long id) {
        var user = getUser(id).orElseThrow(ResourceNotFoundException::new);

        return getUserProfile(user);
    }

    public List<UserProfile> getProfiles() {
        return getUsers().stream()
                .map(this::getUserProfile)
                .toList();
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

    private UserProfile getUserProfile(User user) {
        var tags = repository.findUserTagsById(user.getId()).stream()
                .map(entry -> TagMapper.map(entry.getTag()))
                .toList();
        // TODO fetch own and assigned tickets by userId

        return UserProfile.builder()
                .id(user.getId())
                .username(user.getUsername())
                .createdDate(user.getUserCreateDate())
                .verified(user.getVerified())
                .imageUrl("")
                .ownTickets(List.of())
                .assignedTickets(List.of())
                .tags(tags)
                .build();
    }
}
