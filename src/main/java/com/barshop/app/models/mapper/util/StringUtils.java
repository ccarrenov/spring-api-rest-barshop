package com.barshop.app.models.mapper.util;

public final class StringUtils {

    private StringUtils() throws InstantiationException {
        throw new InstantiationException("You can't create new instance of StringUtils.");
    }

    public static String concat( Object... objects ) {
        return concatSeparate("", objects);
    }

    public static String concatSeparate( String separate, Object... objects ) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Object obj : objects) {
            if (obj != null) {
                sb.append(obj);
                if (i++ <= objects.length - 1) {
                    sb.append(separate);
                }
            }
        }
        return sb.toString();
    }
    
    public static String format( String text, Object... values ) {
        return formatRegex(text, '%', values);
    }    

    public static String formatRegex( String text, char regex, Object... values ) {
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == regex) {
                if (j < values.length) {
                    sb.append(values[j]);
                } else {
                    sb.append(text.charAt(i));
                }
                j++;
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
}
