package co.mimosa.nms.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by ramdurga on 3/23/15.
 */
@Component
public class RedisOperations {
    @Autowired
    private RedisTemplate<String,Double> template;

    public void addDevice(String serialNumber, Double value){
        template.boundListOps(serialNumber).leftPush(value);
    }
    public Double readValue(String serialNumber){
       return template.boundListOps(serialNumber).rightPop();
    }
    public void addDevice5MinRollup(String serialNumber,String timeWindow,Map<String,Map<Integer,Map<String,Double>>> rollup5mins){
        template.boundHashOps(serialNumber).put(timeWindow, rollup5mins);
        template.expire(serialNumber,5, TimeUnit.SECONDS);
    }
    public Map<String,Map<Integer,Map<String,Double>>> getDevice5MinRollup(String serialNumber,String timeWindow){
        Map<String, Map<Integer, Map<String, Double>>> stringMapMap = (Map<String, Map<Integer, Map<String, Double>>>) template.boundHashOps(serialNumber).get(timeWindow);
        if(stringMapMap==null){
            return new HashMap<>();
        }
        else return stringMapMap;
    }
}
