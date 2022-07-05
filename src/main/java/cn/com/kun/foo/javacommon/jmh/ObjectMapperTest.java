package cn.com.kun.foo.javacommon.jmh;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@BenchmarkMode({Mode.Throughput})
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Warmup(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@Threads(10)
public class ObjectMapperTest {
    String json = "{ \"color\" : \"Black\", \"type\" : \"BMW\" }";

    @State(Scope.Benchmark)
    public static class BenchmarkState {
        ObjectMapper GLOBAL_MAP = new ObjectMapper();
        ThreadLocal<ObjectMapper> GLOBAL_MAP_THREAD = new ThreadLocal<>();
    }

    @Benchmark
    public Map globalTest(BenchmarkState state) throws Exception{
        Map map = state.GLOBAL_MAP.readValue(json, Map.class);
        return map;
    }


    @Benchmark
    public Map globalTestThreadLocal(BenchmarkState state) throws Exception{
        if(null == state.GLOBAL_MAP_THREAD.get()){
            state.GLOBAL_MAP_THREAD.set(new ObjectMapper());
        }
        Map map = state.GLOBAL_MAP_THREAD.get().readValue(json, Map.class);
        return map;
    }

    @Benchmark
    public Map localTest() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        Map map = objectMapper.readValue(json, Map.class);
        return map;
    }

    public static void main(String[] args) throws Exception {
        Options opts = new OptionsBuilder()
                .include(ObjectMapperTest.class.getSimpleName())
                .resultFormat(ResultFormatType.CSV)
                .build();

        new Runner(opts).run();
    }
}
