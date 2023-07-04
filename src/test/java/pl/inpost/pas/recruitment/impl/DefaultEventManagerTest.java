package pl.inpost.pas.recruitment.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import pl.inpost.pas.recruitment.EventManager;
import pl.inpost.pas.recruitment.events.BaseTestEvent;
import pl.inpost.pas.recruitment.events.MockEventListener;
import pl.inpost.pas.recruitment.events.SpecificTestEvent;

import static org.junit.jupiter.api.Assertions.*;

class DefaultEventManagerTest {

    private EventManager eventManager;

    @BeforeEach
    void setUp() {
        eventManager = new DefaultEventManager();
    }

    @Test
    void testPublishNullEvent() {
        assertDoesNotThrow(() -> eventManager.publishEvent(null));
    }

    @NullAndEmptySource
    @ParameterizedTest
    void testRegisterInvalidListenerKey(String listenerKey) {
        // given
        var mockEventListener = new MockEventListener(new Class[]{});

        // when & then
        assertThrows(IllegalArgumentException.class, () -> eventManager.registerListener(listenerKey, mockEventListener));
    }

    @Test
    void testRegisterInvalidEventListener() {
        assertThrows(IllegalArgumentException.class, () -> eventManager.registerListener("some.key", null));
    }

    @Test
    void testRegisterListenerAndPublishEvent() {
        // given
        var mockEventListener = new MockEventListener(new Class[]{SpecificTestEvent.class});

        // when
        eventManager.registerListener("some.key", mockEventListener);
        eventManager.publishEvent(new SpecificTestEvent());


        // then
        assertTrue(mockEventListener.isCalled());
    }

    @Test
    void test() {
        // given
        var mockEventListener = new MockEventListener(new Class[]{});

        // when
        eventManager.registerListener("some.key.test", mockEventListener);
        eventManager.publishEvent(new SpecificTestEvent());
        eventManager.publishEvent(new SpecificTestEvent());
        eventManager.registerListener("some.key.test2", mockEventListener);
        eventManager.publishEvent(new SpecificTestEvent());

        // then
        assertTrue(mockEventListener.isCalled());
    }

    @Test
    void testListenerWithoutMatchingEventClass() {
        // given
        var mockEventListener = new MockEventListener(new Class[]{BaseTestEvent.class});

        // when
        eventManager.registerListener("some.key", mockEventListener);
        eventManager.publishEvent(new SpecificTestEvent());

        // then
        assertFalse(mockEventListener.isCalled());
    }

    @Test
    void testRegisterSameEventListenerUnderDifferentKeys() {
        // given
        var mockEventListener = new MockEventListener(new Class[]{SpecificTestEvent.class});

        // when
        eventManager.registerListener("some.key", mockEventListener);
        eventManager.registerListener("other.key", mockEventListener);
        eventManager.publishEvent(new SpecificTestEvent());

        // then
        assertTrue(mockEventListener.isCalled());
        assertEquals(2, mockEventListener.getCount());
    }

    @Test
    void testUnregisterListener() {
        // given
        var mockEventListener1 = new MockEventListener(new Class[]{SpecificTestEvent.class});
        var mockEventListener2 = new MockEventListener(new Class[]{SpecificTestEvent.class});

        // when
        eventManager.registerListener("some.key", mockEventListener1);
        eventManager.registerListener("other.key", mockEventListener2);
        eventManager.unregisterListener("some.key");
        eventManager.publishEvent(new SpecificTestEvent());

        // then
        assertFalse(mockEventListener1.isCalled());
        assertTrue(mockEventListener2.isCalled());
    }

    @Test
    void testOverrideListenerRegistration() {
        // given
        var mockEventListener1 = new MockEventListener(new Class[]{SpecificTestEvent.class});
        var mockEventListener2 = new MockEventListener(new Class[]{SpecificTestEvent.class});

        // when
        eventManager.registerListener("some.key", mockEventListener1);
        eventManager.registerListener("some.key", mockEventListener2);
        eventManager.publishEvent(new SpecificTestEvent());

        // then
        assertFalse(mockEventListener1.isCalled());
        assertTrue(mockEventListener2.isCalled());
    }

    @Test
    void testUnregisterNotRegisteredListener() {
        assertDoesNotThrow(() -> eventManager.unregisterListener("some.key"));
    }

}