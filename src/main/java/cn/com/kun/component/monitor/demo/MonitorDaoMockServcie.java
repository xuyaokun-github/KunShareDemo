package cn.com.kun.component.monitor.demo;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MonitorDaoMockServcie {

    private Map<String, String> statusMap = new HashMap<>();

    public String query(String jobId) {
        return statusMap.get(jobId);
    }

    public void addStatus(String jobId, String status) {
        statusMap.put(jobId, status);
    }
}
