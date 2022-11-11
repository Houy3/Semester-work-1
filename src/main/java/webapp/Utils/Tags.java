package webapp.Utils;

import exceptions.WebException;

import javax.servlet.http.HttpServletRequest;

import static webapp.Utils.Constants.ERROR_MESSAGE_NAME;
import static webapp.Utils.Constants.SUCCESS_MESSAGE_NAME;
import static webapp.Utils.Utils.notNull;
import static webapp.Utils.Utils.requestCheck;

public class Tags {

    public static String errorMessage(HttpServletRequest request) throws WebException {
        requestCheck(request);
        return "<p class=\"h4 text-danger text-center\">" + notNull((String) request.getAttribute(ERROR_MESSAGE_NAME.name())) + "</p>";
    }

    public static String successMessage(HttpServletRequest request) throws WebException {
        requestCheck(request);
        return "<p class=\"h4 text-success text-center\">" + notNull((String)request.getAttribute(SUCCESS_MESSAGE_NAME.name())) + "</p>";
    }


}
