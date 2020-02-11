package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.User;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.matcher.UrlMatcher;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sun.util.locale.provider.LocaleServiceProviderPool;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogOutCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LogOutCommand.class.getName());

    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(Parameters.USER);

        if (user != null) {
            //session.removeAttribute(Parameters.USER);
            session.invalidate();
            LOGGER.info("user: " + user.getLogin()+" was log out");
        } else {
            LOGGER.debug("could not log out: user not identified");
        }
        return Pages.REDIRECT_MAIN;
    }
}
