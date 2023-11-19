package managers.file;

import result.Result;

import java.util.List;

public interface DBSavable {
    public Result<List<String>> toFields();
}
