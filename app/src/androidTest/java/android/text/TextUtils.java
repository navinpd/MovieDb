package android.text;

public class TextUtils {
    public static boolean join(CharSequence str, Iterable iterable) {
        return str == null || str.length() == 0;
    }
}
