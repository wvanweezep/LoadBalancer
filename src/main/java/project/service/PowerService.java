package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.model.CacheMap;

import java.time.Instant;

@Service
public class PowerService {

    /**
     * Record for auto-mapping response JSON to useful fields for the {@code CacheMap}.
     *
     * @param active_power_w The total sum of power in Watt.
     */
    public record PowerResponse(double active_power_w) {

        /**
         * Updates the provided {@code CacheMap} with all acquired fields.
         *
         * @param powerStatus The {@code CacheMap} to update.
         */
        public void updateStatus(CacheMap powerStatus) {
            powerStatus.setValue("activePowerW", active_power_w, Instant.now());
        }
    }


    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheMap powerStatus;

    @Value("${api.ip.p1}")
    private String ipAddress;

    public String getData(String key) {
        if (powerStatus.isValueOverdue(key))
            fetchData();
        return Double.toString(powerStatus.getValue(key));
    }

    private void fetchData() {
        PowerResponse res = restTemplate.getForObject("http://" + ipAddress + "/api/v1/data", PowerResponse.class);
        if (res != null) res.updateStatus(powerStatus);
        else System.out.println("[WARNING] Unable to fetch from P1 Meter!");
    }

}
