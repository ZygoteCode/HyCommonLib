package me.zygotecode.hycommonlib.eventapi;

import me.zygotecode.hycommonlib.eventapi.types.HsPriority;

import java.lang.annotation.*;

/**
 * Marks a method so that the EventManager knows that it should be registered.
 * The priority of the method is also set with this.
 *
 * @author DarkMagician6
 * @see HsPriority
 * @since July 30, 2013
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HsEventTarget {

    byte value() default HsPriority.MEDIUM;
}
