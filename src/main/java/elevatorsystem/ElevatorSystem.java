package elevatorsystem;

import java.util.List;
import java.util.stream.Collectors;

public class ElevatorSystem {
    private List<Elevator> elevators;

    public ElevatorSystem(List<Elevator> elevators) {
        this.elevators = elevators;
    }

    void update(int elevatorId, int currentLevel, int destinationLevel) {
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

    void printElevators() {
        elevators.forEach(e -> System.out.println(e));
    }
}
