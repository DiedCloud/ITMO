/**
 * The Collection class represents a collection of objects parameterized by type T, which must be comparable.
 * The collection is implemented as a TreeSet.
 */
package common;

import managers.file.AbstractReader;
import managers.file.AbstractWriter;
import result.Result;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;

public class Collection<T extends Comparable<T> & IDAccess> implements Serializable {

    private TreeSet<T> collection;

    private HashSet<Long> ids;

    protected final ReentrantLock lock = new ReentrantLock();

    private LocalDate initializationDate;

    public transient AbstractWriter<T> Collection_to_file_writer;
    public transient AbstractReader<T> Collection_from_file_loader;

    @SuppressWarnings("OptionalGetWithoutIsPresent")
    public Collection(AbstractReader<T> Collection_from_file_loader, AbstractWriter<T> Collection_to_file_writer) throws Exception {
        ids = new HashSet<>();
        this.Collection_to_file_writer = Collection_to_file_writer;
        this.Collection_from_file_loader = Collection_from_file_loader;
        Result<Collection<T>> res = Collection_from_file_loader.read();
        if (res.isSuccess()) {
            collection = res.getValue().get().getCollection();
            ids = res.getValue().get().ids;
            Collection_to_file_writer.setCollection(this);
            initializationDate = LocalDate.now();
        }else{
            throw res.getError().get();
        }
    }

    public Collection(){
        collection = new TreeSet<>();
        ids = new HashSet<>();
    }

    /**
     * Adds an element to the collection.
     *
     * @param element the element to add.
     * @return the result of the operation as a Result object. Returns null in case of success.
     * Returns a Result object with an exception and an error message in case of failure.
     */
    public Result<Void> add(T element) {
        lock.lock();
        try {
            if (!ids.contains(element.getID())) {
                ids.add(element.getID());
            }
            else
                throw new IllegalArgumentException("The id of the element is already in use.");
            collection.add(element);
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e, "Error while adding element to collection");
        } finally {
        lock.unlock();
    }
    }

    /**
     * Clears the collection.
     *
     * @return the result of the operation as a Result object. Returns null in case of success.
     * Returns a Result object with an exception and an error message in case of failure.
     */
    public Result<Void> clear() {
        lock.lock();
        try {
            collection.clear();
            ids.clear();
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e, "Error while clearing collection");
        } finally {
        lock.unlock();
    }
    }

    /**
     * Returns the collection.
     *
     * @return the collection.
     */
    public TreeSet<T> getCollection() {
        lock.lock();
        try {
            return collection;
        }finally {
            lock.unlock();
        }
    }

    /**
     * Sets the collection.
     *
     * @param collection the collection.
     */
    public void setCollection(TreeSet<T> collection) {
        this.collection = collection;
    }

    /**
     * Returns the maximum element of the collection.
     *
     * @return the result of the operation as a Result object. Returns the maximum element of the collection in case of success.
     * Returns a Result object with an exception and an error message in case of failure.
     */
    @SuppressWarnings("OptionalGetWithoutIsPresent")

    public Result<T> getMax() {
        lock.lock();
        try {
            if (this.getSize() == 0) {
                return Result.failure(new Exception("Collection is empty"), "Collection is empty");
            }

            T max = collection.stream().max(T::compareTo).get();

            return Result.success(max);
        }catch (Exception e){
            return Result.failure(e);
        }finally {
            lock.unlock();
        }
    }

    /**
     * Returns the number of elements in the collection.
     *
     * @return the number of elements in the collection.
     */
    public int getSize() {
        lock.lock();
        try {
            return collection.size();
        }finally {
            lock.unlock();
        }
    }

    /**
     * Returns the date when the collection was initialized.
     *
     * @return the date when the collection was initialized.
     */
    public String getInitializationDate() {
        lock.lock();
        try {
            return initializationDate.toString();
        }finally {
            lock.unlock();
        }

    }

    /**
     * Removes from the collection all elements greater than the specified element.
     *
     * @param element the element with respect to which the removal takes place.
     * @return the result of the operation as a Result object. Returns an empty Result in case of success.
     * Returns a Result object with an exception and an error message in case of failure.
     */
    public Result<Void> removeGreater(T element) {
        lock.lock();
        try {
            collection.removeIf(element_2 -> element_2.compareTo(element) > 0);
            ids.removeIf(id -> collection.stream().noneMatch(element_2 -> element_2.getID() == element.getID()));
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e, "Failed to remove greater elements");
        }finally {
            lock.unlock();
        }
    }

    /**
     * Removes the specified element from the collection.
     *
     * @param element element to remove.
     * @return result of the operation as a Result object. В случае успеха - пустой Result,
     * in case of failure - Result object with an exception and an error message.
     */
    public Result<Void> remove(T element) {
        lock.lock();
        try {
            collection.remove(element);
            ids.remove(element.getID());
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e, "Failed to remove element");
        } finally {
        lock.unlock();
    }
    }

    /**
     * Checks if the collection is empty.
     *
     * @return true if the collection is empty, false otherwise.
     */
    public boolean isEmpty() {
        lock.lock();
        try {
            return collection.isEmpty();
        }finally {
            lock.unlock();
        }
    }

    /**
     * Checks if the collection contains the specified element with ID.
     *
     * @param id the element to check.
     * @return true if the collection contains the specified element, false otherwise.
     */
    public boolean isUnique(long id) {
        lock.lock();
        try {
            return !ids.contains(id);
        }finally {
            lock.unlock();
        }

    }

    public Result<Void> save(){
        lock.lock();
        try {
            Collection_to_file_writer.write();
            return Result.success(null);
        } catch (Exception e) {
            return Result.failure(e, "Коллекция не сохранена");
        }finally {
        lock.unlock();
    }
    }
}