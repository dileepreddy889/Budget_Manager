package budget;

import java.io.*;
import java.util.*;

public class Main {
    private static double balance = 0.0;
    private static final Map<String, List<Purchase>> purchases = new HashMap<>();

    static {
        purchases.put("Food", new ArrayList<>());
        purchases.put("Clothes", new ArrayList<>());
        purchases.put("Entertainment", new ArrayList<>());
        purchases.put("Other", new ArrayList<>());
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Choose your action:");
            System.out.println("1) Add income");
            System.out.println("2) Add purchase");
            System.out.println("3) Show list of purchases");
            System.out.println("4) Balance");
            System.out.println("5) Save");
            System.out.println("6) Load");
            System.out.println("7) Analyze (Sort)");
            System.out.println("0) Exit");

            int action = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over
            System.out.println();

            switch (action) {
                case 1:
                    addIncome(scanner);
                    break;
                case 2:
                    addPurchase(scanner);
                    break;
                case 3:
                    showListOfPurchases(scanner);
                    break;
                case 4:
                    showBalance();
                    break;
                case 5:
                    savePurchases();
                    break;
                case 6:
                    loadPuchase();
                    break;
                case 7:
                    analyzeExpenses(scanner);
                    break;
                case 0:
                    System.out.println("Bye!");
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    System.out.println();
                    break;
            }
        }
    }

    private static void analyzeExpenses(Scanner scanner) {
        while (true) {
            System.out.println("How do you want to sort?");
            System.out.println("1) Sort all purchases");
            System.out.println("2) Sort by type");
            System.out.println("3) Sort certain type");
            System.out.println("4) Back");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over
            System.out.println();

            switch (choice) {
                case 1:
                    sortAllpurchases();
                    break;
                case 2:
                    sortByType(scanner);
                    break;
                case 3:
                    sortCertainType(scanner);
                    break;
                case 4:
                    return;
                default:
                    System.out.println("Invalid choice! Please try again.");
                    System.out.println();
                    break;
            }
        }
    }

    private static void sortCertainType(Scanner scanner) {
        System.out.println("Choose the type of purchase");
        System.out.println("1) Food");
        System.out.println("2) Clothes");
        System.out.println("3) Entertainment");
        System.out.println("4) Other");

        int choice = scanner.nextInt();
        scanner.nextLine();  // Consume newline left-over
        System.out.println();

        String category = getCategory(choice);
        if (category != null) {
            List<Purchase> purchaseList = purchases.get(category);
            if (purchaseList.isEmpty()) {
                System.out.println("The purchase list is empty!\n");
                return;
            }

            purchaseList.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));

            System.out.println(category + ":");
            double total = 0;
            for (Purchase purchase : purchaseList) {
                System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
                total += purchase.getPrice();
            }
            System.out.printf("Total sum: $%.2f%n\n", total);
        } else {
            System.out.println("Invalid category! Please try again.\n");
        }
    }

    private static void sortByType(Scanner scanner) {
        Map<String, Double> totalsByCategory = new HashMap<>();
        double totalSum = 0;

        for (Map.Entry<String, List<Purchase>> entry : purchases.entrySet()) {
            double total = 0;
            for (Purchase purchase : entry.getValue()) {
                total += purchase.getPrice();
            }
            totalsByCategory.put(entry.getKey(), total);
            totalSum += total;
        }

        totalsByCategory.entrySet().stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .forEach(e -> System.out.printf("%s - $%.2f%n", e.getKey(), e.getValue()));

        System.out.printf("Total sum: $%.2f%n\n", totalSum);
    }

    private static void sortAllpurchases() {
        List<Purchase> allPurchases = new ArrayList<>();
        for (List<Purchase> purchaseList : purchases.values()) {
            allPurchases.addAll(purchaseList);
        }

        if (allPurchases.isEmpty()) {
            System.out.println("The purchase list is empty!\n");
            return;
        }

        allPurchases.sort((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()));

        System.out.println("All:");
        double total = 0;
        for (Purchase purchase : allPurchases) {
            System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
            total += purchase.getPrice();
        }
        System.out.printf("Total: $%.2f%n\n", total);
    }

    private static void loadPuchase() throws IOException {
        try(BufferedReader reader = new BufferedReader(new FileReader("purchases.txt"))) {
            String line = reader.readLine();
            if (line != null) {
                balance = Double.parseDouble(line);
            }
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 3) {
                    String category = parts[0];
                    String name = parts[1];
                    double price = Double.parseDouble(parts[2]);
                    purchases.get(category).add(new Purchase(name,price));
                }
            }
            System.out.println("Purchases were loaded!\n");
        }
    }

    private static void savePurchases() throws IOException {
        try(PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("purchases.txt")))) {
            writer.println(balance);
            for (Map.Entry<String , List<Purchase>> entry: purchases.entrySet()) {
                String category = entry.getKey();
                for (Purchase purchase : entry.getValue()) {
                    writer.printf("%s;%s;%.2f%n", category, purchase.getName(), purchase.getPrice());
                }
            }
            writer.flush();
            System.out.println("Purchases were saved!\n");
        }
    }

    private static void addIncome(Scanner scanner) {
        System.out.println("Enter income:");
        balance += scanner.nextDouble();
        scanner.nextLine();  // Consume newline left-over
        System.out.println("Income was added!\n");
    }

    private static void addPurchase(Scanner scanner) {
        while (true) {
            System.out.println("Choose the type of purchase");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) Back");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over
            System.out.println();

            if (choice == 5) {
                break;
            }

            String category = getCategory(choice);
            if (category != null) {
                System.out.println("Enter purchase name:");
                String name = scanner.nextLine();
                System.out.println("Enter its price:");
                double price = scanner.nextDouble();
                scanner.nextLine();  // Consume newline left-over

                balance -= price;
                purchases.get(category).add(new Purchase(name, price));
                System.out.println("Purchase was added!\n");
            } else {
                System.out.println("Invalid category! Please try again.\n");
            }
        }
    }

    private static void showListOfPurchases(Scanner scanner) {
        while (true) {
            System.out.println("Choose the type of purchases");
            System.out.println("1) Food");
            System.out.println("2) Clothes");
            System.out.println("3) Entertainment");
            System.out.println("4) Other");
            System.out.println("5) All");
            System.out.println("6) Back");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline left-over
            System.out.println();

            if (choice == 6) {
                break;
            }

            if (choice == 5) {
                showAllPurchases();
            } else {
                String category = getCategory(choice);
                if (category != null) {
                    showPurchasesByCategory(category);
                } else {
                    System.out.println("Invalid choice! Please try again.\n");
                }
            }
        }
    }

    private static void showBalance() {
        System.out.printf("Balance: $%.2f%n\n", balance);
    }

    private static void showPurchasesByCategory(String category) {
        List<Purchase> purchaseList = purchases.get(category);

        if (purchaseList.isEmpty()) {
            System.out.println(category + ":");
            System.out.println("The purchase list is empty!\n");
        } else {
            double total = 0;
            System.out.println(category + ":");
            for (Purchase purchase : purchaseList) {
                System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
                total += purchase.getPrice();
            }
            System.out.printf("Total sum: $%.2f%n\n", total);
        }
    }

    private static void showAllPurchases() {
        double total = 0;
        boolean hasPurchases = false;

        for (String category : purchases.keySet()) {
            List<Purchase> purchaseList = purchases.get(category);

            if (!purchaseList.isEmpty()) {
                hasPurchases = true;
                System.out.println(category + ":");
                for (Purchase purchase : purchaseList) {
                    System.out.printf("%s $%.2f%n", purchase.getName(), purchase.getPrice());
                    total += purchase.getPrice();
                }
            }
        }

        if (!hasPurchases) {
            System.out.println("The purchase list is empty!\n");
        } else {
            System.out.printf("Total sum: $%.2f%n\n", total);
        }
    }

    private static String getCategory(int choice) {
        switch (choice) {
            case 1:
                return "Food";
            case 2:
                return "Clothes";
            case 3:
                return "Entertainment";
            case 4:
                return "Other";
            default:
                return null;
        }
    }

    static class Purchase {
        private final String name;
        private final double price;

        public Purchase(String name, double price) {
            this.name = name;
            this.price = price;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }
    }
}
