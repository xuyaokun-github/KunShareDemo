package cn.com.kun.common.utils;


import brave.internal.Platform;
import brave.propagation.TraceContext;

public class TraceIDUtils {

    public final static String LOG_TRACE_ID = "traceId";

    /**
     * 生成traceId
     * @return
     */
    public static String getTraceId() {

        long nextId = nextId();
        TraceContext.Builder builder = TraceContext.newBuilder()
                .sampled(true)
                .traceIdHigh(false? Platform.get().nextTraceIdHigh() : 0L).traceId(nextId)
                .spanId(nextId);
        return builder.build().traceIdString();
    }

    static long nextId(){
        long nextId = Platform.get().randomLong();
        while (nextId == 0L){
            nextId = Platform.get().randomLong();
        }
        return nextId;
    }
}
