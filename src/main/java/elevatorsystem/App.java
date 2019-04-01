package elevatorsystem;

import elevatorsystem.utils.Helper;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * Class with the main method
 */
public class App {
    public static void main(String[] args) {
        Helper.printIntroductionToTheProgram();

        Scanner scanner = new Scanner(System.in);
        System.out.println("\nEnter the desired amount of elevators for your elevator system:");

        int numberOfElevators = scanner.nextInt();

        LinkedList<Elevator> elevators = new LinkedList<>();
        ElevatorSystem elevatorSystem = new ElevatorSystem(elevators);

        System.out.println("Enter current level for every elevator:");

        for (int i = 0; i < numberOfElevators; ++i) {
            System.out.println("Elevator " + i + ":");
            int currentLevel = scanner.nextInt();

            if (currentLevel < 0) {
                System.out.println("Elevators should be above ground!");
                System.out.println("Elevator " + i + ":");
                currentLevel = scanner.nextInt();
            }
            elevatorSystem.addElevator(currentLevel);
        }

        while (true) {
            System.out.println("Choose the activity for the elevator system:");
            String activityName = scanner.next().toLowerCase().trim();
            if (activityName.equals("status")) {
                elevatorSystem.printStatus();
            }

            if (activityName.equals("pickup")) {
                System.out.println("Enter the floor for a pickup:");
                int pickupFloor = scanner.nextInt();

                if (pickupFloor < 0) {
                    System.out.println("The floor should be positive integer!");
                    pickupFloor = scanner.nextInt();
                }

                System.out.println("Enter the direction e.g -2 for 2 floors down, 3 for 3 floors up");
                int direction = scanner.nextInt();

                elevatorSystem.pickup(pickupFloor, direction);
            }

            if (activityName.equals("step")) {
                System.out.println("Enter the number of steps the system should execute:");
                int numberOfSteps = scanner.nextInt();
                for (int i = 0; i < numberOfSteps; i++) {
                    elevatorSystem.step();
                }
            }

            if (activityName.equals("help")) {
                Helper.printHelp();
            }

            if (activityName.equals("exit")) {
                System.out.println("Exiting the system... See you again! :D");
                break;
            }
        }
    }
}
