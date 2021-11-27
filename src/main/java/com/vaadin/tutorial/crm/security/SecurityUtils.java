package com.vaadin.tutorial.crm.security;

import com.vaadin.flow.server.HandlerHelper;
import com.vaadin.flow.shared.ApplicationConstants;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.context.SecurityContextHolder;
import javax.servlet.http.HttpServletRequest;
import java.util.stream.Stream;

@UtilityClass
public class SecurityUtils {
    public UserAuthentication getAuthentication() {
        return (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
    }
    static boolean isFrameworkInternalRequest(HttpServletRequest request) {

        final String parameterValue = request.getParameter
                (ApplicationConstants.REQUEST_TYPE_PARAMETER);
        return parameterValue != null
                && Stream.of(HandlerHelper.RequestType.values())
                .anyMatch(r -> r.getIdentifier().equals(parameterValue));
    }

}
