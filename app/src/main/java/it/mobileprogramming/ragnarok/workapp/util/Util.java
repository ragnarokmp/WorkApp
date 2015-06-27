package it.mobileprogramming.ragnarok.workapp.util;

import android.content.Context;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;

/**
 * Util class.
 * @author pincopallino93
 * @version 1.1
 */
public class Util {

    /**
     * Static context.
     */
    private static Context context = null;

    /**
     * Method that allow to set context with the actual context.
     * @param context the context to be "store".
     */
    public static void setContext(Context context) {
        Util.context = context;
    }

    /**
     * Method that allow to retrieve the context "stored".
     * @return the context "stored".
     */
    public static Context getContext() {
        return Util.context;
    }

    /**
     * Method that bolds text into some tokes.
     * @param text the text with tokens.
     * @param token the tokens used in the text.
     * @return the text without tokens but with bold text
     */
    public static CharSequence boldTextBetweenTokens(CharSequence text, String token) {
        int tokenLen = token.length();
        int start = text.toString().indexOf(token) + tokenLen;
        int end = text.toString().indexOf(token, start);

        while (start > -1 && end > -1) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
            spannableStringBuilder.setSpan(new StyleSpan(Typeface.BOLD), start, end, 0);

            // Delete token before and after
            spannableStringBuilder.delete(end, end + tokenLen);
            spannableStringBuilder.delete(start - tokenLen, start);
            text = spannableStringBuilder;

            start = text.toString().indexOf(token, end - tokenLen - tokenLen) + tokenLen;
            end = text.toString().indexOf(token, start);
        }
        return text;
    }
}
