package elevatorsystem;

import elevatorsystem.model.Pair;

import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSystem {
    private List<Elevator> elevators;

    public ElevatorSystem(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    void update(int elevatorId, int destinationLevel) {
        for (Elevator elevator : elevators) {
            if (elevator.getElevatorId() == elevatorId) {
                elevator.getDestinationLevels().add(destinationLevel);
            }
        }
    }

    List<Elevator.ElevatorStatus> status() {
        return elevators.stream().map(e -> e.generateStatus()).collect(Collectors.toList());
    }

    int addElevator(int currentLevel) {
        elevators.add(new Elevator(elevators.size(), currentLevel));
        return elevators.size() - 1;
    }

    Pair<Integer, Integer> getElevatorAndItsOffset(int floor) {
        int result = Integer.MAX_VALUE;
        int elevatorId = -1;
        for (Elevator elevator : elevators) {
            int pickupTime = elevator.timeToPickup(floor);
            if (pickupTime < result) {
                result = pickupTime;
                elevatorId = elevator.getElevatorId();
            }
        }
        return new Pair<>(elevatorId, result);
    }

    void pickup(int floorNumber) {
        Pair<Integer, Integer> bestElevator = getElevatorAndItsOffset(floorNumber);
        int elevatorId = bestElevator.getFirst();
        int bestOffset = bestElevator.getSecond();
        elevators.get(elevatorId).pickup(floorNumber, bestOffset);
        System.out.println("Elevator for pickup: " + elevatorId);
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
