package elevatorsystem;

import elevatorsystem.model.Pair;

import java.util.Arrays;
import java.util.List;
import java.util.LinkedList;
import java.util.Scanner;

public class App {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the desired amount of elevators for your elevator system:");

        int numberOfElevators = scanner.nextInt();
        LinkedList<Elevator> elevators = new LinkedList<>();
        ElevatorSystem elevatorSystem = new ElevatorSystem(elevators);
        System.out.println("Enter current level for every elevator:");

        for (int i = 0; i < numberOfElevators; ++i) {
            System.out.println("Elevator " + i + ":");
            int currentLevel = scanner.nextInt();
            elevatorSystem.addElevator(currentLevel);
        }

        while (true) {
            System.out.println("Choose the activity of elevator system:");
            String activityName = scanner.next().toLowerCase().trim();
            if (activityName.equals("status")) {
                elevatorSystem.printStatus();
            }

            if (activityName.equals("pickup")) {

            }

            if (activityName.equals("step")) {
                System.out.println("Enter the number of steps the system should execute:");
                int numberOfSteps = scanner.nextInt();
                for (int i = 0; i < numberOfSteps; i++) {
                    elevatorSystem.step();
                }
            }

            if (activityName.equals("exit")) {
                System.out.println("Exiting the system... See you again! :D");
                break;
            }
        }

    }

}
