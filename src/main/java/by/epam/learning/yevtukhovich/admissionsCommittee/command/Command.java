package by.epam.learning.yevtukhovich.admissionsCommittee.command;

import by.epam.learning.yevtukhovich.admissionsCommittee.controller.ActionType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public interface Command {

    String execute(HttpServletRequest request, ActionType actionType);
}
