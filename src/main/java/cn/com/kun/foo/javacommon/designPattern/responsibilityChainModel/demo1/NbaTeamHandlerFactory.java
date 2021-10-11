package cn.com.kun.foo.javacommon.designPattern.responsibilityChainModel.demo1;

public class NbaTeamHandlerFactory {

    public static NbaTeamHandler createNbaTeamHandler(){

        //
        NbaTeamHandler heats = new HeatsNbaTeamHandler("MIA");
        NbaTeamHandler lakers = new LakersNbaTeamHandler("LAL");
        NbaTeamHandler rockets = new RocketsNbaTeamHandler("HOU");
        heats.setSuccessor(lakers);
        lakers.setSuccessor(rockets);
        return heats;
    }

}
