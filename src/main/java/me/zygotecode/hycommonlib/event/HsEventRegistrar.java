package me.zygotecode.hycommonlib.event;

import com.hypixel.hytale.event.EventPriority;
import com.hypixel.hytale.event.EventRegistry;
import com.hypixel.hytale.event.IBaseEvent;

import java.lang.reflect.Method;

public final class HsEventRegistrar {
    private final EventRegistry eventRegistry;

    public HsEventRegistrar(EventRegistry eventRegistry) {
        this.eventRegistry = eventRegistry;
    }

    public void registerEvents(Object listener) {
        Class<?> listenerClass = listener.getClass();

        for (Method method : listenerClass.getDeclaredMethods()) {

            HsEventHandler handler = method.getAnnotation(HsEventHandler.class);
            if (handler == null)
                continue;

            if (method.getParameterCount() != 1)
                continue;

            Class<?> paramType = method.getParameterTypes()[0];

            if (!IBaseEvent.class.isAssignableFrom(paramType))
                continue;

            method.setAccessible(true);

            registerMethod(
                    handler.priority(),
                    listener,
                    method,
                    paramType
            );
        }
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private void registerMethod(
            EventPriority priority,
            Object listener,
            Method method,
            Class<?> rawEventClass
    ) {
        Class<? extends IBaseEvent<?>> eventClass =
                (Class<? extends IBaseEvent<?>>) rawEventClass;

        eventRegistry.registerGlobal(
                priority,
                (Class) eventClass,
                event -> {
                    try {
                        method.invoke(listener, event);
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
        );
    }
}