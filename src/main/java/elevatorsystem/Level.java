package elevatorsystem;

public class Level {
    private int levelNumber;
    private int offset;
    private boolean isDestination;


    public Level(int levelNumber, int offset, boolean isDestination) {
        this.levelNumber = levelNumber;
        this.offset = offset;
        this.isDestination = isDestination;
    }


    int getSourceLevel() {
        return levelNumber + offset;
    }


    public int getLevelNumber() {
        return levelNumber;
    }


    public boolean isDestination() {
        return isDestination;
    }

    public boolean isBelow(Level level) {
        return this.getLevelNumber() < level.getLevelNumber();
    }
}
