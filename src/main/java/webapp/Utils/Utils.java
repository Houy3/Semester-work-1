package webapp.Utils;

import exceptions.WebException;

import javax.servlet.http.HttpServletRequest;

public class Utils {

    public static String notNull(String value) {
        return value != null ? value : "";
    }

    public static String notEmpty(String value) {
        return value.equals("") ? null : value;
    }


    public static void requestCheck(HttpServletRequest request) throws WebException {
        if (request == null) {
            throw new WebException("Request is empty.");
        }
    }


    public static String getParameter(HttpServletRequest request, String parameterName) throws WebException {
        requestCheck(request);
        return notNull(request.getParameter(parameterName));
    }

    public static String getAttribute(HttpServletRequest request, String attributeName) throws WebException {
        requestCheck(request);
        return notNull((String) request.getAttribute(attributeName));
    }


}
