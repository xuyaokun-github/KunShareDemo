package cn.com.kun.springframework.actuator.customMetrics.itemStat;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ActivityDemoService {

    private Map<String, String> finishMap = new ConcurrentHashMap<>();

    public boolean checkActivityFinished(Object value) {

        return finishMap.containsKey(value);
    }

    public void markFinished(Object value){
        finishMap.put((String) value, (String) value);
    }


    public void markRunning(String value) {
        finishMap.remove(value);
    }
}
