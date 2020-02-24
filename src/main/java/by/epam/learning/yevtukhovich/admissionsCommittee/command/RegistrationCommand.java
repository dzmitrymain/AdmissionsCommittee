package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator.UserDataValidator;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.UserService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class RegistrationCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(RegistrationCommand.class.getName());


    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.REDIRECT_REGISTRATION;
        if (type == ActionType.POST) {
            HttpSession session = request.getSession();

            String login = request.getParameter(Parameters.LOGIN);
            String password = request.getParameter(Parameters.PASSWORD);
            String repeatPassword = request.getParameter(Parameters.REPEAT_PASSWORD);
            String firstName = request.getParameter(Parameters.FIRST_NAME);
            String lastName = request.getParameter(Parameters.LAST_NAME);
            String patronymic = request.getParameter(Parameters.PATRONYMIC);
            String userRole = request.getParameter(Parameters.USER_ROLE);

            List<String> errorMessages = UserDataValidator.validateRegistrationForm(login, password, repeatPassword, lastName, firstName, patronymic, userRole);
            if (errorMessages == null) {

                User newUser = new User(UserRole.valueOf(userRole.toUpperCase()), login, password.hashCode(), firstName, lastName, patronymic);
                UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);

                try {
                    Optional<Integer> newUserId = userService.addUser(newUser);
                    if (newUserId.isPresent()) {
                        newUser.setUserId(newUserId.get());
                            if (session.getAttribute(Parameters.USER) != null) {
                                session.invalidate();
                            }
                            session.setAttribute(Parameters.USER, newUser);
                            LOGGER.info("new user has been registered and log in: " + newUser);
                            page = Pages.REDIRECT_PERSONAL_SETTINGS;
                    } else {
                        LOGGER.debug(Messages.LOGIN_NOT_UNIQUE);
                        session.setAttribute(Parameters.ERROR, Messages.LOGIN_NOT_UNIQUE);
                    }
                } catch (UserServiceException e) {
                    LOGGER.error(e.getMessage());
                    request.getSession().setAttribute(Parameters.ERROR, Messages.INTERNAL_ERROR);
                    page = Pages.REDIRECT_ERROR_PAGE;
                }
            } else {
                LOGGER.debug(errorMessages);
                session.setAttribute(Parameters.ERRORS, errorMessages);
            }
        } else {
            page = Pages.FORWARD_REGISTRATION;
        }
        return page;
    }
}
