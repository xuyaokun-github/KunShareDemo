package cn.com.kun.foo.javacommon.jmh;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

/**
 * 没跑通
 * ERROR: transport error 202: connect failed: Connection refused
 * ERROR: JDWP Transport dt_socket failed to initialize, TRANSPORT_INIT(510)
 * JDWP exit error AGENT_ERROR_TRANSPORT_INIT(197): No transports initialized [debugInit.c:750]
 *
 * author:xuyaokun_kzx
 * date:2022/6/24
 * desc:
*/
@BenchmarkMode(Mode.Throughput)
@Warmup(iterations = 3,time = 30, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 3, time = 30, timeUnit = TimeUnit.SECONDS)
@Fork(1)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
public class MyBenchmarkTest {

    @Threads(500)
    @Benchmark
    public void testJMH() {
        try {
            Thread.sleep(50);    // 单线程20tps
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws RunnerException {
        Options options = new OptionsBuilder()
                .include(MyBenchmarkTest.class.getSimpleName())
                .build();
        new Runner(options).run();
    }
}
