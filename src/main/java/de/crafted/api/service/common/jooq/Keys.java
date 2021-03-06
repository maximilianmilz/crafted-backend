/*
 * This file is generated by jOOQ.
 */
package de.crafted.api.service.common.jooq;


import de.crafted.api.service.common.jooq.tables.TicketTag;
import de.crafted.api.service.common.jooq.tables.UserTag;
import de.crafted.api.service.common.jooq.tables.records.TicketTagRecord;
import de.crafted.api.service.common.jooq.tables.records.UserTagRecord;

import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;


/**
 * A class modelling foreign key relationships and constraints of tables in 
 * the default schema.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<TicketTagRecord> TICKET_TAG_PKEY = Internal.createUniqueKey(TicketTag.TICKET_TAG, DSL.name("ticket_tag_pkey"), new TableField[] { TicketTag.TICKET_TAG.TICKET_ID, TicketTag.TICKET_TAG.TAG }, true);
    public static final UniqueKey<UserTagRecord> USER_TAG_PKEY = Internal.createUniqueKey(UserTag.USER_TAG, DSL.name("user_tag_pkey"), new TableField[] { UserTag.USER_TAG.USER_ID, UserTag.USER_TAG.TAG }, true);
}
