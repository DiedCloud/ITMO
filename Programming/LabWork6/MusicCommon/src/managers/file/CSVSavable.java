package managers.file;

import result.Result;

public interface CSVSavable {
    public Result<String> toCSV();
}
