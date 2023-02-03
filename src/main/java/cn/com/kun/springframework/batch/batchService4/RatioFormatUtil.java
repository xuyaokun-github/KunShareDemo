package cn.com.kun.springframework.batch.batchService4;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * 计算比例工具
 */
public class RatioFormatUtil {

    /**
     * 获取百分率 含%
     *
     * @param number
     * @param denominator
     * @return eg:33.33%
     */
    public static String getRatioStr(double number, double denominator) {
        String ratio = "0.00%";
        if (denominator != 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#0.00%");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            ratio = decimalFormat.format(number / denominator);
        }
        return ratio;
    }

    /**
     * 获取百分率 不含%
     *
     * @param number
     * @param denominator
     * @return eg:33.33
     */
    public static Double getRatioForDouble(double number, double denominator) {
        Double ratio = 0.00;
        if (denominator != 0) {
            DecimalFormat decimalFormat = new DecimalFormat("#0.0000");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String ratioStr = decimalFormat.format(number / denominator);
            ratio = Double.valueOf(ratioStr) * 100;
            //处理 可能出现小数点后大于2个的情况
            DecimalFormat df = new DecimalFormat("######0.00");
            String format = df.format(ratio);
            ratio = Double.valueOf(format);
        }
        return ratio;
    }

    /**
     * 获取百分率 含%
     *
     * @param number
     * @param denominator
     * @return eg:33.33%
     */
    public static String getRatioStr(BigDecimal number, BigDecimal denominator) {
        BigDecimal ratio = BigDecimal.valueOf(0.00);
        if (denominator.doubleValue()!=0) {
            ratio = number.divide(denominator, 4, RoundingMode.HALF_UP);
        }
        //将结果百分比
        NumberFormat percent = NumberFormat.getPercentInstance();
        percent.setMaximumFractionDigits(2);
        String formatRatio = percent.format(ratio.doubleValue());
        return formatRatio;
    }

    /**
     * 获取百分率 不含%
     *
     * @param number
     * @param denominator
     * @return eg:33.33
     */
    public static Double getRatioDouble(BigDecimal number, BigDecimal denominator) {
        BigDecimal ratio = BigDecimal.valueOf(0.00);
        if (denominator.doubleValue()!=0) {
            ratio = number.divide(denominator, 4, RoundingMode.HALF_UP);
            ratio = ratio.multiply(BigDecimal.valueOf(100));
            DecimalFormat df = new DecimalFormat("#0.00");
            String format = df.format(ratio);
            ratio = new BigDecimal(format);
        }
        return ratio.doubleValue();
    }

    /**
     * 获取百分率 不含%
     *
     * @param number
     * @param denominator
     * @return eg:33.33
     */
    public static Double getRatioForInt(int number, int denominator) {

        return getRatioDouble(new BigDecimal(number), new BigDecimal(denominator));
    }

    public static void main(String[] args) {

        System.out.println(getRatioForInt(3, 70000));

//        System.out.println(getRatioDouble(3, 7));
    }
}

