import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Transaction {
    private String type;
    private double amount;

    public Transaction(String type, double amount) {
        this.type = type;
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public double getAmount() {
        return amount;
    }
}

class User {
    private String userId;
    private String pin;
    private double balance;
    private List<Transaction> transactionHistory;

    public User(String userId, String pin, double balance) {
        this.userId = userId;
        this.pin = pin;
        this.balance = balance;
        this.transactionHistory = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getPin() {
        return pin;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<Transaction> getTransactionHistory() {
        return transactionHistory;
    }

    public void addTransaction(String type, double amount) {
        Transaction transaction = new Transaction(type, amount);
        transactionHistory.add(transaction);
    }
}






public class ATM {
    private static User currentUser;
    private static List<User> users;

    public static void main(String[] args) {
        initializeUsers();

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter User ID: ");
        String userId = scanner.nextLine();
        System.out.print("Enter PIN: ");
        String pin = scanner.nextLine();

        if (authenticateUser(userId, pin)) {
            displayMainMenu();
            int choice;
            while ((choice = getUserChoice(scanner)) != 5) {
                switch (choice) {
                    case 1:
                        displayTransactionHistory();
                        break;
                    case 2:
                        withdrawMoney(scanner);
                        break;
                    case 3:
                        depositMoney(scanner);
                        break;
                    case 4:
                        transferMoney(scanner);
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
                displayMainMenu();
            }

            System.out.println("\nThank you for using the ATM. Goodbye!\n");
        } else {
            System.out.println("Invalid credentials. Exiting...");
        }
    }

    private static void initializeUsers() {
        users = new ArrayList<>();
        users.add(new User("123456", "1234", 5000));
        users.add(new User("987654", "4321", 10000));
    }

    private static boolean authenticateUser(String userId, String pin) {
        for (User user : users) {
            if (user.getUserId().equals(userId) && user.getPin().equals(pin)) {
                currentUser = user;
                return true;
            }
        }
        return false;
    }

    private static void displayMainMenu() {
        System.out.println("\nWhat do you want to do?");
        System.out.println("1. View Transaction History");
        System.out.println("2. Withdraw Money");
        System.out.println("3. Deposit Money");
        System.out.println("4. Transfer Money");
        System.out.println("5. Quit\n");
        System.out.print("Enter your choice: ");
    }

    private static int getUserChoice(Scanner scanner) {
        int choice = -1;
        while (choice < 1 || choice > 5) {
            System.out.print("Enter your choice (1-5): ");
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                // Invalid input, continue the loop
            }
        }
        return choice;
    }

    private static void displayTransactionHistory() {
        List<Transaction> transactionHistory = currentUser.getTransactionHistory();

        System.out.println("\nTransaction History:");
        for (Transaction transaction : transactionHistory) {
            System.out.println("Type: " + transaction.getType() + ", Amount: " + transaction.getAmount());
        }
    }

    private static void withdrawMoney(Scanner scanner) {
        System.out.print("Enter the amount to withdraw: ");
        double amount = getValidAmount(scanner);

        if (amount > 0 && amount <= currentUser.getBalance()) {
            currentUser.addTransaction("Withdraw", amount);
            double newBalance = currentUser.getBalance() - amount;
            currentUser.setBalance(newBalance);
            System.out.println("\nWithdrawal successful.");
        } else {
            System.out.println("Sorry, insufficient balance or invalid amount.");
        }
    }

    private static void depositMoney(Scanner scanner) {
        System.out.print("Enter the amount to deposit: ");
        double amount = getValidAmount(scanner);

        if (amount > 0) {
            currentUser.addTransaction("Deposit", amount);
            double newBalance = currentUser.getBalance() + amount;
            currentUser.setBalance(newBalance);
            System.out.println("\nDeposit successful.");
        } else {
            System.out.println("Invalid amount.");
        }
    }

    private static void transferMoney(Scanner scanner) {
        System.out.print("Enter the recipient's User ID: ");
        String recipientId = scanner.nextLine();

        User recipient = findUserById(recipientId);

        if (recipient != null) {
            System.out.print("Enter the amount to transfer: ");
            double amount = getValidAmount(scanner);

            if (amount > 0 && amount <= currentUser.getBalance()) {
                currentUser.addTransaction("Transfer to " + recipientId, amount);
                double senderNewBalance = currentUser.getBalance() - amount;
                currentUser.setBalance(senderNewBalance);

                recipient.addTransaction("Transfer from " + currentUser.getUserId(), amount);
                double recipientNewBalance = recipient.getBalance() + amount;
                recipient.setBalance(recipientNewBalance);
                System.out.println("\nTransfer successful.");
            } else {
                System.out.println("Sorry, insufficient balance or invalid amount.");
            }
        } else {
            System.out.println("Recipient with User ID " + recipientId + " not found.");
        }
    }

    private static double getValidAmount(Scanner scanner) {
        double amount = -1;
        while (amount < 0) {
            try {
                amount = Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Enter a valid amount: ");
            }
        }
        return amount;
    }

    private static User findUserById(String userId) {
        for (User user : users) {
            if (user.getUserId().equals(userId)) {
                return user;
            }
        }
        return null;
    }
}
