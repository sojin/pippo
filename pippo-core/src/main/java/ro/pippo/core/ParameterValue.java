/*
 * Copyright (C) 2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ro.pippo.core;

import ro.pippo.core.util.StringUtils;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * A ParameterValue represents either a single-value path parameter or a
 * multi-valued query parameter.
 *
 * @author Decebal Suiu
 * @author James Moger
 */
public class ParameterValue implements Serializable {

    private final String[] values;

    public ParameterValue(final String... values) {
        this.values = values;
    }

    public boolean toBoolean() {
        return toBoolean(false);
    }

    public boolean toBoolean(boolean defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Boolean.parseBoolean(values[0]);
    }

    public byte toByte() {
        return toByte((byte) 0);
    }

    public byte toByte(byte defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Byte.parseByte(values[0]);
    }

    public short toShort() {
        return toShort((short) 0);
    }

    public short toShort(short defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Short.parseShort(values[0]);
    }

    public int toInt() {
        return toInt(0);
    }

    public int toInt(int defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Integer.parseInt(values[0]);
    }

    public long toLong() {
        return toLong(0);
    }

    public long toLong(long defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Long.parseLong(values[0]);
    }

    public float toFloat() {
        return toFloat(0);
    }

    public float toFloat(float defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Float.parseFloat(values[0]);
    }

    public double toDouble() {
        return toDouble(0);
    }

    public double toDouble(double defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return Double.parseDouble(values[0]);
    }

    public BigDecimal toBigDecimal() {
        return toBigDecimal(BigDecimal.ZERO);
    }

    public BigDecimal toBigDecimal(BigDecimal defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        double d = Double.parseDouble(values[0]);
        return new BigDecimal(d);
    }

    public UUID toUUID() {
        return toUUID(null);
    }

    public UUID toUUID(UUID defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return UUID.fromString(values[0]);
    }

    public Character toCharacter() {
        return toCharacter((char) 0);
    }

    public Character toCharacter(Character defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return values[0].charAt(0);
    }

    @Override
    public String toString() {
        return toString(null);
    }

    public String toString(String defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        return values[0];
    }

    public <T extends Enum<?>> T toEnum(Class<T> classOfT) {
        return toEnum(classOfT, null, true);
    }

    public <T extends Enum<?>> T toEnum(Class<T> classOfT, T defaultValue) {
        return toEnum(classOfT, defaultValue, true);
    }

    public <T extends Enum<?>> T toEnum(Class<T> classOfT, T defaultValue, boolean caseSensitive) {
        if (isNull()) {
            return defaultValue;
        }

        int ordinal = Integer.MIN_VALUE;
        try {
            // attempt to interpret value as an ordinal
            ordinal = Integer.parseInt(values[0]);
        } catch (Exception e) {
        }

        T[] constants = classOfT.getEnumConstants();
        for (T constant : constants) {
            if (constant.ordinal() == ordinal) {
                return constant;
            }
            if (caseSensitive) {
                if (constant.name().equals(values[0])) {
                    return constant;
                }
            } else {
                if (constant.name().equalsIgnoreCase(values[0])) {
                    return constant;
                }
            }
        }
        return defaultValue;
    }

    public Set<String> toSet() {
        return toSet(new HashSet<String>());
    }

    public Set<String> toSet(Set<String> defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (values.length == 1) {
            return new HashSet<String>(Arrays.asList(values[0].split(",")));
        }
        return new HashSet<String>(Arrays.asList(values));
    }

    public List<String> toList() {
        return toList(new ArrayList<String>());
    }

    public List<String> toList(List<String> defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        if (values.length == 1) {
            return Arrays.asList(values[0].split(","));
        }
        return Arrays.asList(values);
    }

    public Date toDate(String pattern) {
        return toDate(null, pattern);
    }

    public Date toDate(Date defaultValue, String pattern) {
        if (isNull()) {
            return defaultValue;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        try {
            Date date = dateFormat.parse(values[0]);
            return date;
        } catch (ParseException e) {
            throw new PippoRuntimeException(e);
        }
    }

    public java.sql.Date toSqlDate() {
        return toSqlDate(null);
    }

    public java.sql.Date toSqlDate(java.sql.Date defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        java.sql.Date date = java.sql.Date.valueOf(values[0]);
        return date;
    }

    public Time toSqlTime() {
        return toSqlTime(null);
    }

    public Time toSqlTime(Time defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        Time time = Time.valueOf(values[0]);
        return time;
    }

    public Timestamp toSqlTimestamp() {
        return toSqlTimestamp(null);
    }

    public Timestamp toSqlTimestamp(Timestamp defaultValue) {
        if (isNull()) {
            return defaultValue;
        }

        Timestamp timestamp = Timestamp.valueOf(values[0]);
        return timestamp;
    }

    public <T> T to(Class<T> classOfT) {
        return to(classOfT, null);
    }

    /**
     * Converts a string value(s) to the target type. You may optionally specify
     * a string pattern to assist in the type conversion.
     *
     * @param classOfT
     * @param pattern  optional pattern for interpreting the underlying request
     *                 string value. (used for date & time conversions)
     * @return an object
     */
    @SuppressWarnings("unchecked")
    public <T> T to(Class<T> classOfT, String pattern) {
        if (classOfT == null) {
            return null;
        }

        if (classOfT.isArray()) {
            Class<?> componentType = classOfT.getComponentType();
            Object array = Array.newInstance(componentType, values.length);
            // cheat by not instantiating a ParameterValue for every value
            ParameterValue parameterValue = new ParameterValue(new String[]{"PLACEHOLDER"});
            for (int i = 0; i < values.length; i++) {
                String value = values[i];
                parameterValue.values[0] = value;
                Object object = parameterValue.toObject(componentType, pattern);
                Array.set(array, i, object);
            }
            return (T) array;
        } else {
            return (T) toObject(classOfT, pattern);
        }
    }

    private Object toObject(Class<?> type, String pattern) {
        if (type == String.class) {
            return toString();
        }

        if ((type == Boolean.TYPE) || (type == Boolean.class)) {
            return toBoolean();
        }

        if ((type == Byte.TYPE) || (type == Byte.class)) {
            return toByte();
        }

        if ((type == Short.TYPE) || (type == Short.class)) {
            return toShort();
        }

        if ((type == Integer.TYPE) || (type == Integer.class)) {
            return toInt();
        }

        if ((type == Long.TYPE) || (type == Long.class)) {
            return toLong();
        }

        if ((type == Float.TYPE) || (type == Float.class)) {
            return toFloat();
        }

        if ((type == Double.TYPE) || (type == Double.class)) {
            return toDouble();
        }

        if ((type == Character.TYPE) || (type == Character.class)) {
            return toCharacter();
        }

        if (type == BigDecimal.class) {
            return toBigDecimal();
        }

        if (type == UUID.class) {
            return toUUID();
        }

        if (type.isEnum()) {
            Class<? extends Enum> enumClass = (Class<? extends Enum>) type;
            return toEnum(enumClass);
        }

        if (pattern == null) {
            // no defined pattern, use type defaults
            if (type == java.sql.Date.class) {
                return toSqlDate();
            }

            if (type == Time.class) {
                return toSqlTime();
            }

            if (type == Timestamp.class) {
                return toSqlTimestamp();
            }
        } else {
            if (Date.class.isAssignableFrom(type)) {
                Date date = toDate(pattern);
                if (type == Date.class) {
                    return date;
                } else if (type == java.sql.Date.class) {
                    return new java.sql.Date(date.getTime());
                } else if (type == Time.class) {
                    return new Time(date.getTime());
                } else if (type == Timestamp.class) {
                    return new Timestamp(date.getTime());
                }
            }
        }

        throw new PippoRuntimeException("Cannot convert '" + toString() + "'to type '" + type + "'");
    }

    public boolean isNull() {
        return values == null || values.length == 0 || values[0] == null;
    }

    public boolean isEmpty() {
        return values == null || values.length == 0 || StringUtils.isNullOrEmpty(values[0]);
    }

    public boolean isMultiValued() {
        return values != null && values.length > 1;
    }

}
