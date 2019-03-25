package elevatorsystem;

import java.util.LinkedList;

class Elevator {
    public Elevator(int elevatorId, int currentLevel) {
        this.elevatorId = elevatorId;
        this.currentLevel = currentLevel;
        destinationLevels = new LinkedList<>();
    }

    private int elevatorId;
    private int currentLevel;
    private LinkedList<Integer> destinationLevels;

    public int getElevatorId() {
        return elevatorId;
    }

    public int getCurrentLevel() {
        return currentLevel;
    }

    public LinkedList<Integer> getDestinationLevels() {
        return destinationLevels;
    }

    public void setElevatorId(int elevatorId) {
        this.elevatorId = elevatorId;
    }

    public void setCurrentLevel(int currentLevel) {
        this.currentLevel = currentLevel;
    }

    public void setDestinationLevels(LinkedList<Integer> destinationLevels) {
        this.destinationLevels = destinationLevels;
    }

    public ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }

    public void pickup(int floorNumber, int destinationOffset) {
        addDestination(floorNumber);
        addDestination(floorNumber + destinationOffset);
    }

    public void addDestination(int destinationLevel) {
        if (!destinationLevels.contains(destinationLevel)) {
            destinationLevels.addLast(destinationLevel);
        }
    }

    class ElevatorStatus {
        int elevatorId;
        int currentLevel;
        int currentDestinationLevel;

        ElevatorStatus() {
            this.elevatorId = Elevator.this.elevatorId;
            this.currentLevel = Elevator.this.currentLevel;
            this.currentDestinationLevel = Elevator.this.getDestinationLevels().getFirst();
        }
    }
}
