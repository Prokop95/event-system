package pl.inpost.pas.recruitment;

/**
 * A simple event listener.
 */
public interface EventListener {

    /**
     * Perform some action as a response to an {@link Event}.
     *
     * @param event an event publish using {@link EventManager#publishEvent(Event)}
     */
    void handleEvent(Event event);

    /**
     * Optionally limits which vent classes this listener is interested in.
     *
     * <p>Event Manager performs rudimentary filtering of events by their class. If
     *  you want to receive only a subset of events passing through the system, return
     *  an array of Classes you wish to listen for from this method.</p>
     *
     *  <p>For the sake of efficiency, only exact class matches are performed. Sub/superclassing
     *  is not taken into account.</p>
     *
     * @return An array of event classes that this event listener is only interested in,
     *         or an empty array if the listener should receive all event without filtering.
     *         <b>Must not</b> return null
     */
    Class<?>[] getHandlerEventClasses();

}
