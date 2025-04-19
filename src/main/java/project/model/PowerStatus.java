package project.model;

import project.exception.CacheExpiredException;

import java.time.Instant;
import java.util.HashMap;

public class PowerStatus {

    private final HashMap<String, CachedData<Double>> map = new HashMap<>();

    public PowerStatus() {}

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
}
