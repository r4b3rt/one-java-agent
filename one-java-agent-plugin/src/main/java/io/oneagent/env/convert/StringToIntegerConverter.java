
package io.oneagent.env.convert;

final class StringToIntegerConverter implements Converter<String, Integer> {
    @Override
    public Integer convert(String source, Class<Integer> targetType) {
        return Integer.parseInt(source);
    }
}
