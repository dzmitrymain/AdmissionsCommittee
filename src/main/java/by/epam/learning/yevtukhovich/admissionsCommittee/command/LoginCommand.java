package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.UserService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.matcher.UrlMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements Command {

    private static final Logger LOGGER=LogManager.getLogger(LoginCommand.class.getName());


    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));

        HttpSession session = request.getSession();
        String login = request.getParameter(Parameters.LOGIN);
        String password = request.getParameter(Parameters.PASSWORD);

        LOGGER.debug("login " + login + " password: " + password);

        if (!password.equals("") && !login.equals("")) {
            UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
            userService.takeConnection();
            User user;
            try {
                user = userService.login(login);

                if (user != null) {
                    LOGGER.debug("found user: " + user);
                    long passwordHash = user.getPassword();

                    if (passwordHash == password.hashCode()) {
                        LOGGER.debug("successful login as "+user.getRole());
                        session.setAttribute(Parameters.USER, user);
                    } else {
                        LOGGER.info("incorrect password");
                        session.setAttribute(Parameters.LOGIN_ERROR, Messages.INCORRECT_PASSWORD);
                    }
                } else {
                    LOGGER.info("incorrect login");
                    session.setAttribute(Parameters.LOGIN_ERROR, Messages.INCORRECT_LOGIN);
                }
            } catch (UserServiceException e) {
                LOGGER.error(e.getMessage());
                session.setAttribute(Parameters.LOGIN_ERROR,Messages.INTERNAL_ERROR);
            }finally {
                userService.releaseConnection();
            }
        } else {
            LOGGER.info("field not filled");
            session.setAttribute(Parameters.LOGIN_ERROR, Messages.FIELDS_NOT_FILLED);
        }
        return page;
    }
}