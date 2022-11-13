package edu.rice.comp610.store;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface indicating a primary key for a model object.
 * May be applied to a getter or a setter method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface PrimaryKey {
    /**
     * Indicates that the primary key is auto-generated in the DB schema.
     */
    boolean generated() default false;
}
