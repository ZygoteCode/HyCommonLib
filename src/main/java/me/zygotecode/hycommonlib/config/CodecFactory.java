package me.zygotecode.hycommonlib.config;

import com.hypixel.hytale.codec.Codec;
import com.hypixel.hytale.codec.KeyedCodec;
import com.hypixel.hytale.codec.builder.BuilderCodec;
import com.hypixel.hytale.codec.codecs.array.ArrayCodec;
import com.hypixel.hytale.codec.codecs.map.MapCodec;
import com.hypixel.hytale.codec.codecs.set.SetCodec;
import com.hypixel.hytale.logger.HytaleLogger;

import java.lang.reflect.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.logging.Level;

public class CodecFactory {

    private static final Map<Class<?>, Codec<?>> CODEC_REGISTRY = new ConcurrentHashMap<>(Map.ofEntries(
            Map.entry(String.class, Codec.STRING),
            Map.entry(boolean.class, Codec.BOOLEAN), Map.entry(Boolean.class, Codec.BOOLEAN),
            Map.entry(int.class, Codec.INTEGER), Map.entry(Integer.class, Codec.INTEGER),
            Map.entry(long.class, Codec.LONG), Map.entry(Long.class, Codec.LONG),
            Map.entry(double.class, Codec.DOUBLE), Map.entry(Double.class, Codec.DOUBLE),
            Map.entry(float.class, Codec.FLOAT), Map.entry(Float.class, Codec.FLOAT),
            Map.entry(byte.class, Codec.BYTE), Map.entry(Byte.class, Codec.BYTE),
            Map.entry(short.class, Codec.SHORT), Map.entry(Short.class, Codec.SHORT),
            Map.entry(UUID.class, Codec.UUID_STRING),
            Map.entry(Instant.class, Codec.INSTANT),
            // Primitive Arrays
            Map.entry(String[].class, Codec.STRING_ARRAY),
            Map.entry(int[].class, Codec.INT_ARRAY), Map.entry(Integer[].class, Codec.INT_ARRAY),
            Map.entry(long[].class, Codec.LONG_ARRAY), Map.entry(Long[].class, Codec.LONG_ARRAY),
            Map.entry(double[].class, Codec.DOUBLE_ARRAY), Map.entry(Double[].class, Codec.DOUBLE_ARRAY),
            Map.entry(float[].class, Codec.FLOAT_ARRAY), Map.entry(Float[].class, Codec.FLOAT_ARRAY)
    ));

    public static void registerCustomCodec(Class<?> clazz, Codec<?> codec) {
        CODEC_REGISTRY.put(clazz, codec);
    }

    /**
     * Automatically creates a BuilderCodec for the given class using reflection.
     * <p>
     * This method assumes:
     * 1. The class has a no-args constructor.
     * 2. Fields to be serialized are not static, final or transient and are annotated with @SkipConfigField.
     * 3. Standard Codec types (INT, STRING, etc.) are available in the Codec interface
     * 4. Non-standard types will be created recursively using this method (may cause problems, make sure to handle custom types as needed).
     * </p>
     *
     * @param <T>   The type of the class.
     * @param clazz The class to create a codec for.
     * @return A fully constructed BuilderCodec.
     */
    public static <T> BuilderCodec<T> createClassCodec(Class<T> clazz) {

        // 1. Create the Supplier (requires a no-args constructor)
        BuilderCodec.Builder<T> builder = BuilderCodec.builder(clazz, getConstructor(clazz));
        for (Field field : clazz.getDeclaredFields()) {
            if (shouldSkip(field)) continue;

            field.setAccessible(true);
            FieldName annotation = field.getAnnotation(FieldName.class);
            String name = (annotation != null && !annotation.value().isEmpty()) ? annotation.value() : field.getName();

            Codec<?> fieldCodec;
            if (Set.class.isAssignableFrom(field.getType())) {
                fieldCodec = resolveSetCodec(field.getType(), field.getGenericType());
            } else if (Map.class.isAssignableFrom(field.getType())) {
                fieldCodec = resolveMapCodec(field.getType(), field.getGenericType());
            } else if (field.getType().isArray()) {
                fieldCodec = resolveArrayCodec(field.getType());
            } else {
                fieldCodec = resolveCodec(field.getType());
            }

            // Capture generic type <V> via helper method
            appendField(builder, field, name, fieldCodec);
        }
        return builder.build();

    }

    private static boolean shouldSkip(Field field) {
        int mods = field.getModifiers();
        return Modifier.isStatic(mods)
                || Modifier.isTransient(mods)
                || Modifier.isFinal(mods)
                || field.isAnnotationPresent(SkipConfigField.class);
    }

    @SuppressWarnings("unchecked")
    private static <T, V> void appendField(BuilderCodec.Builder<T> builder, Field field, String name, Codec<V> codec) {
        builder.append(
                new KeyedCodec<>(name, codec),
                (obj, val) -> setField(field, obj, val),
                (obj) -> (V) getField(field, obj)
        ).add();
    }

    private static void setField(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            HytaleLogger.getLogger().at(Level.SEVERE).withCause(e).log("Failed to set " + field.getName());
            throw new RuntimeException("Failed to set " + field.getName(), e);
        }
    }

    private static Object getField(Field field, Object obj) {
        try {
            return field.get(obj);
        } catch (IllegalAccessException e) {
            HytaleLogger.getLogger().at(Level.SEVERE).withCause(e).log("Failed to get " + field.getName());
            throw new RuntimeException("Failed to get " + field.getName(), e);
        }
    }

    private static <T> Supplier<T> getConstructor(Class<T> clazz) {
        return () -> {
            try {
                // Standard POJO no-args
                Constructor<T> ctor = clazz.getDeclaredConstructor();
                ctor.setAccessible(true);
                return ctor.newInstance();

            } catch (Exception e) {
                throw new RuntimeException("Failed to instantiate " + clazz.getName(), e);
            }
        };
    }

    /**
     * Helper to map Java classes to Hytale Codecs.
     * Note: You may need to adjust the Codec references (e.g., Codec.INT, Codec.STRING)
     * depending on how they are named in your specific Codec interface/class.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T> Codec<T> resolveCodec(Class<T> type) {
        if (CODEC_REGISTRY.containsKey(type)) {
            return (Codec<T>) CODEC_REGISTRY.get(type);
        }
        // 2. Handle Arrays (Generic Object Arrays)
        if (type.isArray()) {
            Class<?> componentType = type.getComponentType();
            Codec<?> componentCodec = resolveCodec(componentType);

            // Create array factory: length -> new ComponentType[length]
            IntFunction<Object[]> factory = len -> (Object[]) Array.newInstance(componentType, len);

            Codec<T> arrayCodec = (Codec<T>) new ArrayCodec(componentCodec, factory);
            CODEC_REGISTRY.put(type, arrayCodec);
            return arrayCodec;
        }

        // 3. Handle Complex Objects (Recursive)
        // We put a placeholder or handle cycles carefully if needed,
        // but for now we create and register immediately.
        BuilderCodec<T> codec = createClassCodec(type);
        CODEC_REGISTRY.put(type, codec);
        return codec;
    }

    public static <T> Codec<T> resolveArrayCodec(Class<T> type) {
        if (CODEC_REGISTRY.containsKey(type)) {
            return (Codec<T>) CODEC_REGISTRY.get(type);
        }
        Class<?> componentType = type.getComponentType();
        Codec<?> componentCodec = resolveCodec(componentType);

        // Create array factory: length -> new ComponentType[length]
        IntFunction<Object[]> factory = len -> (Object[]) Array.newInstance(componentType, len);

        Codec<T> arrayCodec = (Codec<T>) new ArrayCodec(componentCodec, factory);
        CODEC_REGISTRY.put(type, arrayCodec);
        return arrayCodec;
    }

    public static <T> Codec<T> resolveSetCodec(Class<T> type, Type genericType) {

        // This gets the ACTUAL type argument (e.g., String.class)
        Type extractedArg = ((ParameterizedType) genericType).getActualTypeArguments()[0];

        return (Codec<T>) createSetCodec(resolveCodec((Class<?>) extractedArg));
    }

    public static <T> Codec<T> resolveMapCodec(Class<T> type, Type genericType) {
        // This gets the ACTUAL type arguments (e.g., String.class, Integer.class)
        Type[] extractedArgs = ((ParameterizedType) genericType).getActualTypeArguments();


        if (extractedArgs[0] != String.class) {
            throw new IllegalArgumentException("Only Maps with String keys are supported");
        }
        Codec<?> valueCodec = resolveCodec((Class<?>) extractedArgs[1]);

        return (Codec<T>) createMapCodec(valueCodec);
    }

    public static <V> MapCodec<V, LinkedHashMap<String, V>> createMapCodec(Codec<V> valueCodec) {
        return new MapCodec<>(valueCodec, LinkedHashMap::new, false);
    }

    public static <V> SetCodec<V, LinkedHashSet<V>> createSetCodec(Codec<V> valueCodec) {
        return new SetCodec<>(valueCodec, LinkedHashSet::new, false);
    }
}