package by.epam.learning.yevtukhovich.admissionsCommittee.filter;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.Enrollment;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.EnrollmentService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.EnrollmentServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.jvm.hotspot.debugger.Page;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class EnrollmentStateFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(EnrollmentStateFilter.class.getName());
    private EnrollmentService enrollmentService;
    private String contextPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
        enrollmentService = (EnrollmentService) ServiceFactory.getService(ServiceType.ENROLLMENT_SERVICE);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        try {
            enrollmentService.takeConnection();
            Enrollment latestEnrollment = enrollmentService.getLatestEnrollment();
            if (latestEnrollment != null) {
                servletRequest.setAttribute(Parameters.ENROLLMENT, latestEnrollment);
            } else {
                LOGGER.warn("there was no one enrollment");
            }
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (EnrollmentServiceException e) {
            LOGGER.error(e.getMessage());
            response.sendRedirect(contextPath + "/" + Pages.REDIRECT_ERROR_PAGE);
            session.setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
        } finally {
            enrollmentService.releaseConnection();
        }
    }

    @Override
    public void destroy() {

    }
}
