package elevatorsystem;

import elevatorsystem.model.Pair;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSystem {
    private List<Elevator> elevators;

    public ElevatorSystem(List<Elevator> elevators) {
        this.elevators = elevators;
    }

//    void update(int elevatorId, int destinationLevel) {
//        for (Elevator elevator : elevators) {
//            if (elevator.getElevatorId() == elevatorId) {
//                elevator.getLevels().add(destinationLevel);
//            }
//        }
//    }

    List<Elevator.ElevatorStatus> status() {
        return elevators.stream().map(e -> e.generateStatus()).collect(Collectors.toList());
    }

    int addElevator(int currentLevel) {
        elevators.add(new Elevator(elevators.size(), currentLevel));
        return elevators.size() - 1;
    }

    Pair<Integer, Integer> getElevatorAndTime(int floor, int offset) {
        int result = Integer.MAX_VALUE;
        int elevatorId = -1;

        LinkedList<Elevator> copyElevators = new LinkedList<>(elevators);

        for (Elevator elevator : copyElevators) {

            int pickupTime = elevator.pickup(floor, offset);
            if (pickupTime < result) {
                result = pickupTime;
                elevatorId = elevator.getElevatorId();
            }
        }
        return new Pair<>(elevatorId, result);
    }

    void pickup(int floorNumber, int offset) {
        Pair<Integer, Integer> bestElevator = getElevatorAndTime(floorNumber, offset);
        int elevatorId = bestElevator.getFirst();

        for (Elevator elevator : elevators) {
            if (elevator.getElevatorId() == elevatorId) {
                elevator.pickup(floorNumber, offset);
            }
        }

//        elevators.get(elevatorId).pickup(floorNumber, offset);
        System.out.println("ElevatorExile for pickup: " + elevatorId);
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
