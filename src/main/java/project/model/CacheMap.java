package project.model;

import project.exception.CacheExpiredException;

import java.time.Instant;
import java.util.HashMap;

/**
 * Class for keeping track of all received data from the P1 Meter,
 * while also keeping track of the validity of the data.
 */
public class CacheMap {

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


    /**
     * {@code HashMap} for keeping track of all useful data of the P1 Meter.
     */
    private final HashMap<String, CachedData<Double>> map = new HashMap<>();

    public CacheMap() {}

    /**
     * Sets an accompanied value of a key in the {@code HashMap} to the value provided,
     * updating the {@code timestamp} in the process.
     *
     * @param key The key of the data to set in the {@code HashMap}.
     * @param value The new value for the given key in the {@code HashMap}
     * @param timestamp The new {@code Instant} timestamp of the provided data.
     * @throws IllegalArgumentException if the given {@code timestamp} is before the
     * previously set {@code timestamp} of the key.
     */
    public void setValue(String key, double value, Instant timestamp) {
        if (timestamp == null)
            throw new IllegalArgumentException("Timestamp cannot be null.");
        if (key == null)
            throw new IllegalArgumentException("Key cannot be null.");

        CachedData<Double> data = map.get(key);
        if (data != null && data.timestamp().isAfter(timestamp))
            throw new IllegalArgumentException(
                    "New timestamp cannot be before the previously set timestamp.");
        map.put(key, new CachedData<>(value, timestamp, 10));
    }

    /**
     * Gets a value at the provided key in the {@code HashMap}.
     *
     * @param key The key of the requested value.
     * @return A {@code double} with the requested value.
     *
     * @throws IllegalArgumentException if the key is not present in the map.
     * @throws CacheExpiredException if the data stored at the key is stale.
     */
    public double getValue(String key) {
        CachedData<Double> data = map.get(key);
        if (data == null)
            throw new IllegalArgumentException(
                    "No key in HashMap with value: " + key);
        else if (data.isOverdue())
            throw new CacheExpiredException(
                    "Cached data at key '" + key + "' is stale and should not be used.");
        else return data.value();
    }

    /**
     * Checks whether a key-value is overdue. Should be used before trying to
     * retrieve data that might be stale.
     *
     * @param key The key of the requested timestamp.
     * @return true, if the data is stale.
     */
    public boolean isValueOverdue(String key) {
        CachedData<Double> data = map.get(key);
        if (data == null)
            throw new IllegalArgumentException(
                    "No key in HashMap with value: " + key);
        return data.isOverdue();
    }
}
