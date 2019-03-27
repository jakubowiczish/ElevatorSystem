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

    public void step() {
        if (destinationLevels.size() == 0) return;

        if (getDirection() == Direction.DOWN) {
            --currentLevel;
        } else if (getDirection() == Direction.UP) {
            ++currentLevel;
        }

        if (currentLevel == destinationLevels.getFirst()) {
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

    int realPickup(int floor) {
        if (getDirection() == Direction.NONE) {
            return getFloorDifference(floor, currentLevel);
        }

        if (getDirection() == Direction.UP) {
            Pair<Integer, Integer> increasingBounds = getBounds(Direction.UP);
            LinkedList<Integer> increasingSubList = (LinkedList<Integer>) destinationLevels.subList(
                    increasingBounds.getFirst(), increasingBounds.getSecond()
            );

            if (isBetweenBounds(increasingBounds, floor)) {
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

        } else if (getDirection() == Direction.DOWN) {
            Pair<Integer, Integer> decreasingBounds = getBounds(Direction.DOWN);
            LinkedList<Integer> decreasingSubList = (LinkedList<Integer>) destinationLevels.subList(
                    decreasingBounds.getFirst(), decreasingBounds.getSecond()
            );

            if (isBetweenBounds(decreasingBounds, floor)) {
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

        return destinationLevels.get(floor);
    }

    int timeToPickup(int floor) {
        if (getDirection() == Direction.NONE) {
            return getFloorDifference(floor, currentLevel);
        }

        LinkedList<Integer> levels = destinationLevels;

        if (getDirection() == Direction.UP) {
            Pair<Integer, Integer> increasingBounds = getBounds(Direction.UP);
            LinkedList<Integer> increasingSubList = new LinkedList<>(levels.subList(
                    increasingBounds.getFirst(), increasingBounds.getSecond())
            );

            if (isBetweenBounds(increasingBounds, floor)) {
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

        } else if (getDirection() == Direction.DOWN) {
            Pair<Integer, Integer> decreasingBounds = getBounds(Direction.DOWN);
            LinkedList<Integer> decreasingSubList = (LinkedList<Integer>) levels.subList(
                    decreasingBounds.getFirst(), decreasingBounds.getSecond()
            );

            if (isBetweenBounds(decreasingBounds, floor)) {
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

        return levels.get(floor);
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

    private Pair<Integer, Integer> getBounds(Direction direction) {
        int startIndex = -1, endIndex = -1;
        boolean isRising = false;
        boolean isDecreasing = false;

        int i;
        for (i = 1; i < destinationLevels.size(); ++i) {
            int previous = destinationLevels.get(i - 1);
            int actual = destinationLevels.get(i);

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
        if (endIndex == -1) endIndex = destinationLevels.size();

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

    private Direction getDirection() {
        if (destinationLevels.size() == 0) return Direction.NONE;
        if (currentLevel > destinationLevels.getFirst()) return Direction.DOWN;
        return Direction.UP;
    }

    enum Direction {
        UP,
        DOWN,
        NONE
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
