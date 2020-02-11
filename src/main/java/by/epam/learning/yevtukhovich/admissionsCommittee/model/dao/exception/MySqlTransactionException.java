package by.epam.learning.yevtukhovich.admissionsCommittee.model.dao.exception;

public class MySqlTransactionException extends Exception {
    public MySqlTransactionException() {
    }

    public MySqlTransactionException(String message) {
        super(message);
    }

    public MySqlTransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public MySqlTransactionException(Throwable cause) {
        super(cause);
    }
}
