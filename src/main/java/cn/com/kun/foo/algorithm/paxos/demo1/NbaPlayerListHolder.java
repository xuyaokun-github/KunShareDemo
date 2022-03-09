package cn.com.kun.foo.algorithm.paxos.demo1;

import java.util.List;

public class NbaPlayerListHolder {

    private static List<NbaPlayer> NBAPLAYERLIST = null;

    public static NbaPlayer find(String name) {

        for (NbaPlayer nbaPlayer : NBAPLAYERLIST){
            if (name.equals(nbaPlayer.getName())){
                return nbaPlayer;
            }
        }
        return null;
    }


    public static int size() {

        return NBAPLAYERLIST.size();
    }

    public static void init(List<NbaPlayer> nbaPlayerList) {
        NBAPLAYERLIST = nbaPlayerList;
    }


    public static List<NbaPlayer> getAll() {
        return NBAPLAYERLIST;
    }
}
