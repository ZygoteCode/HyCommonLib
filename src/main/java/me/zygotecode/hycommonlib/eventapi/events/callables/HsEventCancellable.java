package me.zygotecode.hycommonlib.eventapi.events.callables;

import me.zygotecode.hycommonlib.eventapi.events.HsCancellable;
import me.zygotecode.hycommonlib.eventapi.events.HsEvent;

/**
 * Abstract example implementation of the Cancellable interface.
 *
 * @author DarkMagician6
 * @since August 27, 2013
 */
public abstract class HsEventCancellable implements HsEvent, HsCancellable {

    private boolean cancelled;

    protected HsEventCancellable() {
    }

    /**
     * @see HsCancellable.isCancelled
     */
    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    /**
     * @see HsCancellable.setCancelled
     */
    @Override
    public void setCancelled(boolean state) {
        cancelled = state;
    }

}
