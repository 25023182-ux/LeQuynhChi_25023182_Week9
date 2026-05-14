import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a bank.
 */
public class Bank {
    private List<Customer> customerList;

    public Bank() {
        this.customerList = new ArrayList<>();
    }

    public List<Customer> getCustomerList() {
        return customerList;
    }

    public void setCustomerList(List<Customer> customerList) {
        if (customerList == null) {
            this.customerList = new ArrayList<>();
        } else {
            this.customerList = customerList;
        }
    }

    public void readCustomerList(InputStream inputStream) {
        if (inputStream == null) {
            return;
        }

        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(inputStream))) {
            readLines(reader);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readLines(BufferedReader reader) throws IOException {
        String line;
        Customer currentCustomer = null;

        while ((line = reader.readLine()) != null) {
            line = line.trim();

            if (line.isEmpty()) {
                continue;
            }

            Customer parsedCustomer = parseCustomer(line);
            if (parsedCustomer != null) {
                currentCustomer = parsedCustomer;
                customerList.add(currentCustomer);
            } else if (currentCustomer != null) {
                parseAccount(line, currentCustomer);
            }
        }
    }

    private Customer parseCustomer(String line) {
        int lastSpace = line.lastIndexOf(' ');
        if (lastSpace <= 0) {
            return null;
        }

        String token = line.substring(lastSpace + 1).trim();
        if (!token.matches("\\d{9}")) {
            return null;
        }

        String name = line.substring(0, lastSpace).trim();
        return new Customer(Long.parseLong(token), name);
    }

    private void parseAccount(String line, Customer customer) {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            return;
        }

        long accountNumber = Long.parseLong(parts[0]);
        double balance = Double.parseDouble(parts[2]);

        if (Account.CHECKING_TYPE.equals(parts[1])) {
            customer.addAccount(new CheckingAccount(accountNumber, balance));
        } else if (Account.SAVINGS_TYPE.equals(parts[1])) {
            customer.addAccount(new SavingsAccount(accountNumber, balance));
        }
    }

    public String getCustomersInfoByIdOrder() {
        customerList.sort(Comparator.comparingLong(Customer::getIdNumber));
        return buildCustomerInfo(customerList);
    }

    public String getCustomersInfoByNameOrder() {
        List<Customer> copy = new ArrayList<>(customerList);

        copy.sort((firstCustomer, secondCustomer) -> {
            int result =
                    firstCustomer.getFullName().compareTo(secondCustomer.getFullName());

            if (result != 0) {
                return result;
            }

            return Long.compare(
                    firstCustomer.getIdNumber(),
                    secondCustomer.getIdNumber());
        });

        return buildCustomerInfo(copy);
    }

    private String buildCustomerInfo(List<Customer> customers) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < customers.size(); i++) {
            builder.append(customers.get(i).getCustomerInfo());

            if (i < customers.size() - 1) {
                builder.append("\n");
            }
        }

        return builder.toString();
    }
}