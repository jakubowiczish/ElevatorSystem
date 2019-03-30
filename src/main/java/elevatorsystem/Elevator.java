package elevatorsystem;

import elevatorsystem.model.Pair;

import java.lang.annotation.ElementType;
import java.util.LinkedList;

public class Elevator {
    private int elevatorId;
    private int currentLevel;
    private LinkedList<Integer> risingLevels;
    private LinkedList<Integer> slopingLevels;
    private int direction;

    public Elevator(int elevatorId, int currentLevel) {
        this.elevatorId = elevatorId;
        this.currentLevel = currentLevel;
        risingLevels = new LinkedList<>();
        slopingLevels = new LinkedList<>();
    }

    public int getElevatorId() {
        return elevatorId;
    }


    public LinkedList<Integer> getRisingLevels() {
        return risingLevels;
    }

    public LinkedList<Integer> getSlopingLevels() {
        return slopingLevels;
    }


    public ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }


    int pickupCount(int floor) {
        if (direction == -1) {
            return Math.abs(floor - currentLevel);
        }
        return countSteps(floor);
    }

    int countSteps(int floor) {
        int indexOfFloor = risingLevels.indexOf(floor);

        if (direction == -1) {
            indexOfFloor = slopingLevels.indexOf(floor);
        }

        LinkedList<Integer> levels = risingLevels;

        if (direction == -1) {
            levels = slopingLevels;
        }

        int previous, actual;
        int result = 0;
        for (int i = 1; i < indexOfFloor; ++i) {
            previous = levels.get(i - 1);
            actual = levels.get(i);
            result += Math.abs(actual - previous);
        }

        return result;
    }

    void addWhenStaying(int floor, int offset) {
        setDirection();

        if (direction == 0) {
            if (currentLevel < floor) {
                risingLevels.addFirst(floor + offset);
                risingLevels.addFirst(floor);
            } else if (currentLevel > floor) {
                slopingLevels.addFirst(floor + offset);
                slopingLevels.addFirst(floor);
            } else {
                System.out.println("You are already on the " + floor + " floor!\nTry again");
            }
        }
    }

    void addOtherwise(int floor, int offset) {
        setDirection();

        if (direction == 1 || direction == -1) {
            if (floor + offset > currentLevel) {
                risingLevels.addFirst(floor + offset);
            } else if (floor + offset < currentLevel) {
                slopingLevels.addFirst(floor + offset);
            }

            setDirection();

            if (floor > currentLevel) {
                risingLevels.addFirst(floor);
            } else if (floor < currentLevel) {
                slopingLevels.addFirst(floor);
            }
        }
    }


    void update(int floor, int offset) {
        addWhenStaying(floor, offset);
        addOtherwise(floor, offset);
    }


    public void step() {
        if (risingLevels.size() == 0 && slopingLevels.size() == 0) return;

        if (direction == -1) {
            --currentLevel;
            System.out.println("Elevator " + elevatorId + " moves down, now on " + currentLevel + " floor");

        } else if (direction == 1) {
            ++currentLevel;
            System.out.println("Elevator " + elevatorId + " moves up, now on " + currentLevel + " floor");
        }

        if (direction == 1 && currentLevel == risingLevels.getFirst()) {
            System.out.println("Destination reached for elevator: " + elevatorId);
            risingLevels.removeFirst();
        } else if (direction == -1 && currentLevel == slopingLevels.getFirst()) {
            System.out.println("Destination reached for elevator: " + elevatorId);
            slopingLevels.removeFirst();
        }
        setDirection();

    }


    private void setDirection() {
        if (risingLevels.isEmpty() && slopingLevels.isEmpty()) {
            direction = 0;
        } else if (!risingLevels.isEmpty() && slopingLevels.isEmpty()) {
            direction = 1;
        } else if (risingLevels.isEmpty() && !slopingLevels.isEmpty()) {
            direction = -1;
        }

        if (direction == 1) {
            if (!risingLevels.isEmpty()) {
                direction = 1;
            }
        } else if (direction == -1) {
            if (!slopingLevels.isEmpty()) {
                direction = -1;
            }
        }

        direction = 0;
    }


    class ElevatorStatus {
        int elevatorId;
        int currentLevel;
        int currentDestinationLevel;

        ElevatorStatus() {
            this.elevatorId = Elevator.this.elevatorId;
            this.currentLevel = Elevator.this.currentLevel;

            if (direction == 1) {
                if (Elevator.this.risingLevels.size() > 0) {
                    this.currentDestinationLevel = Elevator.this.risingLevels.getFirst();
                } else {
                    this.currentDestinationLevel = -1;
                }
            }
            if (direction == -1) {
                if (Elevator.this.slopingLevels.size() > 0) {
                    this.currentDestinationLevel = Elevator.this.slopingLevels.getFirst();
                } else {
                    this.currentDestinationLevel = -1;
                }
            }
        }


        @Override
        public String toString() {
            return "ElevatorStatus{" +
                    "elevatorId=" + elevatorId +
                    ", currentLevel=" + currentLevel +
                    ", currentDestinationLevel=" + currentDestinationLevel +
                    '}';
        }
    }
}
