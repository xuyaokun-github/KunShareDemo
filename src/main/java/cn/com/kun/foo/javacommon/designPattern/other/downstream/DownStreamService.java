package cn.com.kun.foo.javacommon.designPattern.other.downstream;

import cn.com.kun.common.utils.JacksonUtils;

public class DownStreamService {

    public static void main(String[] args) {

        DownStreamService downStreamService = new DownStreamService();

        UniRepsonse uniRepsonse = downStreamService.send("EMAIL");
        System.out.println(JacksonUtils.toJSONString(uniRepsonse));
        uniRepsonse = downStreamService.send("SMS");
        System.out.println(JacksonUtils.toJSONString(uniRepsonse));
    }

    public UniRepsonse send(String type){

        //无论下游返回什么，上层只管接收UniRepsonse
        UniRepsonse uniRepsonse = getResponse(type);
        //利用多态，调转换方法
        uniRepsonse.tranfer();
        return uniRepsonse;
    }

    private UniRepsonse getResponse(String type) {

        UniRepsonse uniRepsonse = null;
        switch (type) {
            case "SMS" :
                SmsResponse smsResponse = new SmsResponse();
                smsResponse.setCode1("777");
                smsResponse.setMessage1("aaa");
                uniRepsonse = smsResponse;
                break;
            case "EMAIL" :
                EmaiResponse emaiResponse = new EmaiResponse();
                emaiResponse.setCode("888");
                emaiResponse.setMessage("bbb");
                uniRepsonse = emaiResponse;
                break;
        }
        return uniRepsonse;
    }

}
