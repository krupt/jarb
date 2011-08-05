package org.jarb.violation;

import static org.springframework.util.Assert.state;

/**
 * Thrown whenever a unique key value already exists.
 * 
 * @author Jeroen van Schagen
 * @since 27-05-2011
 */
public class UniqueKeyViolationException extends ConstraintViolationException {
    private static final long serialVersionUID = -5771965201756493983L;

    /**
     * Construct a new {@link UniqueKeyViolationException}.
     * @param violation constraint violation that triggered this exception
     */
    public UniqueKeyViolationException(ConstraintViolation violation) {
        this(violation, (Throwable) null);
    }

    /**
     * Construct a new {@link UniqueKeyViolationException}.
     * @param violation constraint violation that triggered this exception
     * @param message exception message that should be shown
     */
    public UniqueKeyViolationException(ConstraintViolation violation, String message) {
        this(violation, message, null);
    }

    /**
     * Construct a new {@link UniqueKeyViolationException}.
     * @param violation constraint violation that triggered this exception
     * @param cause the cause of this constraint violation exception, can be {@code null}
     */
    public UniqueKeyViolationException(ConstraintViolation violation, Throwable cause) {
        this(violation, "Unique key '" + violation.getConstraintName() + "' was violated.", cause);
    }

    /**
     * Construct a new {@link UniqueKeyViolationException}.
     * @param violation constraint violation that triggered this exception
     * @param message exception message that should be shown
     * @param cause the cause of this constraint violation exception, can be {@code null}
     */
    public UniqueKeyViolationException(ConstraintViolation violation, String message, Throwable cause) {
        super(violation, message, cause);
        state(violation.getType() == ConstraintViolationType.UNIQUE_KEY, "Unique key exception can only occur for unique key violations");
    }
}
