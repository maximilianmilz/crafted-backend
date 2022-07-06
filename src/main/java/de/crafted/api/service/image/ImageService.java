package de.crafted.api.service.image;

import de.crafted.api.controller.execption.ResourceNotFoundException;
import de.crafted.api.service.image.jooq.tables.records.ImageRecord;
import de.crafted.api.service.image.jooq.tables.records.TicketImageRecord;
import de.crafted.api.service.image.mapper.ImageMapper;
import de.crafted.api.service.image.model.Image;
import de.crafted.api.service.image.model.TicketImage;
import de.crafted.api.service.image.repository.ImageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository repository;

    public Image findById(long id) {
        return repository.findById(id)
                .map(ImageMapper::map)
                .orElseThrow(ResourceNotFoundException::new);
    }

    public List<Image> findByTicketId(long ticketId) {
        return repository.findByTicketId(ticketId).stream()
                .map(ImageMapper::map)
                .toList();
    }

    public Image create(String url, String altText) {
        ImageRecord record = new ImageRecord();

        record.setUrl(url);
        record.setAltText(altText);

        return ImageMapper.map(repository.create(record));
    }

    public TicketImage createTicketImage(long ticketId, long imageId) {
        TicketImageRecord record = new TicketImageRecord();

        record.setTicketId(ticketId);
        record.setImageId(imageId);

        return ImageMapper.map(repository.createTicketImage(record));
    }
}
