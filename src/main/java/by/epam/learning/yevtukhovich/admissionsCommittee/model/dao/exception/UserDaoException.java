package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class UserDaoException extends DaoException{

    public UserDaoException() {
    }

    public UserDaoException(String message) {
        super(message);
    }

    public UserDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserDaoException(Throwable cause) {
        super(cause);
    }
}
