package com.barshop.app.models.mapper.util;


public final class StringUtils {

    private StringUtils() throws InstantiationException {
        throw new InstantiationException("You can't create new instance of StringUtils.");
    }

    public static String concat( Object... objects ) {
        StringBuilder sb = new StringBuilder();

        for (Object obj : objects) {
            if (obj != null) {
                sb.append(" ").append(obj);
            }
        }
        return sb.toString();
    }

    public static String replaceValues( String text, char regex, Object... values ) {
        StringBuilder sb = new StringBuilder();
        int j = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == regex) {
                sb.append(values[j]);
                j++;
            } else {
                sb.append(text.charAt(i));
            }
        }
        return sb.toString();
    }
}
