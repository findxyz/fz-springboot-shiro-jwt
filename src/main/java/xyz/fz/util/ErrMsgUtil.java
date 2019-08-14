package xyz.fz.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by fz on 2016/9/7.
 */
public class ErrMsgUtil {

    public static String getStackTrace(Exception e) {

        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw, true);
            e.printStackTrace(pw);
            return sw.toString();
        } finally {
            try {
                if (sw != null) {
                    sw.close();
                }
                if (pw != null) {
                    pw.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
