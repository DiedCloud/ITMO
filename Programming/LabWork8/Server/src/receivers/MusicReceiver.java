package receivers;

import common.LocalizationKeys;
import common.builders.MusicBandBuilder;
import common.descriptions.LoadDescription;
import main.Main;
import managers.connection.Notifier;
import managers.file.*;
import common.Album;
import common.Collection;
import common.MusicBand;
import common.descriptions.MusicBandDescription;
import managers.file.decorators.DataBase.DBReader;
import managers.file.decorators.DataBase.DBWriter;
import result.ClearWarning;
import result.Result;
import result.UpdateWarning;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;


/**
 * Класс, хранящий коллекцию и выполняющий все операции с ней. Принимает запросы на выполнение команды. Реализует паттерн одиночка.
 *
 * @author Фролов К.Д.
 * @author Качанов Д.В.
 */
public class MusicReceiver extends Receiver<MusicBand> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    /**
     * Ссылка на текущий {@link Receiver}. Класс реализует паттерн одиночка.
     */
    private static MusicReceiver instance;

    /**
     * Одиночка. Если объекта нет - создадим, иначе вернем тот, что есть.
     *
     * @return ссылка на существующий объект {@link Receiver}
     */
    public static MusicReceiver GetInstance() throws Exception {
        if (instance == null) {
            instance = new MusicReceiver();
        }
        return instance;
    }

    /**
     * Здесь происходит вся инициализация: инициализация коллекции, подключение к файлу, его чтение и запись данных в коллекцию. Происходит одна один раз - спасибо паттерну одиночка.
     */
    private MusicReceiver() throws Exception {
        try {
            Collection<MusicBand> tmp = new Collection<>();

            //, new LoadDescription<>("YourLogin", "OwnerLogin", musicBandBuilder::setName, null, String.class) - добавить это и id в MusicBandDescription

            collection_to_file_writer = new AbstractWriter<>("MusicBands") {
                @Override
                public void write() throws Exception {}

                @Override
                public Result<Boolean> insert(MusicBand musicBand) {
                    return null;
                }

                @Override
                public Result<Boolean> update(MusicBand musicBand, long i) {
                    return null;
                }

                @Override
                public Result<Boolean> remove(long l) {
                    return null;
                }

                @Override
                public Result<Boolean> remove(String col, String val) {
                    return null;
                }
            };
            AbstractReader<MusicBand> Collection_from_file_loader = new AbstractReader<>("MusicBands", new MusicBandDescription(), tmp) {
                @Override
                public Result<Collection<MusicBand>> read() {
                    return null;
                }
            };

            MusicBandDescription mbd_writer = new MusicBandDescription();
            mbd_writer.getFields().add(new LoadDescription<String>(LocalizationKeys.LOGIN_COMMAND, LocalizationKeys.OWNER_LOGIN, ((MusicBandBuilder) mbd_writer.getBuilder())::setOwnerLogin, null, String.class));
            collection_to_file_writer = new DBWriter<>("MusicBands", collection_to_file_writer, mbd_writer);

            MusicBandDescription mbd_reader = new MusicBandDescription();
            mbd_reader.getFields().add(0, new LoadDescription<Long>(LocalizationKeys.ID, LocalizationKeys.ID_FIELD, ((MusicBandBuilder) mbd_reader.getBuilder())::setId, null, Long.class));
            mbd_reader.getFields().add(new LoadDescription<String>(LocalizationKeys.LOGIN_COMMAND, LocalizationKeys.OWNER_LOGIN, ((MusicBandBuilder) mbd_reader.getBuilder())::setOwnerLogin, null, String.class));
            Collection_from_file_loader = new DBReader<>("MusicBands", mbd_reader, Collection_from_file_loader, tmp);


            collection = new common.Collection<>(Collection_from_file_loader, collection_to_file_writer);
        } catch (NullPointerException e) {
            Main.logger.error(e.getMessage(), e);
            throw new NullPointerException("Error with collection loading");
        }
    }

    @Override
    public Result<Void> add(MusicBand obj) {
       Result<Void> result = super.add(obj);
       if(result.isSuccess())
           Notifier.getInstance().warnAll(UpdateWarning.warning(obj, obj.getID()));
       return result;
    }

    public Result<Void> removeById(long id) {
        Result<Void> result = super.removeById(id);
        if(result.isSuccess())
            Notifier.getInstance().warnAll(UpdateWarning.warning(null, id));
        return result;
    }

    /**
     * Возвращает все элементы коллекции в массиве
     *
     * @return объект {@link Result} с ответом
     */
    public Result<common.Collection<MusicBand>> showElementsOfCollection() {
            ReentrantLock lock = new ReentrantLock();
            lock.lock();
            try {
                return Result.success(collection);
            }finally {
                lock.unlock();
            }
    }



    /**
     * Method to add an element to the collection if it is greater than all elements of the collection
     *
     * @return the execution result of the command (success/failure)
     */
    public Result<Void> addIfMax(MusicBand obj) {
        MusicBand newBand, maxBand;
        newBand = obj;
            Result<MusicBand> maxBandResult = collection.getMax();
            if (maxBandResult.isSuccess() && maxBandResult.getValue().isPresent()) {
                maxBand = maxBandResult.getValue().get();
            } else {
                return Result.failure(maxBandResult.getError().orElse(null), maxBandResult.getMessage());
            }
            if (newBand.compareTo(maxBand) > 0) {
                return this.add(obj);
            } else {
                return Result.success(null, LocalizationKeys.NOT_THE_GREATEST_ELEMENT);
            }
    }

    /*public Result<List<Result<?>>> executeQueue(List<CommandDescription> queue, Invoker invoker) {
        Collection<MusicBand> backUp = collection.clone();
        List<Result<?>> results = new ArrayList<>();
        for (CommandDescription cd : queue) {
            Result<?> tmp_res = invoker.executeCommand(cd.getName(), cd);
            if (tmp_res.isSuccess()) {
                results.add(tmp_res);
            } else {
                collection = backUp.clone();
                return Result.failure(tmp_res.getError().get(), tmp_res.getMessage()+"\nСкрипт не исполнен. Загружено состояние коллекции, которое было до начла исполнения.");
            }
        }
        return Result.success(results);
    }*/

    /**
     * Method for filtering collection by best album
     *
     * @param album best album
     * @return result of executing command (success/error)
     */
    public Result<TreeSet<MusicBand>> filterByBestAlbum(Album album) {
            return Result.success(
                    collection.getCollection()
                            .stream()
                            .filter(
                                    (MusicBand band) -> band.getBestAlbum().equals(album))
                            .collect(Collectors.toCollection(TreeSet::new)));
    }

    /**
     * Method for printing info about max element of collection by best album
     *
     * @return result of executing command (success/error)
     */
    public Result<MusicBand> maxByBestAlbum() {
        try {
            if (collection.getSize() == 0) {
                return Result.failure(new Exception(""), LocalizationKeys.ERROR_EMPTY_COLLECTION);
            }
            try {
                MusicBand maxAlbumBand = collection.getCollection()
                        .stream()
                        .max(Comparator.comparing(band -> band.getBestAlbum().getSales()))
                        .orElse(null);
                if (maxAlbumBand == null) {
                    return Result.failure(new Exception(""), LocalizationKeys.FAIL);
                }
                return Result.success(maxAlbumBand);
            } catch (Exception e) {
                return Result.failure(e);
            }
        }catch (Exception e){
            return Result.failure(e);
        }
    }

    /**
     * Method to count the elements in the collection whose bestAlbum field is equal to the specified value
     *
     * @param bestAlbum
     * @return the execution result of the command (success/failure) with the number of elements
     */
    public Result<?> countByBestAlbum(Album bestAlbum) {
            long count = collection.getCollection().stream()
                    .filter((MusicBand band) -> band.getBestAlbum().equals(bestAlbum)).count();
            return Result.success("Number of elements with best album equal to " + bestAlbum + " is " + count);
    }

    public Result<Void> clear(String userLogin) {
            Result<Boolean> result = collection_to_file_writer.remove("OwnerLogin", userLogin);
            if (result.isSuccess()) {
                collection.setCollection(
                        collection.getCollection().stream()
                                .filter((i) -> !i.getOwnerLogin().equals(userLogin))
                                .collect(Collectors.toCollection(TreeSet::new))
                );
                Main.logger.info("User " + userLogin + " elements of collection cleared");
                Notifier.getInstance().warnAll(ClearWarning.warning(userLogin));
                return Result.success(null);
            } else {
                Main.logger.error("Collection wasn't cleared. " + result.getMessage());
                return Result.failure(result.getError().orElse(null), result.getMessage());
            }
    }

    /**
     * Abstract method for removing an element from the collection by its ID.
     *
     * @return a Result object that indicates the success or failure of the operation.
     */
    public Result<Void> removeById(long id, String login) {
            boolean is_present = collection.getCollection().stream()
                    .anyMatch((MusicBand element) -> (element.getID() == id && element.getOwnerLogin().equals(login)));
            if (is_present) {
                Result<Boolean> remove_res = collection_to_file_writer.remove(id);
                if (remove_res.isSuccess()) {
                    collection.setCollection(collection
                            .getCollection()
                            .stream()
                            .filter((MusicBand band) -> band.getID() != id)
                            .collect(Collectors.toCollection(TreeSet::new))
                    );
                    Main.logger.info("Element removed");

                    Notifier.getInstance().warnAll(UpdateWarning.warning(null, id));
                    return Result.success(null, LocalizationKeys.SUCCESS);
                } else {
                    return Result.failure(remove_res.getError().orElse(null), remove_res.getMessage());
                }
            } else {
                return Result.failure(null,LocalizationKeys.YOU_HAVE_NOT_PERMISSION_TO_SUCH_ELEMENTS);
            }

    }


    /**
     * Удаляет из коллекции все элементы, превышающие заданный
     *
     * @param element объект, с которым будут сравнивать объекты коллекции
     * @return {@link Result} of executing command (success/error)
     */
    public Result<Void> removeGreater(MusicBand element) {
            HashSet<MusicBand> bands_to_delete = collection.getCollection().stream()
                    .filter(mb -> mb.compareTo(element) > 0 && mb.getOwnerLogin().equals(element.getOwnerLogin()))
                    .collect(Collectors.toCollection(HashSet::new));
            int counter = 0;
            for (MusicBand mb : bands_to_delete) {
                Result<?> remove_res = removeById(mb.getID(), mb.getOwnerLogin());
                if (remove_res.isSuccess()) {
                    counter++;
                }
            }
            return Result.success(null, LocalizationKeys.SUCCESS);
    }


    /**
     * Abstract method for updating an element of the collection by ID.
     *
     * @return a Result object that indicates the status of the update operation.
     */
    public Result<Void> updateById(long id, MusicBand newElement) {
            boolean is_present = collection.getCollection().stream()
                    .anyMatch((MusicBand element) -> (element.getID() == id && element.getOwnerLogin().equals(newElement.getOwnerLogin())));
            if (!is_present) {
                return Result.failure(null, LocalizationKeys.YOU_HAVE_NOT_PERMISSION_TO_SUCH_ELEMENTS);
            }

            Result<Boolean> update_res = collection_to_file_writer.update(newElement, id);

            if (update_res.isSuccess()) {
                try {
                    Result<Void> result = collection.remove(findById(id));
                    if (result.isSuccess()) {
                        newElement.setID(id);
                        result = collection.add(newElement);
                        if (result.isSuccess()) {
                            Main.logger.info("Element updated");
                            return Result.success(null, LocalizationKeys.SUCCESS);
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
                    return Result.failure(e, LocalizationKeys.FAIL);
                }
            } else {
                return Result.failure(update_res.getError().orElse(null), update_res.getMessage());
            }
    }

    public Result<java.util.Collection<MusicBand>> getCollection(){
        return Result.success(collection.getCollection());
    }
}
