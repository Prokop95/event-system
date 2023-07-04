package pl.inpost.pas.recruitment.events;

import pl.inpost.pas.recruitment.Event;
import pl.inpost.pas.recruitment.EventListener;

public class MockEventListener implements EventListener {

    private final Class<?>[] handledClasses;
    private boolean called;
    private int count;

    public MockEventListener(Class<?>[] handledClasses) {
        this.handledClasses = handledClasses;
    }

    @Override
    public void handleEvent(Event event) {
        called = true;
        count++;
    }

    public void resetCalled() {
        called = false;
    }

    public boolean isCalled() {
        return called;
    }

    public void resetCount() {
        count = 0;
    }

    public int getCount() {
        return count;
    }

    @Override
    public Class<?>[] getHandlerEventClasses() {
        return handledClasses;
    }

}
