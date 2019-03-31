package elevatorsystem;

import elevatorsystem.model.Pair;

import javax.swing.text.AbstractDocument;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSystem {
    private LinkedList<Elevator> elevators;

    public ElevatorSystem(LinkedList<Elevator> elevators) {
        this.elevators = elevators;
    }

    List<Elevator.ElevatorStatus> status() {
        return elevators.stream().map(e -> e.generateStatus()).collect(Collectors.toList());
    }

    int addElevator(int currentLevel) {
        elevators.add(new Elevator(elevators.size(), currentLevel));
        return elevators.size() - 1;
    }

    Pair<Integer, Integer> getElevatorAndSteps(int floor) {
        int result = Integer.MAX_VALUE;
        int elevatorId = -1;

        LinkedList<Elevator> elevatorsCopy = new LinkedList<>();

        for (int i = 0; i < elevators.size(); ++i) {
            elevatorsCopy.add(i, elevators.get(i).clone());
        }

        for (Elevator elevator : elevatorsCopy) {
            int pickupTime = elevator.pickup(floor, 0, false);

//            System.out.println("PICKUP TIME: " + pickupTime + " FOR ELEVATOR: " + elevator.getElevatorId());

            if (pickupTime < result) {
                result = pickupTime;
                elevatorId = elevator.getElevatorId();
            }
        }

        return new Pair<>(elevatorId, result);
    }

    void pickup(int floor, int offset) {
        Pair<Integer, Integer> bestElevator = getElevatorAndSteps(floor);
        int elevatorId = bestElevator.getFirst();

        elevators.get(elevatorId).pickup(floor, 0, false);
        elevators.get(elevatorId).pickup(floor, offset, true);

//        System.out.println("Elevator for pickup: " + elevatorId + ",\nsteps: " + bestElevator.getSecond());
    }

    void printElevators() {
        elevators.forEach(e -> System.out.println(e));
    }

    void printStatus() {
        System.out.println(status());
    }

    void step() {
        elevators.forEach(e -> e.step());
    }
}
