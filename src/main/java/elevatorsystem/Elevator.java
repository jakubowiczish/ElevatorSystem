package elevatorsystem;

import elevatorsystem.model.Pair;

import java.util.LinkedList;

public class Elevator {
    private int elevatorId;
    private int currentLevel;
    private LinkedList<Level> levels;


    public Elevator(int elevatorId, int currentLevel) {
        this.elevatorId = elevatorId;
        this.currentLevel = currentLevel;
        levels = new LinkedList<>();
    }


    public int getElevatorId() {
        return elevatorId;
    }


    public LinkedList<Level> getLevels() {
        return levels;
    }


    public ElevatorStatus generateStatus() {
        return new ElevatorStatus();
    }


    private int countDistance(Level level) {
        return Math.abs(currentLevel - level.getLevelNumber());
    }


    boolean containsLevelNumber(int levelNumber) {
        for (Level level : levels) {
            if (level.getLevelNumber() == levelNumber)
                return true;
        }

        return false;
    }

    private void addToLevels(Level levelToAdd) {
        if (levels.size() == 0) {
            levels.addFirst(levelToAdd);
        } else {
            if (!containsLevelNumber(levelToAdd.getLevelNumber())) {
                Level firstLevelInTheList = levels.getFirst();
                if (countDistance(levelToAdd) < countDistance(firstLevelInTheList)) {
                    if (levelToAdd.getSourceLevel() == firstLevelInTheList.getLevelNumber()) {
                        if (levels.size() >= 2) {
                            levels.add(2, levelToAdd);
                        } else {
                            levels.addLast(levelToAdd);
                        }
                    } else if (levelToAdd.getLevelNumber() == firstLevelInTheList.getSourceLevel()) {
                        levels.addFirst(levelToAdd);
                    }
                } else {
                    if (firstLevelInTheList.getSourceLevel() == levelToAdd.getLevelNumber()) {
                        levels.addFirst(levelToAdd);
                    } else {
                        if (levels.size() >= 2) {
                            levels.add(2, levelToAdd);
                        } else {
                            levels.addLast(levelToAdd);
                        }
                    }
                }
            }

        }
    }

    boolean subListContains(LinkedList<Level> subList, Level level) {
        for (Level level1 : subList) {
            if (level1.getLevelNumber() == level.getLevelNumber()) {
                return true;
            }
        }

        return false;
    }


    int pickup(int floor, int offset) {
        Level srcLevel = new Level(floor, 0, false);
        Level destLevel = new Level(floor + offset, offset, true);

        if (getDirection() == Direction.NONE) {
            addToLevels(srcLevel);
            addToLevels(destLevel);
            return getFloorDifference(floor);
        } else if (getDirection() == Direction.UP) {
            Pair<Integer, Integer> increasingBounds = getBounds(Direction.UP);

            if (isBetweenBounds(increasingBounds, floor)) {
                LinkedList<Level> increasingSubList = new LinkedList<>(
                        levels.subList(increasingBounds.getFirst(), increasingBounds.getSecond())
                );

                if (!subListContains(increasingSubList, srcLevel)) {
                    for (int i = increasingBounds.getFirst(); i < increasingBounds.getSecond(); ++i) {

                    }
                }
            }


        }

        return -1;
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

        if (currentLevel == levels.getFirst().getLevelNumber()) {
            System.out.println("Destination reached for elevator: " + elevatorId);
            levels.removeFirst();
        }
    }


    private Pair<Integer, Integer> getBounds(Direction direction) {

        int startIndex = levels.size() - 1, endIndex = levels.size() - 1;
        boolean isRising = false;
        boolean isDecreasing = false;

        int i;
        for (i = 1; i < levels.size(); ++i) {
            int previous = levels.get(i - 1).getLevelNumber();
            int actual = levels.get(i).getLevelNumber();

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


    private Direction getDirection() {
        if (levels.size() == 0) {
            return Direction.NONE;
        }

        if (currentLevel - levels.getFirst().getLevelNumber() < 0) {
            return Direction.UP;
        }

        return Direction.DOWN;
    }


    int countNumberOfSteps(int floor) {
        int indexOfFloor = levels.indexOf(floor);

        int previous, actual;
        int result = 0;

        for (int i = 1; i < indexOfFloor; ++i) {
            previous = levels.get(i - 1).getLevelNumber();
            actual = levels.get(i).getLevelNumber();
            result += Math.abs(actual - previous);
        }

        return result;
    }


    private int getFloorDifference(int floor) {
        return Math.abs(floor - currentLevel);
    }


    private boolean isBetweenBounds(Pair<Integer, Integer> bounds, int floor) {
        return floor > levels.get(bounds.getFirst()).getLevelNumber()
                && floor < levels.get(bounds.getSecond()).getLevelNumber();
    }


    class ElevatorStatus {
        int elevatorId;
        int currentLevel;
        int currentDestinationLevel;
        LinkedList<Level> levels;

        ElevatorStatus() {
            this.elevatorId = Elevator.this.elevatorId;
            this.currentLevel = Elevator.this.currentLevel;
            if (getLevels().size() > 0) {
                this.currentDestinationLevel = Elevator.this.getLevels().getFirst().getLevelNumber();
            } else {
                this.currentDestinationLevel = -1;
            }
            this.levels = Elevator.this.levels;
        }


        @Override
        public String toString() {
            return "ElevatorStatus{" +
                    "elevatorId=" + elevatorId +
                    ", currentLevel=" + currentLevel +
                    ", currentDestinationLevel=" + currentDestinationLevel +
                    ", levels=" + levels +
                    '}';
        }
    }

    enum Direction {
        UP,
        DOWN,
        NONE
    }
}
