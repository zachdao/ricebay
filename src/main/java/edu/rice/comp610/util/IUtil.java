package edu.rice.comp610.util;

import com.google.gson.JsonParser;

/**
 * A utility interface defining useful util functions might be used in this project.
 */
public interface IUtil {

    String getRandomColor();

    double normalDistribution(double u, double v);

    int randomInt(int lowerBound, int upperBound);

    JsonParser getJsonParser();

}
