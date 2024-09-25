package rs.etf.sab.student.util;

import java.math.BigDecimal;

public class Util {
    
  public  static double euclideanDist(int x1, int y1, int x2, int y2) {
    return Math.sqrt(((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
  }
  
  public static BigDecimal getPackagePriceForPacket(int type, BigDecimal weight, double distance, BigDecimal percentage) {
    percentage = percentage.divide(new BigDecimal(100));
    switch (type) {
      case 0:
        return (new BigDecimal(10.0D * distance)).multiply(percentage.add(new BigDecimal(1)));
      case 1:
        return (new BigDecimal((25.0D + weight.doubleValue() * 100.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
      case 2:
        return (new BigDecimal((75.0D + weight.doubleValue() * 300.0D) * distance)).multiply(percentage.add(new BigDecimal(1)));
    } 
    return null;
  }

    /*public static double getDistance(Pair<Integer, Integer>... addresses) {
        double distance = 0.0D;
        for (int i = 1; i < addresses.length; i++)
            distance += euclidean(((Integer)addresses[i - 1].getKey()).intValue(), ((Integer)addresses[i - 1].getValue()).intValue(), ((Integer)addresses[i]
              .getKey()).intValue(), ((Integer)addresses[i].getValue()).intValue()); 
        return distance;
    }
    
    static BigDecimal distance(final int x1, final int y1, final int x2, final int y2) {
        return BigDecimal.valueOf(Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)));
    }*/

}
