package de.crafted.api.service.image.mapper;

import de.crafted.api.controller.model.ImageInput;
import de.crafted.api.service.image.jooq.tables.records.ImageRecord;
import de.crafted.api.service.image.jooq.tables.records.TicketImageRecord;
import de.crafted.api.service.image.model.Image;
import de.crafted.api.service.image.model.TicketImage;

public class ImageMapper {
    public ImageMapper() {
        throw new IllegalStateException("ImageMapper class");
    }

    public static Image map(ImageRecord record) {
        return Image.builder()
                .id(record.getId())
                .url(record.getUrl())
                .altText(record.getAltText())
                .build();
    }

    public static ImageRecord map(ImageInput input) {
        ImageRecord record = new ImageRecord();

        record.setUrl(input.getUrl());
        record.setAltText(input.getAltText());

        return record;
    }

    public static TicketImage map(TicketImageRecord record) {
        return TicketImage.builder()
                .ticketId(record.getTicketId())
                .imageId(record.getImageId())
                .build();
    }
}
