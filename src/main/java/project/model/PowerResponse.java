package project.model;

import java.time.Instant;

/**
 * Record for auto-mapping response JSON to useful fields for the {@code PowerStatus}.
 *
 * @param active_power_w The total sum of power in Watt.
 */
public record PowerResponse(double active_power_w) {

    /**
     * Updates the provided {@code PowerStatus} with all acquired fields.
     *
     * @param powerStatus The {@code PowerStatus} to update.
     */
    public void updateStatus(PowerStatus powerStatus) {
        powerStatus.setValue("activePowerW", active_power_w, Instant.now());
    }
}
