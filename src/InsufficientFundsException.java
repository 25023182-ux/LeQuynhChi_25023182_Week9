import java.util.Locale;

/**
 * Exception thrown when account balance is insufficient.
 */
public class InsufficientFundsException extends BankException {
    public InsufficientFundsException(double amount) {
        super(
                "Số dư tài khoản không đủ $"
                        + String.format(Locale.US, "%.2f", amount)
                        + " để thực hiện giao dịch");
    }
}