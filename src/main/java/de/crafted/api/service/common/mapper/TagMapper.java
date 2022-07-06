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

    public static de.crafted.api.service.common.jooq.enums.Tag map(Tag tag) {
        return switch (tag) {
            case SANITARY -> de.crafted.api.service.common.jooq.enums.Tag.sanitary;
            case WOOD -> de.crafted.api.service.common.jooq.enums.Tag.wood;
            case METAL -> de.crafted.api.service.common.jooq.enums.Tag.metal;
            case ELECTRIC -> de.crafted.api.service.common.jooq.enums.Tag.electric;
            case MOVING -> de.crafted.api.service.common.jooq.enums.Tag.moving;
            case PAINTER -> de.crafted.api.service.common.jooq.enums.Tag.painter;
            case RENOVATION -> de.crafted.api.service.common.jooq.enums.Tag.renovation;
            case GARDENING -> de.crafted.api.service.common.jooq.enums.Tag.gardening;
            case MONTAGE -> de.crafted.api.service.common.jooq.enums.Tag.montage;
        };
    }
}
