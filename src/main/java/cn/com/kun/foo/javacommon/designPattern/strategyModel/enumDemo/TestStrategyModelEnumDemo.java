package cn.com.kun.foo.javacommon.designPattern.strategyModel.enumDemo;

public class TestStrategyModelEnumDemo {

    public static void main(String[] args) {

        StrategyModelQueryService queryService = new StrategyModelQueryService();
        queryService.query("EM01");
        queryService.query("EM02");
        queryService.query("PM01");

    }


}
