package elevatorsystem;

import elevatorsystem.model.Pair;

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

    public LinkedList<Integer> getDestinationLevels() {
        return destinationLevels;
    }

    public ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }

    int getDirection() {
        if (destinationLevels.size() == 0) {
            return 0;
        }
        if (currentLevel - destinationLevels.getFirst() < 0) {
            return 1;
        }

        return -1;
    }

    public void step() {
        if (destinationLevels.size() == 0) return;

        if (getDirection() == -1) {
            --currentLevel;
            System.out.println("Elevator " + elevatorId + " moves down, now on " + currentLevel + " floor");
        } else if (getDirection() == 1) {
            ++currentLevel;
            System.out.println("Elevator " + elevatorId + " moves up, now on " + currentLevel + " floor");
        }

        if (currentLevel == destinationLevels.getFirst()) {
            System.out.println("Destination reached for elevator: " + elevatorId);
            destinationLevels.removeFirst();
        }
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

    int countNumberOfSteps(int floor) {
        int indexOfFloor = destinationLevels.indexOf(floor);

        int previous, actual;
        int result = 0;
        for (int i = 1; i < indexOfFloor; ++i) {
            previous = destinationLevels.get(i - 1);
            actual = destinationLevels.get(i);
            result += Math.abs(actual - previous);
        }

        return result;
    }

    int doThePickup(int floor) {
        if (getDirection() == 0) {
            int difference = getFloorDifference(floor, currentLevel);
            System.out.println("Real difference: " + difference);
            return difference;
        }

        if (getDirection() == 1) {
            Pair<Integer, Integer> increasingBounds = getBounds(1);

            if (isBetweenBounds(increasingBounds, floor)) {
                LinkedList<Integer> increasingSubList = new LinkedList<>(destinationLevels.subList(
                        increasingBounds.getFirst(), increasingBounds.getSecond()
                ));
                if (!increasingSubList.contains(floor)) {
                    for (int i = increasingBounds.getFirst(); i < increasingBounds.getSecond(); ++i) {
                        if (destinationLevels.get(i) > floor) {
                            destinationLevels.add(i, floor);
                            break;
                        }
                    }
                }
            } else {
                destinationLevels.add(increasingBounds.getSecond() + 1, floor);
            }

        } else if (getDirection() == -1) {
            Pair<Integer, Integer> decreasingBounds = getBounds(-1);

            if (isBetweenBounds(decreasingBounds, floor)) {
                LinkedList<Integer> decreasingSubList = new LinkedList<>(destinationLevels.subList(
                        decreasingBounds.getFirst(), decreasingBounds.getSecond()
                ));
                if (!decreasingSubList.contains(floor)) {
                    for (int i = decreasingBounds.getFirst(); i < decreasingBounds.getSecond(); ++i) {
                        if (destinationLevels.get(i) < floor) {
                            destinationLevels.add(i, floor);
                            break;
                        }
                    }
                }
            } else {
                destinationLevels.add(decreasingBounds.getSecond() + 1, floor);
            }
        }
        int numberOfSteps = countNumberOfSteps(floor);
        System.out.println("Number of steps: " + numberOfSteps);
        return numberOfSteps;
    }

    int timeToPickup(int floor) {
        if (getDirection() == 0) {
            int difference = getFloorDifference(floor, currentLevel);
            System.out.println("Difference: " + difference);
            return difference;
        }

        LinkedList<Integer> levels = destinationLevels;

        if (getDirection() == 1) {
            Pair<Integer, Integer> increasingBounds = getBounds(1);

            if (isBetweenBounds(increasingBounds, floor)) {
                LinkedList<Integer> increasingSubList = new LinkedList<>(levels.subList(
                        increasingBounds.getFirst(), increasingBounds.getSecond())
                );
                if (!increasingSubList.contains(floor)) {
                    for (int i = increasingBounds.getFirst(); i < increasingBounds.getSecond(); ++i) {
                        if (levels.get(i) > floor) {
                            levels.add(i, floor);
                            break;
                        }
                    }
                }
            } else {
                levels.add(increasingBounds.getSecond() + 1, floor);
            }

        } else if (getDirection() == -1) {
            Pair<Integer, Integer> decreasingBounds = getBounds(-1);


            if (isBetweenBounds(decreasingBounds, floor)) {
                LinkedList<Integer> decreasingSubList = new LinkedList<>(levels.subList(
                        decreasingBounds.getFirst(), decreasingBounds.getSecond()
                ));
                if (!decreasingSubList.contains(floor)) {
                    for (int i = decreasingBounds.getFirst(); i < decreasingBounds.getSecond(); ++i) {
                        if (levels.get(i) < floor) {
                            levels.add(i, floor);
                            break;
                        }
                    }
                }
            } else {
                levels.add(decreasingBounds.getSecond() + 1, floor);
            }
        }
        int numberOfSteps = countNumberOfSteps(floor);
        System.out.println("Number of steps: " + numberOfSteps);
        return numberOfSteps;
    }

    private int getFloorDifference(int floor, int currentLevel) {
        return Math.abs(floor - currentLevel);
    }

    private boolean isBetweenBounds(Pair<Integer, Integer> bounds, int floor) {
        if (floor > destinationLevels.get(bounds.getFirst())
                && floor < destinationLevels.get(bounds.getSecond())) {
            return true;
        }
        return false;
    }

    private Pair<Integer, Integer> getBounds(int direction) {
        int startIndex = destinationLevels.size() - 1, endIndex = destinationLevels.size() - 1;
        boolean isRising = false;
        boolean isDecreasing = false;

        int i;
        for (i = 1; i < destinationLevels.size(); ++i) {
            int previous = destinationLevels.get(i - 1);
            int actual = destinationLevels.get(i);

            if (direction == 1) {
                if (actual > previous) {
                    if (!isRising) {
                        startIndex = i - 1;
                    }
                    isRising = true;
                } else {
                    endIndex = i - 1;
                    break;
                }
            } else if (direction == -1) {
                if (actual < previous) {
                    if (!isDecreasing) {
                        startIndex = i - 1;
                    }
                    isDecreasing = true;
                } else {
                    endIndex = i - 1;
                    break;
                }
            }
        }

        return new Pair<>(startIndex, endIndex);
    }

    class ElevatorStatus {
        int elevatorId;
        int currentLevel;
        int currentDestinationLevel;

        ElevatorStatus() {
            this.elevatorId = Elevator.this.elevatorId;
            this.currentLevel = Elevator.this.currentLevel;
            if (getDestinationLevels().size() > 0) {
                this.currentDestinationLevel = Elevator.this.getDestinationLevels().getFirst();
            } else {
                this.currentDestinationLevel = -1;
            }
        }


        @Override
        public String toString() {
            return "ElevatorStatus{" +
                    "elevatorId=" + elevatorId +
                    ", currentLevel=" + currentLevel +
                    ", currentDestinationLevel=" + currentDestinationLevel +
                    "}\n";
        }
    }


    @Override
    public String toString() {
        return "Elevator{" +
                "elevatorId=" + elevatorId +
                ", currentLevel=" + currentLevel +
                ", destinationLevels=" + destinationLevels +
                '}';
    }
}
