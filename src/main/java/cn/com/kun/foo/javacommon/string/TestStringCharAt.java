package cn.com.kun.foo.javacommon.string;

public class TestStringCharAt {

    public static void main(String[] args) {

//        String str = "kunghsu";
//        System.out.println(str.charAt(0));
//        System.out.println(str.charAt(6));

        String str2 = "kungh\\suddddd\\dddddd";
        System.out.println(str2.contains("\\"));


        int cnt = 0;
        String str = "kungh\\suddddd\\dddddd";
        if(str.contains("\\")){
            cnt = str.length() - str.replaceAll("\\\\","").length();
        }
        System.out.println(cnt);

        System.out.println(countCharNum(str, '\\'));
        System.out.println(countCharNum2(str, '\\'));

    }

    /**
     * 统计字符个数
     * 这种方法不好，正则遇到\ 就会报错,要做特殊处理
     * @param str
     * @param c
     * @return
     */
    private static int countCharNum2(String str, char c) {

        if (str == null || str.length() < 1){
            return 0;
        }
        String replaceKey = String.valueOf(c).equals("\\") ? "\\\\" : String.valueOf(c);
        return str.length() - str.replaceAll(replaceKey,"").length();
    }

    /**
     * 统计字符个数
     * 用这个方法统计\\ 一样会报错,要做特殊处理
     * @param str
     * @param c
     * @return
     */
    private static int countCharNum(String str, char c) {

        if (str == null || str.length() < 1){
            return 0;
        }
        String splitKey = String.valueOf(c).equals("\\") ? "\\\\" : String.valueOf(c);
        String[] split = str.split(splitKey);
        int len = split.length == 0? 0 : split.length - 1;

        return len;
    }
}
