import co.mimosa.nms.config.AppConfig;
import co.mimosa.nms.redis.RedisOperations;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;


/**
 * Created by ramdurga on 3/23/15.
 */
public class Main {
    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        // CqlOperations cqlOperations = ctx.getBean(CqlOperations.class);
        RedisOperations operations= ctx.getBean(RedisOperations.class);
        RedisTemplate template = ctx.getBean(RedisTemplate.class);
        //RawMetricsRepository repository = ctx.getBean(RawMetricsRepository.class);
        System.out.println("successfull");
        //operations.addDevice("12345",20.2);
        //System.out.println(operations.readValue("12345"));
        //Map<String,Map<String,Map<Integer,Map<String,Double>>>> rollupMap5Mins = new HashMap<>();
        Map<String,Double> actualValues = new HashMap<>();
        IntStream.rangeClosed(1,35).forEach(i -> actualValues.put("abc"+i,2.2+i));


        Map<Integer,Map<String,Double>> internal5Mins = new HashMap<>();
        internal5Mins.put(2,actualValues);
        Map<String,Map<Integer,Map<String,Double>>> internal5MinsCurrent = new HashMap<>();
        internal5MinsCurrent.put("current",internal5Mins);
        internal5MinsCurrent.put("previous",internal5Mins);
        //rollupMap5Mins.put("12345",internal5MinsCurrent);
        long startTime = System.currentTimeMillis();
       // IntStream.rangeClosed(1,100000).parallel().forEach(i -> operations.addDevice5MinRollup("SR" + i, "5mins", internal5MinsCurrent));
        long endTime = System.currentTimeMillis();
        System.out.println("Time take to write:" + (endTime - startTime));
        startTime = System.currentTimeMillis();
        IntStream.rangeClosed(1,100000).parallel().forEach(i-> operations.getDevice5MinRollup("SR"+i,"5mins").get("previous"));
        endTime = System.currentTimeMillis();
        System.out.println("Time take to READ :"+(endTime-startTime));
       // System.out.println(operations.getDevice5MinRollup("SR1","5mins").get("previous"));
        //template.

    }
}
