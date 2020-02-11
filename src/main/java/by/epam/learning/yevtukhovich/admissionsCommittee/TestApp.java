package by.epam.learning.yevtukhovich.admissionsCommittee;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.entity.role.UserRole;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.configuration.ConfigurationData;
import by.epam.learning.yevtukhovich.admissionsCommittee.util.matcher.UrlMatcher;

import javax.servlet.http.HttpServletRequest;

public class TestApp {

    public static void main(String[] args) {

//        String test = "http://localhost:8080/AdmissionsCommittee/Committee";
//
//        String result = UrlMatcher.matchUrl(test);
//
//        System.out.println(test.replaceFirst(UrlMatcher.regex,test) );
//
//        System.out.println(result);

        System.out.println(("Jdd kd ldakl Ldda".matches("[A-Z][a-z]{1,20}(\\s?[A-Za-z][a-z]{1,20})*")));



    }

    static class TestInfo {

        static void info(HttpServletRequest request) {

            System.out.println("===========================");
            System.out.println(request.getHeader("referer"));

            System.out.println(request.getRequestURL().toString());

            // Getting servlet request query string.
            System.out.println(request.getQueryString());

            // Getting request information without the hostname.
            System.out.println(request.getRequestURI());

            // Below we extract information about the request object path
            // information.
            System.out.println(request.getScheme());
            System.out.println(request.getServerName());
            System.out.println(request.getServerPort());
            System.out.println(request.getContextPath());
            System.out.println(request.getServletPath());
            System.out.println(request.getPathInfo());
            System.out.println(request.getQueryString());
            System.out.println("===========================");
//        ${pageContext.request.servletPath}
        }
    }
}
