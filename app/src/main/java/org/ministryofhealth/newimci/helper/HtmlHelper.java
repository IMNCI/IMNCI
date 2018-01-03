package org.ministryofhealth.newimci.helper;

import android.os.Build;
import android.text.Html;
import android.text.Spanned;

/**
 * Created by chriz on 9/26/2017.
 */

public class HtmlHelper {
    public static Spanned parseHTML(String text){
        Spanned parsedHTML = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            parsedHTML = Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT);
        }else{
            parsedHTML = Html.fromHtml(text);
        }
        return parsedHTML;
    }
}
