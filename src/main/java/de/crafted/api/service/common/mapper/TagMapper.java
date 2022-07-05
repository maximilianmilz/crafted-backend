package de.crafted.api.service.common.mapper;

import de.crafted.api.service.common.model.Tag;

public class TagMapper {
    public TagMapper() {
        throw new IllegalStateException("TagMapper class");
    }

    public static Tag map(de.crafted.api.service.common.jooq.enums.Tag tag) {
        return switch (tag) {
            case sanitary -> Tag.SANITARY;
            case wood -> Tag.WOOD;
            case metal -> Tag.METAL;
            case electric -> Tag.ELECTRIC;
            case moving -> Tag.MOVING;
            case painter -> Tag.PAINTER;
            case renovation -> Tag.RENOVATION;
            case gardening -> Tag.GARDENING;
            case montage -> Tag.MONTAGE;
        };
    }
}
