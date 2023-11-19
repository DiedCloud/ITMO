package receivers;

import commands.ShowCommand;
import common.*;
import main.Main;
import managers.file.AbstractWriter;
import managers.file.DBSavable;
import result.Result;

import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public abstract class Receiver<T extends Comparable<T> & IDAccess & DBSavable> {
    Collection<T> collection;
    AbstractWriter<T> collection_to_file_writer;

    /**
     * Abstract method for adding an element to the collection.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> add(T obj) {
            Result<Boolean> insert_res = collection_to_file_writer.insert(obj);
            if (insert_res.isSuccess()) {
                Result<Void> addResult = collection.add(obj);
                if (addResult.isSuccess()) {
                    Main.logger.info("New element successfully added to collection");
                    return Result.success(null, LocalizationKeys.SUCCESS);
                } else {
                    return Result.failure(addResult.getError().orElse(null), addResult.getMessage());
                }
            } else {
                return Result.failure(insert_res.getError().orElse(null), insert_res.getMessage());
            }
    }

    /**
     * Abstract method for clearing the collection.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> clear() {

        Result<Void> result = collection.clear();
        if (result.isSuccess()) {
            Main.logger.info("Collection cleared");
            return Result.success(null, LocalizationKeys.SUCCESS);
        } else {
            Main.logger.error("Collection wasn't cleared. " + result.getMessage());
            return Result.failure(result.getError().orElse(null), result.getMessage());
        }
    }

    /**
     * Abstract method for printing information about the collection.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<?> info() {
        return Result.success(Map.of(LocalizationKeys.TYPE_OF_COLLECTION, collection.getClass().getName(),
                LocalizationKeys.NUMBER_OF_ELEMENTS, collection.getSize(),
                LocalizationKeys.DATE_OF_INITIALIZATION, collection.getInitializationDate()));
    }


    /**
     * Abstract method for removing an element from the collection by its ID.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> removeById(long id) {
        Result<Boolean> remove_res = collection_to_file_writer.remove(id);
        if (remove_res.isSuccess()) {
            try {
                boolean is_present = collection.getCollection().stream().anyMatch(element -> element.getID() == id);
                collection.setCollection(collection
                        .getCollection()
                        .stream()
                        .filter((T band) -> band.getID() != id)
                        .collect(Collectors.toCollection(TreeSet::new))
                );
                Main.logger.info("Element removed");
                if(is_present)
                    return Result.success(null, LocalizationKeys.SUCCESS);
                else
                    return Result.failure(new Exception(), LocalizationKeys.ERROR_NO_SUCH_ELEMENTS);
            } catch (Exception e) {
                return Result.failure(e, LocalizationKeys.ERROR_REMOVING_ELEMENT);
            }
        } else {
            return Result.failure(remove_res.getError().orElse(null), remove_res.getMessage());
        }
    }

    /**
     * Удаляет из коллекции все элементы, превышающие заданный
     *
     * @param element объект, с которым будут сравнивать объекты коллекции
     * @return {@link Result} of executing command (success/error)
     */
    public Result<Void> removeGreater(T element) {
        Result<Void> removeGreaterResult = collection.removeGreater(element);
        if (removeGreaterResult.isSuccess()) {
            Main.logger.info("Element removed");
            return Result.success(null);
        } else {
            return Result.failure(new Exception("ERROR_NO_SUCH_ELEMENTS"));
        }

    }

    public T findById(long id) {
        return collection.getCollection()
                .stream()
                .filter((T element) -> element.getID() == id)
                .findFirst().
                orElse(null);
    }

    /**
     * Abstract method for saving the collection to a file.
     *
     * @return a Result object that indicates the status of the save operation.
     */
    public Result<Void> saveCollection() {
        return collection.save();
    }

    /**
     * Abstract method for displaying all elements of the collection.
     *
     * @return a Result object that indicates the status of the display operation.
     */
    public abstract Result<common.Collection<T>> showElementsOfCollection();

    /*public abstract Result<List<Result<?>>> executeQueue(List<CommandDescription> queue, Invoker invoker);*/
}
