package de.crafted.api.service.user;

import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.controller.model.UserProfileInput;
import de.crafted.api.service.common.mapper.TagMapper;
import de.crafted.api.service.common.model.Tag;
import de.crafted.api.service.image.ImageService;
import de.crafted.api.service.ticket.TicketService;
import de.crafted.api.service.user.jooq.tables.records.UserRecord;
import de.crafted.api.service.user.mapper.UserMapper;
import de.crafted.api.service.user.model.User;
import de.crafted.api.service.user.model.UserProfile;
import de.crafted.api.service.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final TicketService ticketService;
    private final ImageService imageService;

    public User createUser(String subject, String username) {
        var record = new UserRecord();

        record.setSubject(subject);
        record.setUsername(username);
        record.setVerified(false);
        record.setUserCreateDate(ZonedDateTime.now().toLocalDateTime());

        return UserMapper.map(repository.create(record));
    }

    public Optional<User> findBySubject(String subject) {
        return repository.findBySubject(subject)
                .map(UserMapper::map);
    }

    public Optional<User> findById(long id) {
        return repository.findById(id)
                .map(UserMapper::map);
    }

    public List<User> findAll(Optional<String> searchTerm, Optional<Boolean> verified, Optional<List<Tag>> tags, Optional<Boolean> bestRatingOrder) {
        Optional<List<de.crafted.api.service.common.jooq.enums.Tag>> mappedTags = Optional.empty();

        if (tags.isPresent()) {
            mappedTags = Optional.of(tags.get().stream().map(TagMapper::map).toList());
        }

        return repository.findAll(searchTerm, verified, mappedTags, bestRatingOrder)
                .stream()
                .map(UserMapper::map)
                .collect(Collectors.toList());
    }

    public UserProfile getProfile(long id) {
        var user = findById(id)
                .orElseThrow(ResourceNotFoundException::new);

        return getUserProfile(user);
    }

    public List<UserProfile> getProfiles(Optional<String> searchTerm, Optional<Boolean> verified, Optional<List<Tag>> tags, Optional<Boolean> bestRatingOrder) {
        return findAll(searchTerm, verified, tags, bestRatingOrder).stream()
                .map(this::getUserProfile)
                .toList();
    }

    private UserProfile getUserProfile(User user) {
        var tags = repository.findTagsByUserId(user.getId()).stream()
                .map(entry -> TagMapper.map(entry.getTag()))
                .toList();

        var tickets = ticketService.findByUserId(user.getId());
        var projects = ticketService.findByAssignedTo(user.getId());
        var image = user.getProfilePhotoId() != null ? imageService.findById(user.getProfilePhotoId()) : null;

        return UserProfile.builder()
                .user(user)
                .tickets(tickets)
                .profilePhoto(image)
                .projects(projects)
                .tags(tags)
                .build();
    }

    public UserProfile updateUserProfile(Long userId, UserProfileInput userProfileInput) {
        var user = repository.updateDescription(userId, userProfileInput.getDescription())
                .orElseThrow(ResourceNotFoundException::new);

        var tags = userProfileInput.getTags().stream()
                .map(TagMapper::map)
                .toList();

        repository.deleteTags(userId);
        repository.createTags(userId, tags);

        return getUserProfile(UserMapper.map(user));
    }
}
