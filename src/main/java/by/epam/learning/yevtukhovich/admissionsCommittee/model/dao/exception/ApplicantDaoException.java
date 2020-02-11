package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class ApplicantDaoException extends DaoException {
    public ApplicantDaoException() {
    }

    public ApplicantDaoException(String message) {
        super(message);
    }

    public ApplicantDaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicantDaoException(Throwable cause) {
        super(cause);
    }
}
