package edu.rice.comp610.util;

import com.google.gson.JsonParser;

public final class Util implements IUtil {
    private Util() {
    }

    private static final Util instance = new Util();

    public static Util getInstance() {
        return instance;
    }


    private final java.util.Random random = new java.util.Random();
    private final JsonParser jsonParser = new JsonParser();

    public int randomInt(int lowerBound, int upperBound) {
        return random.nextInt(upperBound - lowerBound) + lowerBound;
    }

    @Override
    public JsonParser getJsonParser() {
        return jsonParser;
    }

    /**
     * Sample with Normal Distribution with POSITIVE result.
     *
     * @param u mean
     * @param v std
     * @return positive result of normal distribution.
     */
    public double normalDistribution(double u, double v) {
        double result = Math.sqrt(v) * random.nextGaussian() + u;
        if (result <= 0) {
            return normalDistribution(u, v);
        } else {
            return result;
        }
    }


    // Acceptable color
    private static final String[] colors = new String[]{"red", "blue", "yellow", "green", "black", "purple", "orange", "gray", "brown"};

    /**
     * Get random color.
     *
     * @return random color
     */
    public String getRandomColor() {
        return colors[random.nextInt(colors.length)];
    }

}
