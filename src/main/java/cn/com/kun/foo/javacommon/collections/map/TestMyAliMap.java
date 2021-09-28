package cn.com.kun.foo.javacommon.collections.map;

public class TestMyAliMap {

    public static void main(String[] args) throws InterruptedException {

        //最多存3个，10秒后自动过期
        MyAliMap myAliMap = new MyAliMap(3, 10 * 1000);

        myAliMap.put("111", "aaa");
        myAliMap.setLruCallback("111", new MyAliMap.LruCallBackFunction() {
            @Override
            public void lruCallback(String key, Object value) {
                System.out.println(String.format("触发了lruCallback回调，key:%s", key));
            }
        });
        myAliMap.put("222", "bbb");
        myAliMap.put("333", "ccc");
        myAliMap.setTimeoutCallback("333", new MyAliMap.TimeoutCallBackFunction() {
            @Override
            public void timeoutCallback(String key, Object value) {
                System.out.println(String.format("触发了timeoutCallback回调，key:%s", key));
            }
        });

        try {
            Thread.sleep(15000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        myAliMap.get("555");
        myAliMap.put("444", "ddd");

        myAliMap.forEach((k,v)->{
            System.out.println(String.format("key:%s value:%s", k, v));
        });

        myAliMap.put("666", "ddd");
        myAliMap.setLruCallback("666", new MyAliMap.LruCallBackFunction() {
            @Override
            public void lruCallback(String key, Object value) {
                System.out.println(String.format("触发了timeoutCallback回调，key:%s", key));
            }
        });
        myAliMap.put("777", "ddd");
        myAliMap.put("888", "ddd");
        myAliMap.put("999", "ddd");

        myAliMap.forEach((k,v)->{
            System.out.println(String.format("key:%s value:%s", k, v));
        });

        //因为例子中设置了国企时间为10秒，这里睡15秒，预期结果是map输出为零
        Thread.sleep(15000);
        System.out.println(myAliMap.size());
        myAliMap.forEach((k,v)->{
            System.out.println(String.format("key:%s value:%s", k, v));
        });

    }

}
