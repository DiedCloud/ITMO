package receivers;

import common.*;
import main.Main;
import result.Result;

import java.util.TreeSet;
import java.util.stream.Collectors;

public abstract class Receiver<T extends Comparable<T> & IDAccess> {
    Collection<T> collection;

    /**
     * Abstract method for adding an element to the collection.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> add(T obj) {
        try {
            Result<Void> addResult = collection.add(obj);
            if (addResult.isSuccess()) {
                Main.logger.info("New element successfully added to collection");
                return Result.success(null, "New element successfully added to collection");
            } else {
                return Result.failure(addResult.getError().orElse(null), addResult.getMessage());
            }
        } catch (Exception e) {
            return Result.failure(e, "Error with executing add command");
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
            return Result.success(null, "Collection successfully cleared");
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
    public Result<String> info() {
        return Result.success("Тип коллекции: " + collection.getClass().getName() +
                "\nКоличество элементов: " + collection.getSize() +
                "\nДата инициализации: " + collection.getInitializationDate());
    }


    /**
     * Abstract method for removing an element from the collection by its ID.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> removeById(long id) {
        try {
            boolean is_present = collection.getCollection().stream().anyMatch(element -> element.getID()==id);
            collection.setCollection(collection
                    .getCollection()
                    .stream()
                    .filter((T band) -> band.getID() != id)
                    .collect(Collectors.toCollection(TreeSet::new))
            );
            Main.logger.info("Element removed");

            return Result.success(null, is_present ? "Element removed" : "No such ID presented");
        } catch (Exception e) {
            return Result.failure(e, "Error with removing element");
        }
    }

    /**
     * удаляет из коллекции все элементы, превышающие заданный
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
            return Result.failure(new Exception("Greater elements are not found"));
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
    public abstract Result<Collection<T>> showElementsOfCollection();

    /**
     * Abstract method for updating an element of the collection by ID.
     *
     * @return a Result object that indicates the status of the update operation.
     */
    public Result<Void> updateById(long id, T newElement) {
        try {
            if (findById(id) == null) {
                Main.logger.info("No such element");
                return Result.failure(new Exception("Element with such ID is not found"), "Element with such ID is not found");
            }

            Result<Void> result = collection.remove(findById(id));
            if (result.isSuccess()) {
                newElement.setID(id);
                result = collection.add(newElement);
                if (result.isSuccess()) {
                    Main.logger.info("Element updated");
                    return Result.success(null, "Element successfully updated");
                } else {
                    Main.logger.info("Element just removed");
                    return Result.failure(result.getError().get(), result.getMessage());
                }
            } else {
                Main.logger.info("Element didn't change");
                return Result.failure(result.getError().get(), result.getMessage());
            }
        } catch (Exception e) {
            Main.logger.error(e.getMessage());
            return Result.failure(e, "Error with executing updateById command");
        }
    }

    /*public abstract Result<List<Result<?>>> executeQueue(List<CommandDescription> queue, Invoker invoker);*/
}
