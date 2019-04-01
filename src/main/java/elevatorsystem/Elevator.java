package elevatorsystem;

import java.util.LinkedList;

/**
 * Class that contains information about elevator
 * in addition to necessary methods that make usage of the program possible
 * @see ElevatorSystem
 * @see ElevatorStatus
 */
class Elevator {
    private int elevatorId;
    private int currentLevel;
    private LinkedList<Integer> levels;


    public Elevator(int elevatorId, int currentLevel) {
        this.elevatorId = elevatorId;
        this.currentLevel = currentLevel;
        levels = new LinkedList<>();
    }


    public Elevator(int elevatorId, int currentLevel, LinkedList<Integer> levels) {
        this.elevatorId = elevatorId;
        this.currentLevel = currentLevel;
        this.levels = new LinkedList<>(levels);
    }


    /**
     * Returns the clone of the elevator, required to make the copy of all elevators
     *
     * @return a clone of the elevator
     * @see ElevatorSystem
     */
    public Elevator clone() {
        return new Elevator(this.elevatorId, this.currentLevel, this.levels);
    }

    /**
     * Returns elevator id
     *
     * @return elevator id
     */
    public int getElevatorId() {
        return elevatorId;
    }


    public LinkedList<Integer> getLevels() {
        return levels;
    }


    /**
     * Returns elevator status for
     *
     * @return
     * @see ElevatorStatus
     */
    public ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }


    /**
     * Returns current direction of the elevator
     *
     * @return current direction of the elevator
     */
    Direction getDirection() {
        if (levels.isEmpty()) {
            return Direction.NONE;
        }
        if (currentLevel - levels.getFirst() < 0) {
            return Direction.UP;
        }

        return Direction.DOWN;
    }


    public void step() {
        if (levels.size() == 0) return;

        if (getDirection() == Direction.DOWN) {
            --currentLevel;
            System.out.println("Elevator " + elevatorId + " moves down, now on " + currentLevel + " floor");
        } else if (getDirection() == Direction.UP) {
            ++currentLevel;
            System.out.println("Elevator " + elevatorId + " moves up, now on " + currentLevel + " floor");
        }

        if (currentLevel == levels.getFirst()) {
            System.out.println("Destination reached for elevator: " + elevatorId);
            levels.removeFirst();
        }
    }


    int countNumberOfSteps(int floor) {
        int indexOfFloor = levels.indexOf(floor);

        int previous, actual;
        int result = 0;
        for (int i = 1; i < indexOfFloor; ++i) {
            previous = levels.get(i - 1);
            actual = levels.get(i);
            result += Math.abs(actual - previous);
        }

        return result;
    }


    int pickup(int floor, int offset, boolean isDestinationToAdd) {
        if (getDirection() == Direction.NONE) {
            int difference = getFloorDifference(floor, currentLevel);
            if (levels.isEmpty()) {
                levels.add(floor);
            } else {
                if (getFloorDifference(floor, currentLevel) < getFloorDifference(levels.getFirst(), currentLevel)) {
                    levels.addFirst(floor);
                } else {
                    for (int i = 0; i < levels.size(); ++i) {
                        if (getFloorDifference(floor, currentLevel) < getFloorDifference(levels.get(i), currentLevel)) {
                            levels.add(i, floor);
                            break;
                        }
                    }
                }
            }

            if (isDestinationToAdd) {
                int destination = floor + offset;

                int indexOfFloor = levels.indexOf(floor);
                if (indexOfFloor == levels.size() - 1) {
                    levels.add(destination);
                } else {
                    for (int i = indexOfFloor + 1; i < levels.size(); ++i) {
                        if (getFloorDifference(floor, currentLevel) < getFloorDifference(levels.get(i), currentLevel)) {
                            levels.add(i, floor);
                            break;
                        }
                    }
                }
            }

            return difference;

        } else if (getDirection() == Direction.UP) {
            Pair<Integer, Integer> increasingBounds = getBounds(1);

            if (!isDestinationToAdd) {

                if (isBetweenBounds(increasingBounds, floor, true)) {
                    LinkedList<Integer> increasingSubList = new LinkedList<>(levels.subList(
                            increasingBounds.getFirst(), increasingBounds.getSecond() + 1)
                    );

                    if (!increasingSubList.contains(floor)) {
                        for (int i = increasingBounds.getFirst(); i <= increasingBounds.getSecond(); ++i) {
                            if (levels.get(i) > floor) {
                                if (!levels.contains(floor)) {
                                    levels.add(i, floor);
                                }
                                break;
                            }
                        }
                    }

                } else {
                    if (!levels.contains(floor)) {
                        if (currentLevel < floor) {
                            levels.add(0, floor);
                        } else {
                            levels.add(increasingBounds.getSecond() + 1, floor);
                        }
                    }
                }
            }

            if (isDestinationToAdd) {
                int destination = floor + offset;
                int indexOfFloor = levels.indexOf(floor);

                if (indexOfFloor == levels.size() - 1) {
                    levels.addLast(destination);
                } else {
                    if (isBetweenBounds(increasingBounds, floor, true) && destination > floor) {

                        LinkedList<Integer> increasingSubList = new LinkedList<>(levels.subList(
                                increasingBounds.getFirst(), increasingBounds.getSecond() + 1)
                        );

                        if (!increasingSubList.contains(destination)) {
                            if (indexOfFloor == levels.size() - 1) {
                                levels.addLast(destination);
                            } else {
                                boolean added = false;
                                for (int i = indexOfFloor + 1; i < levels.size(); ++i) {
                                    if (levels.get(i) > destination) {
                                        levels.add(i, destination);
                                        added = true;
                                        break;
                                    }
                                }
                                if (!added) {
                                    levels.addLast(destination);
                                }
                            }

                        }
                    } else if (isBetweenBounds(increasingBounds, floor, true) && destination < floor) {
                        if (levels.size() > increasingBounds.getSecond()) {
                            levels.add(increasingBounds.getSecond() + 1, destination);
                        } else {
                            levels.addLast(destination);
                        }
                    } else {
                        levels.addLast(destination);
                    }
                }
            }

        } else if (getDirection() == Direction.DOWN) {
            Pair<Integer, Integer> decreasingBounds = getBounds(-1);

            if (!isDestinationToAdd) {
                if (isBetweenBounds(decreasingBounds, floor, false)) {
                    LinkedList<Integer> decreasingSubList = new LinkedList<>(levels.subList(
                            decreasingBounds.getFirst(), decreasingBounds.getSecond() + 1
                    ));

                    System.out.println(decreasingSubList);

                    if (!decreasingSubList.contains(floor)) {
                        boolean added = false;
                        for (int i = decreasingBounds.getFirst(); i <= decreasingBounds.getSecond(); ++i) {
                            if (levels.get(i) < floor) {
                                levels.add(i, floor);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            levels.addLast(floor);
                        }
                    }
                } else {
                    if (!levels.contains(floor)) {
                        if (currentLevel < floor) {
                            levels.add(0, floor);
                        } else {
                            levels.add(decreasingBounds.getSecond() + 1, floor);
                        }
                    }
                }
            }


            if (isDestinationToAdd) {
                int destination = floor + offset;
                int indexOfFloor = levels.indexOf(floor);

                if (indexOfFloor == levels.size() - 1) {
                    levels.addLast(destination);
                } else {
                    if (isBetweenBounds(decreasingBounds, floor, false) && destination < floor) {

                        LinkedList<Integer> decreasingSubList = new LinkedList<>(levels.subList(
                                decreasingBounds.getFirst(), decreasingBounds.getSecond() + 1)
                        );

                        if (!decreasingSubList.contains(destination)) {
                            if (indexOfFloor == levels.size() - 1) {
                                levels.addLast(destination);
                            } else {
                                boolean added = false;
                                for (int i = indexOfFloor + 1; i < levels.size(); ++i) {
                                    if (levels.get(i) < destination) {
                                        levels.add(i, destination);
                                        added = true;
                                        break;
                                    }
                                }
                                if (!added) {
                                    levels.addLast(destination);
                                }
                            }
                        }
                    } else if (isBetweenBounds(decreasingBounds, floor, false) && destination > floor) {
                        if (levels.size() > decreasingBounds.getSecond()) {
                            levels.add(decreasingBounds.getSecond() + 1, destination);
                        } else {
                            levels.addLast(destination);
                        }
                    } else {
                        levels.addLast(destination);
                    }
                }
            }
        }
        return countNumberOfSteps(floor);
    }


    private int getFloorDifference(int floor, int currentLevel) {
        return Math.abs(floor - currentLevel);
    }


    private boolean isBetweenBounds(Pair<Integer, Integer> bounds, int floor, boolean isIncreasing) {
        if (isIncreasing) {
            return floor > levels.get(bounds.getFirst()) && floor < levels.get(bounds.getSecond());
        }

        return floor < levels.get(bounds.getFirst()) && floor > levels.get(bounds.getSecond());
    }


    private Pair<Integer, Integer> getBounds(int direction) {
        int startIndex = levels.size() - 1, endIndex = levels.size() - 1;
        boolean isRising = false;
        boolean isDecreasing = false;

        int i;
        for (i = 1; i < levels.size(); ++i) {
            int previous = levels.get(i - 1);
            int actual = levels.get(i);

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


    /**
     * Enum that contains fields that specify the direction of the elevator
     */
    enum Direction {
        UP,
        DOWN,
        NONE
    }


    /**
     * An inner class that is needed to generate status of the elevator
     */
    class ElevatorStatus {
        int elevatorId;
        int currentLevel;
        int currentDestinationLevel;
        LinkedList<Integer> levels;

        ElevatorStatus() {
            this.elevatorId = Elevator.this.elevatorId;
            this.currentLevel = Elevator.this.currentLevel;
            if (getLevels().size() > 0) {
                this.currentDestinationLevel = Elevator.this.getLevels().getFirst();
            } else {
                this.currentDestinationLevel = -1;
            }
            this.levels = Elevator.this.levels;
        }


        @Override
        public String toString() {
            return "STATUS:\n" +
                    "elevatorId=" + elevatorId +
                    ", currentLevel=" + currentLevel +
                    ", currentDestinationLevel=" + currentDestinationLevel +
                    ", levels=" + levels +
                    "\n";
        }
    }


    @Override
    public String toString() {
        return "ELEVATOR:\n" +
                "elevatorId=" + elevatorId +
                ", currentLevel=" + currentLevel +
                ", levels=" + levels +
                "\n";
    }
}
