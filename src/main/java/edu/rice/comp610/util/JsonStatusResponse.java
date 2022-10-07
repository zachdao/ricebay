package edu.rice.comp610.util;

/**
 * Using this JsonStatusResponse to make sure all the response have similar structure
 * {}
 */
public class JsonStatusResponse {
    private boolean success;
    private Object data;
    private String message;

    /**
     * Constructor of JSONStatusResponse.
     *
     * @param success if the response is success
     * @param data    data of resp
     * @param message resp message
     */
    public JsonStatusResponse(boolean success, Object data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }
}
