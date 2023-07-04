package pl.inpost.pas.recruitment.impl;

import pl.inpost.pas.recruitment.Event;
import pl.inpost.pas.recruitment.EventListener;
import pl.inpost.pas.recruitment.EventManager;

import java.util.*;

/**
 * Manages receiving and dispatching of events.
 *
 * <p>Any event passed to {@link #publishEvent} will be dispatched to all listeners
 * which are registered to handle it</p>
 *
 * <p>Event listeners can be registered to receive events via {@link #registerListener}</p>
 */
public class DefaultEventManager implements EventManager {

    private final Map<String, EventListener> eventListenerMap = new HashMap<>();
    private final Map<Class<?>, List<EventListener>> eventListenersByClass = new HashMap<>();
    private final Map<String, List<EventListener>> eventListenersWithoutClass = new HashMap<>();

    @Override
    public void publishEvent(Event event) {
        if (event == null) {
            System.err.println("Null event received");
            return;
        }

        var eventListeners = getEventListeners(event.getClass());
        if (eventListeners.isEmpty()) {
            List<EventListener> collect = eventListenersWithoutClass.values().stream()
                .flatMap(List::stream).toList();
            for (EventListener eventListener : collect) {
                eventListener.handleEvent(event);
            }
        } else {
        for (var eventListener : eventListeners) {
            eventListener.handleEvent(event);
        }}
    }

    private Collection<EventListener> getEventListeners(Class<?> clazz) {
        return Objects.requireNonNullElseGet(eventListenersByClass.get(clazz), Collections::emptyList);
    }

    @Override
    public void registerListener(String listenerKey, EventListener eventListener) {
        if (listenerKey == null || listenerKey.isEmpty()) {
            throw new IllegalArgumentException("listenerKey must not be null nor empty: " + eventListener);
        }

        if (eventListener == null) {
            throw new IllegalArgumentException("eventListener must not be null");
        }

        if (eventListenerMap.containsKey(listenerKey)) {
            unregisterListener(listenerKey);
        }

        var classes = Objects.requireNonNull(eventListener.getHandlerEventClasses());

        if (classes.length == 0) {
            addEventListenerWithoutClass(listenerKey ,eventListener);
        } else {
        for (var clazz : classes) {
            addEventListener(clazz, eventListener);
        }
        }

        eventListenerMap.put(listenerKey, eventListener);
    }

    private void addEventListener(Class<?> clazz, EventListener eventListener)
    {
        eventListenersByClass.computeIfAbsent(clazz, __ -> new LinkedList<>());
        eventListenersByClass.get(clazz).add(eventListener);
    }

    private void addEventListenerWithoutClass(String listenerKey, EventListener listener) {
        eventListenersWithoutClass.computeIfAbsent(listenerKey, __ -> new LinkedList<>());
        eventListenersWithoutClass.get(listenerKey).add(listener);

    }

    @Override
    public void unregisterListener(String listenerKey) {
        var eventListener = eventListenerMap.remove(listenerKey);
        if (eventListener == null) return;

        for (var clazz : eventListener.getHandlerEventClasses()) {
            eventListenersByClass.get(clazz).remove(eventListener);
        }
    }

}
