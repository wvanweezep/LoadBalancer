package project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import project.model.PowerResponse;
import project.model.PowerStatus;

@Service
public class PowerService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private PowerStatus powerStatus;

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
