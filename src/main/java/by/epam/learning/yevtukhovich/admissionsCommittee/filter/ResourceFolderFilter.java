package by.epam.learning.yevtukhovich.admissionsCommittee.filter;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResourceFolderFilter implements Filter {

    private static final Logger LOGGER = LogManager.getLogger(ResourceFolderFilter.class.getName());

    private String contextPath;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        contextPath = filterConfig.getServletContext().getContextPath();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        User user = (User) request.getSession().getAttribute(Parameters.USER);

        if (user != null && user.getRole() == UserRole.ADMIN) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            LOGGER.warn("non-admin user tried to enter resource folder");
            response.sendRedirect(contextPath + "/" + Pages.REDIRECT_MAIN);
        }
    }

    @Override
    public void destroy() {

    }
}
