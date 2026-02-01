package me.zygotecode.hycommonlib.eventapi.events.callables;

import me.zygotecode.hycommonlib.eventapi.events.HsEvent;
import me.zygotecode.hycommonlib.eventapi.events.HsTyped;

/**
 * Abstract example implementation of the Typed interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class HsEventTyped implements HsEvent, HsTyped {

    private final byte type;

    /**
     * Sets the type of the event when it's called.
     *
     * @param eventType
     *         The type ID of the event.
     */
    protected HsEventTyped(byte eventType) {
        type = eventType;
    }

    /**
     * @see HsTyped.getType
     */
    @Override
    public byte getType() {
        return type;
    }

}
