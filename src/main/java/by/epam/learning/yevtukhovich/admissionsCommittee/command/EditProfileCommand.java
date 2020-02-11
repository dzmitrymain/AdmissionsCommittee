package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.validator.UserDataValidator;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.UserService;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.exception.UserServiceException;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceFactory;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory.ServiceType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Messages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.matcher.UrlMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public class EditProfileCommand implements Command {
    @Override
    public String execute(HttpServletRequest request, ActionType actionType) {
        String page = UrlMatcher.matchUrl(request.getHeader(Parameters.REFERER));
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Parameters.USER);
        if (actionType == ActionType.GET) {
            page = Pages.FORWARD_EDIT_PROFILE;
        } else {
            String password = request.getParameter(Parameters.PASSWORD);
            if (password != null && password.hashCode() == user.getPassword()) {

                User userUpdate = new User();
                String firstName = request.getParameter(Parameters.FIRST_NAME);
                String lastName = request.getParameter(Parameters.LAST_NAME);
                String patronymic = request.getParameter(Parameters.PATRONYMIC);
                List<String> errorMessages = UserDataValidator.validateEditingForm(lastName, firstName, patronymic);
                if (errorMessages == null) {
                    userUpdate.setUserId(user.getUserId());
                    userUpdate.setFirstName(firstName);
                    userUpdate.setLastName(lastName);
                    userUpdate.setPatronymic(patronymic);
                    UserService userService = (UserService) ServiceFactory.getService(ServiceType.USER_SERVICE);
                    userService.takeConnection();
                    try {
                        userService.editUser(userUpdate);
                        user.setLastName(lastName);
                        user.setFirstName(firstName);
                        user.setPatronymic(patronymic);
                        session.setAttribute(Parameters.USER,user);
                        page = Pages.REDIRECT_PERSONAL_SETTINGS;
                    } catch (UserServiceException e) {
                        e.printStackTrace();
                    } finally {
                        userService.releaseConnection();
                    }
                } else {
                    session.setAttribute(Parameters.ERRORS, errorMessages);
                }

            } else {
                session.setAttribute(Parameters.ERROR, Messages.INCORRECT_PASSWORD);
            }
        }
        return page;
    }
}
