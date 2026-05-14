import java.util.Locale;

/**
 * Exception thrown when transaction amount is invalid.
 */
public class InvalidFundingAmountException extends BankException {
    public InvalidFundingAmountException(double amount) {
        super("Số tiền không hợp lệ: $" + String.format(Locale.US, "%.2f", amount));
    }
}