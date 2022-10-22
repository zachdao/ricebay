package edu.rice.comp610.controller;

import java.util.Objects;

/**
 * Represents a response from the RiceBay application. A response consists of
 * <ul>
 *   <li>success - a boolean indicating success if true</li>
 *   <li>data - the result of the method if any</li>
 *   <li>message - an error message if not successful</li>
 * </ul>
 * @param <T> the type of the response data.
 */
public class AppResponse<T> {
    private final boolean success;
    private final T data;
    private final String message;

    /**
     * Create a new response.
     *
     * @param success true if the response indicates success
     * @param data the result if there is any, null otherwise
     * @param message an error message if success is false
     */
    public AppResponse(boolean success, T data, String message) {
        this.success = success;
        this.data = data;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public T getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AppResponse<?> that = (AppResponse<?>) o;
        return success == that.success && Objects.equals(data, that.data) && Objects.equals(message, that.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(success, data, message);
    }

    @Override
    public String toString() {
        return "AppResponse{" +
                "success=" + success +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
