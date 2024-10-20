package io.github.boukenijhuis.dynamicversionurl;

public record AnnotationValues(int[] versions, String[] path) {

    public static AnnotationValues of(Object versionsAttribute, Object valueAttribute, Object pathAttribute) {
        int[] typedVersions = (int[]) versionsAttribute;
        String[] _value = (String[]) valueAttribute;
        String[] _path = (String[]) pathAttribute;

        String[] nonNullPath = returnValueOrPath(_value, _path);

        return new AnnotationValues(typedVersions, nonNullPath);
    }

    /***
     * Returns the path array if it contains values, otherwise it will return the value array.
     * @param value the value array (from a RequestMapping)
     * @param path the path array (from a RequestMapping)
     * @return the path array if it contains values, otherwise the value array
     */
    private static String[] returnValueOrPath(String[] value, String[] path) {
        if (path != null && path.length > 0) {
            return path;
        } else return value;
    }
}

