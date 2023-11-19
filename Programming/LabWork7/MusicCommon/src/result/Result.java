package result;
import java.io.Serializable;
import java.util.Optional;

/**

 The Result class is a generic container class used to store the result of an operation.

 @param <T> the type of value that is returned in case of a successful operation.
 */
public class Result<T> implements Serializable {

    /**
     * A flag indicating the success of the operation.
     */
    private final boolean success;
    /**
     * A message describing the result of the operation.
     */
    private final String message;
    /**
     * The value returned in case of a successful operation.
     */
    private final T value;
    /**
     * The exception that was thrown in case of a failed operation.
     */
    private final Exception error;
    /**
     * Constructs a new instance of the Result class.
     * @param success a flag indicating the success of the operation.
     * @param message a message describing the result of the operation.
     * @param value the value returned in case of a successful operation.
     * @param error the exception that was thrown in case of a failed operation.
     */
    private Result(boolean success, String message, T value, Exception error) {
        this.success = success;
        this.message = message;
        this.value = value;
        this.error = error;
    }
    /**
     * Creates a new instance of the Result class with a success flag and a value.
     * @param value the value to be contained in the Result instance.
     * @param <T> the type of value to be contained in the Result instance.
     * @return a new instance of the Result class with a success flag and a value.
     */
    public static <T> Result<T> success(T value) {
        return new Result<T>(true, "Command executed successfully", value, null);
    }
    /**
     * Creates a new instance of the Result class with a success flag and a value.
     * @param value the value to be contained in the Result instance.
     * @param <T> the type of value to be contained in the Result instance.
     * @return a new instance of the Result class with a success flag and a value.
     */
    public static <T> Result<T> success(T value, String message) {
        return new Result<T>(true, message, value, null);
    }
    /**
     * Creates a new instance of the Result class with a failure flag and an exception.
     * @param error the exception to be contained in the Result instance.
     * @param <T> the type of value expected in the Result instance.
     * @return a new instance of the Result class with a failure flag and an exception.
     */
    public static <T> Result<T> failure(Exception error) {
        return new Result<T>(false, "An error occurred while executing the command", null, error);
    }
    /**
     * Creates a new instance of the Result class with a failure flag, a message and an exception.
     * @param error the exception to be contained in the Result instance.
     * @param message a message describing the result of the operation.
     * @param <T> the type of value expected in the Result instance.
     * @return a new instance of the Result class with a failure flag, a message and an exception.
     */
    public static <T> Result<T> failure(Exception error, String message) {
        return new Result<T>(false, message, null, error);
    }
    /**
     * Returns the success flag of the operation.
     * @return the success flag of the operation.
     */
    public boolean isSuccess() {
        return success;
    }
    /**
     * Method getValue returns the value obtained from the operation, if the operation was successful.
     * @return the value obtained from the operation, if the operation was successful.
     */
    public Optional<T> getValue() {
        return success ? Optional.ofNullable(value) : Optional.empty();
    }

    /**
     * Method getError returns the exception that was thrown in case of a failed operation.
     * @return exception that was thrown in case of a failed operation.
     */
    public Optional<Exception> getError() {
        return success ? Optional.empty() : Optional.ofNullable(error);
    }

    /**
     * Returns the message describing the result of the operation.
     * @return the message describing the result of the operation.
     */
    public String getMessage(){
        return message;
    }
}