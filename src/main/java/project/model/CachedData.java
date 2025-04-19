package project.model;

import java.time.Instant;

public record CachedData<T>(T value, Instant timestamp, int validity) {

    public boolean isOverdue() {
        return Instant.now().getEpochSecond() - this.timestamp.getEpochSecond() > validity;
    }
}
