package me.zygotecode.hycommonlib.event;

import com.hypixel.hytale.event.EventPriority;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface HsEventHandler {
    EventPriority priority() default EventPriority.NORMAL;
}