package search;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final String MENU = "\n=== MENU ===\n" +
                                        "1. Find a person\n" +
                                        "2. Print all people\n" +
                                        "0. Exit.";

    public static void main(String[] args) {
        PeopleDB peopleDB = initDbTestData();
        int action = -1;
        while (action != 0) {
            System.out.println(MENU);
            action = scanner.nextInt();
            switch (action) {
                case 1:
                    find(peopleDB);
                    break;
                case 2:
                    printAll(peopleDB);
                    break;
                case 0:
                    System.out.println("\nBye!");
                    break;
                default:
                    System.out.println("\nIncorrect option! Try again.");
                    break;
            }
        }
    }

    private static void printAll(PeopleDB peopleDB) {
        System.out.println("\n=== List of people ===");
        List<String> people = peopleDB.getAll();
        people.forEach(System.out::println);
    }

    private static void find(PeopleDB peopleDB) {
        System.out.println("Select a matching strategy: ALL, ANY, NONE");
        scanner.nextLine();
        String strategy = scanner.nextLine();
        System.out.println("\nEnter a name or email to search all suitable people.");
        List<String> query = Arrays.asList(scanner.nextLine().split("\\s+"));
        List<String> people;
        switch (strategy) {
            case "ANY" :
                people = peopleDB.findAllByAny(query);
                break;
            case "ALL" :
                people = peopleDB.findAllByAll(query);
                break;
            case "NONE" :
                people = peopleDB.findAllByNone(query);
                break;
            default:
                System.out.println("Unknown strategy");
                return;
        }
        if (people.size() == 0) {
            System.out.println("\nnot found");
        } else {
            System.out.println(people.size() + " person(s) found:");
            people.forEach(System.out::println);
        }

    }
    private static PeopleDB initDbFromStandardInput () {
        System.out.println("Enter the number of people:");
        int count = scanner.nextInt();
        scanner.nextLine();
        String[] entities = new String[count];
        System.out.println("Enter all the people:");
        for (int i = 0; i < count; i++) {
            entities[i] = scanner.nextLine();
        }
        return new PeopleDB(Arrays.asList(entities));
    }
    private static PeopleDB initDbTestData () {
        String[] entities = new String[]{"Dwight Joseph djo@gmail.com",
                "Rene Webb webb@gmail.com",
                "Katie Jacobs",
                "Erick Harrington harrington@gmail.com",
                "Myrtle Medina",
                "Erick Burgess"};
        return new PeopleDB(Arrays.asList(entities));
    }
    private static PeopleDB initFromFile(String[] args)  {
        String filename = "";
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("--data")) {
                filename = args[i + 1];
            }
        }
        File file = new File(filename);
        List<String> people = new ArrayList<>();
        try (Scanner fileScanner = new Scanner(file)) {
            while (fileScanner.hasNext()) {
                people.add(fileScanner.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new PeopleDB(people);
    }
}
