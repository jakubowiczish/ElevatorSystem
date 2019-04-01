package elevatorsystem;

import java.util.LinkedList;

/**
 * Class that contains information about elevator
 * in addition to necessary methods that make usage of the program possible
 *
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
     * Does the step operation for the elevator -
     * if the list of levels to visit is empty nothing happens
     * if the the direction of elevator is equal to Direction.DOWN then the elevator moves one level down
     * similarly when Direction.UP - the elevator moves one level up
     */
    public void step() {
        if (levels.isEmpty()) return;

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


    /**
     * Main method of the algorithm,
     * returns the number of steps that the elevator needs to do to be able to do the pickup,
     * as well as adds floor and destination floor respectively to the list of levels to make the algorithm efficient
     * <p>
     * The method should be called for every element in the copy of list of elevators
     * because initially the clue is to determine the optimum number of steps amongst every elevator and then choose best elevator
     *
     * @param floor              floor number for the pickup operation
     * @param offset             the direction of the elevator and the distance to overcome in one
     *                           e.g. when offset is equal to -3 it means that elevator is to move 3 floors downwards
     * @param isDestinationToAdd boolean value that decides whether the destination floor is to be added to the list
     * @return number of steps that this elevator should do to be able to carry out the pickup operation for given floor
     * @see ElevatorSystem
     * @see Elevator
     */
    int pickup(int floor, int offset, boolean isDestinationToAdd) {
        if (getDirection() == Direction.NONE) {
            int difference = getFloorDifference(floor, currentLevel);

            if (!isDestinationToAdd) {
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

            int difference = 100;

            Pair<Integer, Integer> increasingBounds;

            if (!isDestinationToAdd) {
                increasingBounds = getBounds(Direction.UP);

                if (isBetweenBounds(increasingBounds, floor, true)) {
                    LinkedList<Integer> increasingSubList = new LinkedList<>(levels.subList(
                            increasingBounds.getFirst(), increasingBounds.getSecond() + 1)
                    );

                    if (!increasingSubList.contains(floor)) {
                        boolean added = false;
                        for (int i = increasingBounds.getFirst(); i <= increasingBounds.getSecond(); ++i) {
                            if (levels.get(i) > floor) {
                                levels.add(i, floor);
                                added = true;
                                break;
                            }
                        }
                        if (!added) {
                            levels.addLast(floor);
                        }
                    } else {
                        levels.addLast(floor);
                    }

                } else {
                    if (currentLevel < floor) {
                        levels.add(0, floor);
                    } else {
                        levels.add(increasingBounds.getSecond() + 1, floor);
                    }
                }

                difference = countNumberOfSteps(floor);
            }

            if (isDestinationToAdd) {
                increasingBounds = getBounds(Direction.UP);

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

            return difference;

        } else {
            int difference = 100;

            Pair<Integer, Integer> decreasingBounds;

            if (!isDestinationToAdd) {
                decreasingBounds = getBounds(Direction.DOWN);

                if (isBetweenBounds(decreasingBounds, floor, false)) {
                    LinkedList<Integer> decreasingSubList = new LinkedList<>(levels.subList(
                            decreasingBounds.getFirst(), decreasingBounds.getSecond() + 1
                    ));

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
                    if (currentLevel > floor) {
                        levels.add(0, floor);
                    } else {
                        levels.add(decreasingBounds.getSecond() + 1, floor);
                    }

                }

                difference = countNumberOfSteps(floor);
            }


            if (isDestinationToAdd) {
                decreasingBounds = getBounds(Direction.DOWN);

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

            return difference;
        }
    }


    /**
     * Counts amount of steps that the elevator needs to take to be able to do the pickup operation
     *
     * @param floor floor number for which number of steps is to be determined
     * @return number of steps that the elevator needs to take to be able to do the pickup
     * @see ElevatorSystem
     */
    private int countNumberOfSteps(int floor) {
        if (!levels.contains(floor)) {
            return 1000;
        }

        int indexOfFloor = levels.indexOf(floor);

        int previous, actual;
        int result = 0;
        for (int i = 1; i <= indexOfFloor; ++i) {
            previous = levels.get(i - 1);
            actual = levels.get(i);
            result += Math.abs(actual - previous);
        }

        return result;
    }


    private int getFloorDifference(int floor, int currentLevel) {
        return Math.abs(floor - currentLevel);
    }


    /**
     * Method that checks whether the floor is between given bounds, for increasing or decreasing sub list
     * - third parameter specifies whether it is increasing list
     *
     * @param bounds       bounds for which the floor will be checked
     * @param floor        floor that needs to be checked
     * @param isIncreasing information about whether the list is increasing or decreasing
     * @return true if floor is between given bounds, false otherwise
     */
    private boolean isBetweenBounds(Pair<Integer, Integer> bounds, int floor, boolean isIncreasing) {
        if (isIncreasing) {
            return floor > levels.get(bounds.getFirst()) && floor < levels.get(bounds.getSecond());
        }

        return floor < levels.get(bounds.getFirst()) && floor > levels.get(bounds.getSecond());
    }


    /**
     * Returns Pair<Integer, Integer> that contains indices that specify
     * bounds for either first increasing or first decreasing list in the list of levels
     *
     * @param direction a current direction of the elevator
     * @return Pair<Integer, Integer>
     * - first field in pair is the first index of the bounds, second field is the last index of the bounds
     * @see Elevator
     */
    private Pair<Integer, Integer> getBounds(Direction direction) {
        int startIndex = 0, endIndex = levels.size() - 1;
        boolean isRising = false;
        boolean isDecreasing = false;

        int i;
        for (i = 1; i < levels.size(); ++i) {
            int previous = levels.get(i - 1);
            int actual = levels.get(i);

            if (direction == Direction.UP) {
                if (actual > previous) {
                    if (!isRising) {
                        startIndex = i - 1;
                    }
                    isRising = true;
                } else {
                    endIndex = i - 1;
                    break;
                }
            } else if (direction == Direction.DOWN) {
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
     * Returns current direction of the elevator
     *
     * @return current direction of the elevator
     * @see Direction
     * @see Elevator
     */
    private Direction getDirection() {
        if (levels.isEmpty()) {
            return Direction.NONE;
        }
        if (currentLevel - levels.getFirst() < 0) {
            return Direction.UP;
        }

        return Direction.DOWN;
    }


    /**
     * Enum that contains fields that specify the direction of the elevator
     *
     * @see Elevator
     */
    enum Direction {
        UP,
        DOWN,
        NONE
    }


    /**
     * Returns current status of the elevator
     *
     * @return current status of the elevator
     * @see ElevatorStatus
     */
    ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }


    /**
     * An inner class that is needed to generate status of the elevator
     *
     * @see Elevator
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
            return "\nSTATUS OF ELEVATOR:\n" +
                    " elevatorId: " + elevatorId +
                    ", current level: " + currentLevel +
                    ", current destination level: " + currentDestinationLevel +
                    ", levels to visit: " + levels +
                    "\n";
        }
    }


    @Override
    public String toString() {
        return "\nELEVATOR:\n" +
                "elevatorId: " + elevatorId +
                ", currentLevel: " + currentLevel +
                ", levels: " + levels +
                "\n";
    }
}
