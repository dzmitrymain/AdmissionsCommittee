package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Pages;

import javax.servlet.http.HttpServletRequest;

public class NoCommand implements Command {


    @Override
    public String execute(HttpServletRequest request, ActionType type) {

        String page = Pages.FORWARD_MAIN;

        if (type == ActionType.POST) {
            page = Pages.REDIRECT_MAIN;
        }
        return page;
    }
}
