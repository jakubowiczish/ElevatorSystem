package elevatorsystem;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * Class that contains the list of elevators, as well as methods responsible
 * for basic operations on this system
 *
 * @see Elevator
 */
public class ElevatorSystem {
    private LinkedList<Elevator> elevators;


    ElevatorSystem(LinkedList<Elevator> elevators) {
        this.elevators = elevators;
    }


    /**
     * Adds elevator to the list of elevators
     *
     * @param currentLevel the current level of elevator
     *                     that is to be added to list of elevators
     * @see Elevator
     */
    void addElevator(int currentLevel) {
        elevators.add(new Elevator(elevators.size(), currentLevel));
    }


    /**
     * Returns Pair<Integer, Integer> that contains elevatorId of elevator
     * which needs the least amount of steps to be able to do the pickup for the given floor,
     * and amount of these steps for this specific elevator
     *
     * @param floor floor number for the pickup activity
     * @return Pair that contains id of the most efficient elevator
     * - elevator that needs the least amount of steps
     * to be able to do the pickup for the given floor as well as the number of these steps
     * @see Elevator
     */
    private Pair<Integer, Integer> getElevatorAndSteps(int floor) {
        int numberOfSteps = Integer.MAX_VALUE;
        int elevatorId = -1;

        LinkedList<Elevator> elevatorsCopy = new LinkedList<>();

        for (int i = 0; i < elevators.size(); ++i) {
            elevatorsCopy.add(i, elevators.get(i).clone());
        }

        for (Elevator elevator : elevatorsCopy) {
            int pickupTime = elevator.pickup(floor, 0, false);

            if (pickupTime < numberOfSteps) {
                numberOfSteps = pickupTime;
                elevatorId = elevator.getElevatorId();
            }
        }

        return new Pair<>(elevatorId, numberOfSteps);
    }


    /**
     * Does the pickup operation for the given floor, and for the destination
     *
     * @param floor  floor number for the pickup operation
     * @param offset the direction of the elevator and the distance to overcome in one
     *               e.g. when offset is equal to -3 it means that elevator is to move 3 floors downwards
     * @see Elevator
     */
    void pickup(int floor, int offset) {
        Pair<Integer, Integer> bestElevatorStatus = getElevatorAndSteps(floor);
        int elevatorId = bestElevatorStatus.getFirst();

        System.out.println("Id of elevator that is responsible for this pickup: " + elevatorId);

        elevators.get(elevatorId).pickup(floor, 0, false);
        elevators.get(elevatorId).pickup(floor, offset, true);
    }


    /**
     * Returns list of generated statuses for every elevator in the system
     *
     * @return List of statuses for every elevator in the system
     * @see Elevator
     * @see Elevator.ElevatorStatus
     */
    private List<Elevator.ElevatorStatus> status() {
        return elevators.stream().map(e -> e.generateStatus()).collect(Collectors.toList());
    }


    /**
     * Prints the status of elevator system
     */
    void printStatus() {
        status().forEach(e -> System.out.println(e));
    }


    /**
     * Does the step for every elevator in the list of elevators
     */
    void step() {
        elevators.forEach(e -> e.step());
    }
}
