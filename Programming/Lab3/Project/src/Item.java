public abstract class Item {
    protected String name;
    protected Boolean isBad;
    public String getName(){
        return name;
    }

    public Boolean getBadStatus() {
        return isBad;
    }

    public void goBad(){
        isBad = true;
    }
}
