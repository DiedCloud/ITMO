import java.util.Objects;

public class Location{
    private int x;
    private int y;
    private int z;

    public Location(int x, int y, int z){
        this.x = x;
        this.y = y;
        this.z = z;
    }
    public int[] getPos(){
        return new int[]{x, y, z};
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return x == location.x && y == location.y && z == location.z;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y, z);
    }

    @Override
    public String toString() {
        return "Location{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                '}';
    }
}
