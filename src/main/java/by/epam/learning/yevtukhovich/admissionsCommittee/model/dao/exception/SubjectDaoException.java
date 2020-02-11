package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class SubjectDaoException extends DaoException {
    public SubjectDaoException() {
    }

    public SubjectDaoException(String message) {
        super(message);
    }

    public SubjectDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubjectDaoException(Throwable cause) {
        super(cause);
    }
}
