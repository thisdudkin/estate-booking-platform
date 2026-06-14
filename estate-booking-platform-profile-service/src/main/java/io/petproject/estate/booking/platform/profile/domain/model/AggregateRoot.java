package io.petproject.estate.booking.platform.profile.domain.model;

import io.petproject.estate.booking.platform.profile.domain.event.DomainEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class AggregateRoot<ID> {

    private final List<DomainEvent> domainEvents = new ArrayList<>();

    public abstract ID id();

    protected final void registerEvent(DomainEvent event) {
        domainEvents.add(Objects.requireNonNull(event, "Domain event must not be null"));
    }

    public final List<DomainEvent> pullDomainEvents() {
        List<DomainEvent> events = List.copyOf(domainEvents);
        domainEvents.clear();
        return events;
    }

    public final boolean hasDomainEvents() {
        return !domainEvents.isEmpty();
    }

}
