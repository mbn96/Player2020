package mbn.libs.io.json;

import java.util.ArrayList;
import java.util.Arrays;

public class MbnJSON {

    public static final int TYPE_INT = 0;
    public static final int TYPE_LONG = 1;
    public static final int TYPE_STRING = 2;
    public static final int TYPE_JSON_OBJECT = 3;
    public static final int TYPE_ARRAY_OF_ARRAY = 4;

    private interface JsonBuilder {
        void appendJsonString(StringBuilder stringBuilder);
    }

    private static void appendStringInJsonFormat(StringBuilder stringBuilder, String string) {
        stringBuilder.append('"');
        stringBuilder.append(string);
        stringBuilder.append('"');
    }

    private static Integer[] toIntArray(int[] a) {
        Integer[] out = new Integer[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i];
        }
        return out;
    }

    private static Long[] toLongArray(long[] a) {
        Long[] out = new Long[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = a[i];
        }
        return out;
    }

    public static class JsonObjectBuilder implements JsonBuilder {

        private final ArrayList<JsonPartBuilder> partBuilders = new ArrayList<>();

        public JsonObjectBuilder appendJsonPart(JsonPartBuilder jsonPartBuilder) {
            partBuilders.add(jsonPartBuilder);
            return this;
        }

        public JsonObjectBuilder putInt(String mappingKey, int value) {
            return appendJsonPart(new IntegerJsonPart(mappingKey, value));
        }

        public JsonObjectBuilder putLong(String mappingKey, long value) {
            return appendJsonPart(new LongJsonPart(mappingKey, value));
        }

        public JsonObjectBuilder putString(String mappingKey, String value) {
            return appendJsonPart(new StringJsonPart(mappingKey, value));
        }

        public JsonObjectBuilder putJsonObject(String mappingKey, JsonObjectBuilder value) {
            return appendJsonPart(new JsonObjectJsonPart(mappingKey, value));
        }

        public JsonObjectBuilder putArray(String mappingKey, int type, Object array) {
            switch (type) {
                case TYPE_INT:
                    return appendJsonPart(new JsonArrayJsonPart(mappingKey, new IntegerArray(toIntArray((int[]) array))));

                case TYPE_LONG:
                    return appendJsonPart(new JsonArrayJsonPart(mappingKey, new LongArray(toLongArray((long[]) array))));

                case TYPE_STRING:
                    return appendJsonPart(new JsonArrayJsonPart(mappingKey, new StringArray((String[]) array)));

                case TYPE_JSON_OBJECT:
                    return appendJsonPart(new JsonArrayJsonPart(mappingKey, new JsonObjectArray((JsonObjectBuilder[]) array)));

                case TYPE_ARRAY_OF_ARRAY:
                    return appendJsonPart(new JsonArrayJsonPart(mappingKey, new ArrayOfArray((JsonArrayBuilder[]) array)));

            }
            return this;
        }


        public String Build() {
            StringBuilder stringBuilder = new StringBuilder();
            appendJsonString(stringBuilder);
            return stringBuilder.toString();
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('{');
            for (int i = 0; i < partBuilders.size(); i++) {
                partBuilders.get(i).appendJsonString(stringBuilder);
                if (i != (partBuilders.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append('}');
        }
    }

    private static abstract class JsonPartBuilder implements JsonBuilder {
        final String partTitle;

        JsonPartBuilder(String partTitle) {
            this.partTitle = partTitle;
        }

        String getPartTitle() {
            return partTitle;
        }
    }

    public static class IntegerJsonPart extends JsonPartBuilder {

        private final int value;

        public IntegerJsonPart(String partTitle, int value) {
            super(partTitle);
            this.value = value;
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            appendStringInJsonFormat(stringBuilder, getPartTitle());
            stringBuilder.append(':');
            stringBuilder.append(String.valueOf(value));
        }
    }

    public static class LongJsonPart extends JsonPartBuilder {

        private final long value;

        public LongJsonPart(String partTitle, long value) {
            super(partTitle);
            this.value = value;
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            appendStringInJsonFormat(stringBuilder, getPartTitle());
            stringBuilder.append(':');
            stringBuilder.append(String.valueOf(value));
        }
    }

    public static class StringJsonPart extends JsonPartBuilder {

        private final String value;

        public StringJsonPart(String partTitle, String value) {
            super(partTitle);
            this.value = value;
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            appendStringInJsonFormat(stringBuilder, getPartTitle());
            stringBuilder.append(':');
            appendStringInJsonFormat(stringBuilder, value);
        }
    }

    public static class JsonObjectJsonPart extends JsonPartBuilder {

        private final JsonObjectBuilder value;

        public JsonObjectJsonPart(String partTitle, JsonObjectBuilder value) {
            super(partTitle);
            this.value = value;
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            appendStringInJsonFormat(stringBuilder, getPartTitle());
            stringBuilder.append(':');
            value.appendJsonString(stringBuilder);
        }
    }

    public static class JsonArrayJsonPart extends JsonPartBuilder {

        private final JsonArrayBuilder value;

        public JsonArrayJsonPart(String partTitle, JsonArrayBuilder value) {
            super(partTitle);
            this.value = value;
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            appendStringInJsonFormat(stringBuilder, getPartTitle());
            stringBuilder.append(':');
            value.appendJsonString(stringBuilder);
        }
    }

    public static abstract class JsonArrayBuilder<E> implements JsonBuilder {

        protected final ArrayList<E> array = new ArrayList<>();

        public JsonArrayBuilder() {
        }

        public JsonArrayBuilder(E[] elements) {
            array.addAll(Arrays.asList(elements));
        }

        public JsonArrayBuilder(ArrayList<? extends E> array) {
            this.array.addAll(array);
        }

        public JsonArrayBuilder<E> addElement(E element) {
            array.add(element);
            return this;
        }

        public JsonArrayBuilder<E> addElements(E[] elements) {
            array.addAll(Arrays.asList(elements));
            return this;
        }

        public JsonArrayBuilder<E> addElements(ArrayList<? extends E> elements) {
            array.addAll(elements);
            return this;
        }

        public int size() {
            return array.size();
        }

        /**
         * Convenient method to built a JSON-Array.
         *
         * @return The string representing the Array.
         */
        public String build() {
            StringBuilder builder = new StringBuilder();
            appendJsonString(builder);
            return builder.toString();
        }
    }

    public static class IntegerArray extends JsonArrayBuilder<Integer> {

        public IntegerArray() {
        }

        public IntegerArray(Integer[] elements) {
            super(elements);
        }

        public IntegerArray(ArrayList<Integer> array) {
            super(array);
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            for (int i = 0; i < array.size(); i++) {
                stringBuilder.append(String.valueOf(array.get(i)));
                if (i != (array.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append(']');
        }
    }

    public static class LongArray extends JsonArrayBuilder<Long> {

        public LongArray(Long[] elements) {
            super(elements);
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            for (int i = 0; i < array.size(); i++) {
                stringBuilder.append(String.valueOf(array.get(i)));
                if (i != (array.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append(']');
        }
    }

    public static class StringArray extends JsonArrayBuilder<String> {

        public StringArray() {
        }

        public StringArray(String[] elements) {
            super(elements);
        }

        public StringArray(ArrayList<String> array) {
            super(array);
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            for (int i = 0; i < array.size(); i++) {
                appendStringInJsonFormat(stringBuilder, array.get(i));
                if (i != (array.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append(']');
        }
    }

    public static class JsonObjectArray extends JsonArrayBuilder<JsonObjectBuilder> {

        public JsonObjectArray() {
        }

        public JsonObjectArray(JsonObjectBuilder[] elements) {
            super(elements);
        }

        public JsonObjectArray(ArrayList<JsonObjectBuilder> array) {
            super(array);
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            for (int i = 0; i < array.size(); i++) {
                array.get(i).appendJsonString(stringBuilder);
                if (i != (array.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append(']');
        }
    }

    public static class ArrayOfArray extends JsonArrayBuilder<JsonArrayBuilder> {

        public ArrayOfArray() {
        }

        public ArrayOfArray(JsonArrayBuilder[] elements) {
            super(elements);
        }

        public ArrayOfArray(ArrayList<JsonArrayBuilder> array) {
            super(array);
        }

        @Override
        public void appendJsonString(StringBuilder stringBuilder) {
            stringBuilder.append('[');
            for (int i = 0; i < array.size(); i++) {
                array.get(i).appendJsonString(stringBuilder);
                if (i != (array.size() - 1)) stringBuilder.append(',');
            }
            stringBuilder.append(']');
        }
    }
}
