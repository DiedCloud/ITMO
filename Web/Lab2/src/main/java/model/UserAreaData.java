package model;
import java.beans.JavaBean;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

@JavaBean
public class UserAreaData implements Serializable {
    private ArrayList<AreaData> areaDataList;

    public UserAreaData() {
        areaDataList = new ArrayList<>();
    }

    public ArrayList<AreaData> getAreaDataList() {
        return areaDataList;
    }

    public void setAreaDataList(ArrayList<AreaData> areaDataList) {
        this.areaDataList = areaDataList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserAreaData)) return false;
        UserAreaData that = (UserAreaData) o;
        return Objects.equals(getAreaDataList(), that.getAreaDataList());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAreaDataList());
    }

    @Override
    public String toString() {
        return "UserAreaDatas{" +
                "areaDataList=" + areaDataList +
                '}';
    }
}