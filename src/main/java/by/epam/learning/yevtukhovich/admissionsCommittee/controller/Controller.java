package by.epam.learning.yevtukhovich.admissionsCommittee.controller;

import by.epam.learning.yevtukhovich.admissionsCommittee.command.Command;
import by.epam.learning.yevtukhovich.admissionsCommittee.command.CommandManager;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.Parameters;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class Controller extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp, ActionType.GET);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doProcess(req, resp, ActionType.POST);
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response, ActionType type) {

        String commandName = request.getParameter(Parameters.COMMAND);
        Command command = CommandManager.getCommand(commandName);
        // logger
        String page = command.execute(request, type);

        try {
            if (type == ActionType.GET) {
                request.getRequestDispatcher(page).forward(request, response);
            } else {
                response.sendRedirect(request.getContextPath() + "/" + page);
            }
        } catch (ServletException | IOException e) {
            //logger
            e.printStackTrace();
        }
    }

    @Override
    public void destroy() {
        super.destroy();
    }
}
