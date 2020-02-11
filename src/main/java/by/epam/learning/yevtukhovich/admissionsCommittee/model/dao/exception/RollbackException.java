package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class RollbackException extends MySqlTransactionException {
    public RollbackException() {
    }

    public RollbackException(String message) {
        super(message);
    }

    public RollbackException(String message, Throwable cause) {
        super(message, cause);
    }

    public RollbackException(Throwable cause) {
        super(cause);
    }
}
