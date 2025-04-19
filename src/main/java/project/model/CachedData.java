package project.model;

import java.time.Instant;

/**
 * Record for storing any data with a {@code Instant} timestamp and a validity,
 * keeping track when data gets stale.
 *
 * @param value The value to store of type {@code T}.
 * @param timestamp The timestamp on creation.
 * @param validity The amount of seconds this data is valid after creation.
 * @param <T> The type for the value stored.
 */
public record CachedData<T>(T value, Instant timestamp, int validity) {

    /**
     * Checks whether the data is stale by comparing its timestamp
     * with the one provided.
     *
     * @return {@code true}, if the time elapsed is greater than the {@code validity} of the data.
     */
    public boolean isOverdue() {
        return Instant.now().getEpochSecond() - this.timestamp.getEpochSecond() > validity;
    }
}
