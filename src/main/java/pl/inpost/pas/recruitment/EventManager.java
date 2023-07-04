package pl.inpost.pas.recruitment;

public interface EventManager {

    /**
     * Publish an {@link Event} that will be consumed by any listener which has
     * been registered to receive it.
     *
     * @param event event to be published
     */
    void publishEvent(Event event);

    /**
     * Register an {@link EventListener} to receive {@link Event}s. If you register a listener with the
     * same key as an existing listener, the previous listener with that key will be unregistered.
     *
     * @param listenerKey A unique key for this listener
     * @param eventListener The listener that is being registered
     */
    void registerListener(String listenerKey, EventListener eventListener);

    /**
     * Unregister the listener so that it will no longer receive events.
     * If no listener has been registered under this key, this operation will be ignored.
     *
     * @param listenerKey A key of an {@link EventListener} which will be unregistered.
     */
    void unregisterListener(String listenerKey);

}
