package UserInterface.graphics;

import commandRealization.CommandRealization;
import common.Album;
import common.Coordinates;
import common.LocalizationKeys;
import common.MusicBand;
import common.builders.MusicBandBuilder;
import common.descriptions.CommandDescription;
import common.descriptions.LoadDescription;
import common.descriptions.MusicBandDescription;
import loaders.FromListLoader;
import managers.AbstractLoader;
import managers.BaseTextReceiver;
import managers.LocalizationManager;
import modules.InteractiveMode;
import result.ClearWarning;
import result.Result;
import result.UpdateWarning;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class MainForm extends JFrame {

    private JPanel panel1;
    private JTable table1;
    private JButton removeButton;
    private JButton updateButton;
    private JComboBox<String> commandComboBox;
    private JButton executeButton;
    private JComboBox<String> languageComboBox;
    private JLabel descriptionLabel;
    private JLabel languageLabel;
    private JLabel userLoginLabel;
    private JScrollBar HorScrollBar;
    private JScrollBar vertScrollBar;
    private JPanel MapPanel;
    private CustomPanel customPanel;

    private DefaultTableModel tableModel;

    private final Map<String, String> localizationMap;
    private final LocalizationManager localizationManager;
    private String language;

    private final InteractiveMode interactiveMode;
    private ArrayList<MusicBand> collection;

    private final String userLogin;

    private Long selectedID;
    private final Set<Long> allowedToSelect;

    private MusicBand oldRowValue;
    private MusicBandDescription newRowValue;

    private int sbX;
    private int sbY;
    private int scale;

    private ReentrantLock lock;

    private List<Comparator<MusicBand>> comparators;

    private void sortByIndex(int index){
        refreshTableAndMap(collection.stream().sorted(comparators.get(index)).collect(Collectors.toCollection(ArrayList::new)));
    }

    public MainForm(Map<String, String> localizationMap, InteractiveMode interactiveMode, String userLogin, String language) {
        lock = new ReentrantLock();
        lock.lock();
        try {
            this.language = language;
            localizationManager = new LocalizationManager(localizationMap.get(language), localizationMap.get(language));
            this.localizationMap = localizationMap;
            this.interactiveMode = interactiveMode;
            this.userLogin = userLogin;
            userLoginLabel.setText(userLogin);
            allowedToSelect = new HashSet<>();


            // Задаем параметры окна
            setTitle("Music Bands");
            setSize(1280, 720);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);

            // Создаем модель данных для таблицы
            tableModel = new DefaultTableModel() {
                @Override
                public boolean isCellEditable(int rowIndex, int columnIndex) {
                    return columnIndex > 0;
                }
            };
            MusicBandDescription mbd = new MusicBandDescription();
            mbd.getFields().add(0,
                    new LoadDescription<Long>(LocalizationKeys.ID, LocalizationKeys.ID_FIELD,
                            ((MusicBandBuilder) mbd.getBuilder())::setId, null, Long.class)
            );
            for (LocalizationKeys lk : getColumns(mbd)) {
                tableModel.addColumn(localizationManager.getLine(lk));
            }
            table1.setModel(tableModel);

            // Выводим окно
            setContentPane(panel1);
            setVisible(true);

            // Переключение локали
            for (String key : this.localizationMap.keySet()) {
                languageComboBox.addItem(key);
            }
            languageComboBox.addItemListener(this::ChangeLanguage);
            languageComboBox.setSelectedItem(language);

            // Заполнение коллекции
            Result<Collection<MusicBand>> res;
            try {
                CommandDescription cd = interactiveMode.getCommandDescription("show").clone();
                CommandRealization caller = (CommandRealization) cd.getCaller();
                interactiveMode.addCommandToQueue(caller);
                interactiveMode.executeAll();
                res = (Result<Collection<MusicBand>>) caller.getResult();
            } catch (CloneNotSupportedException e) {
                throw new RuntimeException(e);
            }
            collection = new ArrayList<>(res.getValue().orElse(new ArrayList<>()));

            // Устанавливаем модель выделения строк таблицы
            ListSelectionModel selectionModel = new DefaultListSelectionModel();
            selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            table1.setSelectionModel(selectionModel);
            selectionModel.addListSelectionListener(this::CellSelected); // Добавляем слушателя для отслеживания событий выделения

            // Create a custom cell editor
            MainForm mf = this;
            TableColumnModel columnModel = table1.getColumnModel();
            for (int i = 1; i < table1.getColumnCount(); i++) {
                TableColumn column = columnModel.getColumn(i);
                column.setCellEditor(new DefaultCellEditor(new JTextField()) {
                    @Override
                    public boolean isCellEditable(EventObject anEvent) {
                        if (anEvent instanceof MouseEvent) {
                            if (((MouseEvent)anEvent).getClickCount() >= clickCountToStart){
                                updateButton.setEnabled(false);
                                removeButton.setEnabled(false);
                                return true;
                            }
                            else
                                return false;
                        }
                        return true;
                    }
                    @Override
                    public boolean stopCellEditing() {
                        int selectedRow = table1.getSelectedRow();
                        int selectedCol = table1.getSelectedColumn();
                        JTextField editorComponent = (JTextField) getComponent();
                        String newValue = editorComponent.getText();
                        System.out.println("New value: " + newValue);
                        if (!allowedToSelect.contains(Long.valueOf(table1.getValueAt(selectedRow, 0).toString()))) {
                            editorComponent.setText(table1.getValueAt(selectedRow, selectedCol).toString());
                            JOptionPane.showMessageDialog(mf, localizationManager.getLine(LocalizationKeys.YOU_HAVE_NOT_PERMISSION_TO_SUCH_ELEMENTS), "Error", JOptionPane.ERROR_MESSAGE);
                            updateButton.setEnabled(true);
                            removeButton.setEnabled(true);
                            return super.stopCellEditing();
                        }

                        MusicBandDescription mbd = new MusicBandDescription();
                        ArrayList<String> data = IntStream.range(1, table1.getColumnCount()).
                                mapToObj((int i) -> table1.getValueAt(selectedRow, i).toString()).
                                collect(Collectors.toCollection(ArrayList::new));
                        data.set(selectedCol - 1, newValue);
                        FromListLoader loader = new FromListLoader(data);
                        try {
                            loader.enter(mbd);
                            newRowValue = mbd;
                            updateButton.setEnabled(true);
                            removeButton.setEnabled(true);
                            return super.stopCellEditing();
                        } catch (Exception exception) {
                            String s = parseResult(exception.getMessage());
                            String rule = localizationManager.getLine(LocalizationKeys.YOU_CANNOT_UPDATE_UNTIL);
                            JOptionPane.showMessageDialog(mf, s + "\n" + rule, "Error", JOptionPane.ERROR_MESSAGE);
                            updateButton.setEnabled(true);
                            removeButton.setEnabled(true);
                            return false;
                        }
                    }
                });
            }

            comparators =  Arrays.asList(
                    Comparator.comparing(MusicBand::getID),
                    Comparator.comparing(MusicBand::getName),
                    Comparator.comparing(MusicBand::getX),
                    Comparator.comparing(MusicBand::getY),
                    Comparator.comparing(MusicBand::getCreationDate),
                    Comparator.comparing(MusicBand::getNumberOfParticipants),
                    Comparator.comparing(mb -> mb.getBestAlbum().getName()),
                    Comparator.comparing(mb -> mb.getBestAlbum().getLength()),
                    Comparator.comparing(mb -> mb.getBestAlbum().getTracks()),
                    Comparator.comparing(mb -> mb.getBestAlbum().getSales()),
                    Comparator.comparing(MusicBand::getGenre)
            );

            // Добавляем слушателя на событие клика по колонке
            table1.getTableHeader().addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    int column = table1.columnAtPoint(e.getPoint());
                    sortByIndex(column);
                    System.out.println("Клик по колонке " + column);
                }
            });

            // Инициализация карты объектов
            customPanel = new CustomPanel(table1, lock);
            MapPanel.add(customPanel, BorderLayout.CENTER);

            //Scroll Bars
            sbX = 0;
            sbY = 0;
            scale = 1;
            vertScrollBar.addAdjustmentListener(e -> {
                int newY = e.getValue(); // Получаем новое значение Y из скролл-бара
                customPanel.updateObjectsPosition(0, (sbY - newY) * scale);// Обновляем позицию объектов по Y и перерисовываем панель
                sbY = newY;
            });
            HorScrollBar.addAdjustmentListener(e -> {
                int newX = e.getValue(); // Получаем новое значение X из скролл-бара
                customPanel.updateObjectsPosition((sbX - newX) * scale, 0);// Обновляем позицию объектов по X и перерисовываем панель
                sbX = newX;
            });

            // Заполнение карты объектов и таблицы
            refreshTableAndMap(collection);

            boolean d = false;
            // Заполняем команды в комбо боксик
            for (String key : interactiveMode.getCommandDescriptionMap().keySet()) {
                commandComboBox.addItem(key);
                if (!d) {
                    descriptionLabel.setText(localizationManager.getLine(
                            interactiveMode.getCommandDescriptionMap().get(key).getDescription()));
                    d = true;
                }
            }
            commandComboBox.addItemListener(this::ChangeSelectedCommand);

            // Привязываем кнопкам методы слушатели
            executeButton.addActionListener(this::ExecuteCommand);
            updateButton.addActionListener(this::UpdateCommand);
            removeButton.addActionListener(this::RemoveCommand);


        } finally {
            lock.unlock();
        }
    }

    public void CellSelected(ListSelectionEvent e) {
        lock.lock();
        try {
            // Обработка событий выделения
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table1.getSelectedRow();
                System.out.println("Selected Row: " + selectedRow + "\nID: " + selectedID);

                if (selectedRow != -1) {
                    selectedID = Long.valueOf(table1.getValueAt(selectedRow, 0).toString());

                    MusicBandDescription mbd = new MusicBandDescription();
                    ArrayList<String> data = IntStream.range(1, table1.getColumnCount()).
                            mapToObj((int i) -> table1.getValueAt(selectedRow, i).toString()).
                            collect(Collectors.toCollection(ArrayList::new));
                    FromListLoader loader = new FromListLoader(data);
                    loader.enter(mbd);
                    oldRowValue = mbd.getValue();


                    if (selectedID != null &&
                            oldRowValue != null &&
                            !selectedID.equals(Long.valueOf(table1.getValueAt(selectedRow, 0).toString()))
                    ) {
                        int oldRow = -1;
                        for (int row = 0; row < table1.getRowCount(); row++) {
                            if (Long.valueOf(table1.getValueAt(row, 0).toString()).equals(selectedID)) {
                                oldRow = row;
                                break;
                            }
                        }
                        tableModel.setValueAt(selectedID.toString(), oldRow, 0);
                        tableModel.setValueAt(oldRowValue.getName(), oldRow, 1);
                        tableModel.setValueAt(oldRowValue.getCoordinates().getX().toString(), oldRow, 2);
                        tableModel.setValueAt(oldRowValue.getCoordinates().getY().toString(), oldRow, 3);
                        tableModel.setValueAt(oldRowValue.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), oldRow, 4);
                        tableModel.setValueAt(oldRowValue.getNumberOfParticipants().toString(), oldRow, 5);
                        tableModel.setValueAt(oldRowValue.getBestAlbum().getName(), oldRow, 6);
                        tableModel.setValueAt(oldRowValue.getBestAlbum().getLength().toString(), oldRow, 7);
                        tableModel.setValueAt(oldRowValue.getBestAlbum().getTracks().toString(), oldRow, 8);
                        tableModel.setValueAt(oldRowValue.getBestAlbum().getSales().toString(), oldRow, 9);
                        tableModel.setValueAt(oldRowValue.getGenre().toString(), oldRow, 10);
                    }

                } else {
                    selectedID = null;
                    oldRowValue = null;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    String parseResult(Object arg) {
        if (arg instanceof LocalizationKeys) {
            return localizationManager.getLine((LocalizationKeys) arg);
        } else if (arg instanceof Map<?, ?>) {
            String res = "";
            try{
                Map<?, CommandDescription> map = (Map<?, CommandDescription>) arg;
                for (Object key : map.keySet()) {
                    res += key.toString();
                    res += " - ";
                    res += localizationManager.getLine(map.get(key).getDescription());
                    res += "\n";
                }
                return res;
            } catch (Exception e){

                try{
                Map<LocalizationKeys, ?> map = (Map<LocalizationKeys, ?>) arg;
                for (LocalizationKeys key : map.keySet()) {
                    res += localizationManager.getLine(key);
                    res += " - ";
                    res += map.get(key);
                    res += "\n";
                }
                return res;
                }
                catch (Exception ex){
                    return arg.toString();
                }
            }
        } else if(arg instanceof Map<?, ?>) {
            String res = "";
            Map<LocalizationKeys, ?> map = (Map<LocalizationKeys, ?>) arg;
            for (LocalizationKeys key : map.keySet()) {
                res += localizationManager.getLine(key);
                res += " - ";
                res += map.get(key);
                res += "\n";
            }
            return res;

        }else if (arg instanceof Collection<?>) {
            String res = "";
            for (Object o : (Collection<?>) arg)
                res += o.toString() + "\n";
            return res;
        } else if (arg instanceof String) {
            try {
                String s = (String) arg;
                return parseResult(LocalizationKeys.valueOf(s.trim()));
            } catch (Exception e) {
                return (String) arg;
            }
        } else {
            return arg.toString();
        }
    }


    public void ChangeLanguage(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            ChangeLanguage((String) e.getItem());
        }
    }

    public void ChangeLanguage(String language) {
        lock.lock();
        try {
            this.language = language;
            localizationManager.setLanguage(localizationMap.get(language));
            languageLabel.setText(localizationManager.getLine(LocalizationKeys.LANGUAGE));
            removeButton.setText(localizationManager.getLine(LocalizationKeys.REMOVE_ELEMENT));
            updateButton.setText(localizationManager.getLine(LocalizationKeys.UPDATE_ELEMENT));
            executeButton.setText(localizationManager.getLine(LocalizationKeys.EXECUTE_COMMAND));
            descriptionLabel.setText("");
            commandComboBox.setSelectedItem("");
            ArrayList<String> names = new ArrayList<>();
            MusicBandDescription mbd = new MusicBandDescription();
            mbd.getFields().add(0,
                    new LoadDescription<Long>(LocalizationKeys.ID, LocalizationKeys.ID_FIELD,
                            ((MusicBandBuilder) mbd.getBuilder())::setId, null, Long.class)
            );
            for (LocalizationKeys lk : getColumns(mbd)) {
                names.add(localizationManager.getLine(lk));
            }
            tableModel.setColumnIdentifiers(names.toArray());
        } finally {
            lock.unlock();
        }
    }

    private List<LocalizationKeys> getColumns(LoadDescription<?> ld) {
        List<LocalizationKeys> res = new ArrayList<>();
        for (LoadDescription<?> ld_ : ld.getFields()) {
            if (ld_.getFields().size() == 0) {
                res.add(ld_.getFieldName());
            } else {
                for (LocalizationKeys obj : getColumns(ld_)) {
                    res.add(obj);
                }
            }
        }
        return res;
    }


    public void ChangeSelectedCommand(ItemEvent e) {
        if (e.getStateChange() == ItemEvent.SELECTED) {
            String newKey = (String) e.getItem();
            LocalizationKeys description = interactiveMode.getCommandDescriptionMap().get(newKey).getDescription();
            descriptionLabel.setText(localizationManager.getLine(description));
        }
    }

    public LoadDescription<?> InputArguments(LoadDescription<?> ld) {
        new ArgumentsDialog(ld, localizationManager, this);
        return ld;
    }


    private void ExecuteCommand(ActionEvent e) {
        try {
            CommandDescription command = interactiveMode.getCommandDescription((String) commandComboBox.getSelectedItem()).clone();

            if (command.getOneLineArguments() != null) {
                command.getOneLineArguments().forEach(this::InputArguments);
                for (LoadDescription<?> ld : command.getOneLineArguments()) {
                    if (ld.getValue() == null) {
                        return;
                    }
                }
            }
            if (command.getArguments() != null) {
                command.getArguments().forEach(this::InputArguments);
                for (LoadDescription<?> ld : command.getArguments()) {
                    if (ld.getValue() == null) {
                        return;
                    }
                }
            }

            Result<?> res = SendCommand(command);
            if (res.isSuccess()) {
                String message = parseResult(res.getValue().isPresent() ? res.getValue().get() : localizationManager.getLine(LocalizationKeys.SUCCESS));
                JOptionPane.showMessageDialog(this, message, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String s = parseResult(res.getMessage());
                JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception exception) {
            String s = parseResult(exception.getMessage());
            JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void UpdateCommand(ActionEvent e) {
        try {
            CommandDescription command = interactiveMode.getCommandDescription("update").clone();

            ArrayList<String> id_arr = new ArrayList<>();
            id_arr.add(selectedID.toString());
            FromListLoader loader = new FromListLoader(id_arr);
            loader.enter(command.getOneLineArguments().get(0));

            ArrayList<LoadDescription<?>> arguments = new ArrayList<LoadDescription<?>>();
            arguments.add(newRowValue);
            command.setArguments(arguments);

            table1.clearSelection();

            Result<?> res = SendCommand(command);
            if (res.isSuccess()) {
                oldRowValue = null;
                newRowValue = null;
                selectedID = null;

                table1.clearSelection();

                String message = parseResult(res.getValue().isPresent() ? res.getValue().get() : localizationManager.getLine(LocalizationKeys.SUCCESS));
                JOptionPane.showMessageDialog(this, message, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String s = parseResult(res.getMessage());
                JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception exception) {
            String s = parseResult(exception.getMessage());
            JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void RemoveCommand(ActionEvent e) {
        try {
            CommandDescription command = interactiveMode.getCommandDescription("remove_by_id").clone();

            ArrayList<String> id_arr = new ArrayList<>();
            id_arr.add(selectedID.toString());
            FromListLoader loader = new FromListLoader(id_arr);
            loader.enter(command.getOneLineArguments().get(0));

            table1.clearSelection();

            Result<?> res = SendCommand(command);
            if (res.isSuccess()) {
                oldRowValue = null;
                newRowValue = null;
                selectedID = null;

                table1.clearSelection();

                String message = parseResult(res.getValue().isPresent() ? res.getValue().get() : localizationManager.getLine(LocalizationKeys.SUCCESS));
                JOptionPane.showMessageDialog(this, message, "Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                String s = parseResult(res.getMessage());
                JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception exception) {
            String s = parseResult(exception.getMessage());
            JOptionPane.showMessageDialog(this, s, "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Result<?> SendCommand(CommandDescription command) {
        Result<?> res;
        try {
            command.setLoader(new AbstractLoader(new BaseTextReceiver() {
                @Override
                public void print(String s) {

                }

                @Override
                public void println(String s) {

                }
            }) {
                @Override
                public <T extends LoadDescription<?>> T enter(T t) {
                    System.out.println(t);
                    System.out.println(t.getValue());
                    return null;
                }

                @Override
                public <T extends LoadDescription<?>> T enterDate(T t) {
                    return null;
                }

                @Override
                public <T extends LoadDescription<Enum>> T enterEnum(T t) {
                    return null;
                }

                @Override
                public <T extends LoadDescription<?>> T enterWrapper(T t) {
                    return null;
                }

                @Override
                public LoadDescription<String> enterString(LoadDescription<String> loadDescription) {
                    return null;
                }
            });
            CommandRealization caller = (CommandRealization) command.getCaller();
            caller.setCommandDescription(command);
            caller.call();
            interactiveMode.addCommandToHistory(command.getName());
            interactiveMode.clearCommandQueue();
            res = caller.getResult();
            return res;
        } catch (Exception exception) {
            return Result.failure(exception);
        }
    }


    private void UpdateMapElement(UpdateWarning uw) {
        RemoveMapElement(uw.getId());
        AddMapElement(uw);
    }

    private void AddMapElement(UpdateWarning uw) {
        MusicBand musicBand = uw.getValue().get();

        collection.add(musicBand);

        ArrayList<String> row = new ArrayList<String>();
        row.add(musicBand.getID() + "");
        row.add(musicBand.getName());
        row.add(musicBand.getCoordinates().getX().toString());
        row.add(musicBand.getCoordinates().getY().toString());
        row.add(musicBand.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        row.add(musicBand.getNumberOfParticipants().toString());
        row.add(musicBand.getBestAlbum().getName());
        row.add(musicBand.getBestAlbum().getLength().toString());
        row.add(musicBand.getBestAlbum().getTracks().toString());
        row.add(musicBand.getBestAlbum().getSales().toString());
        row.add(musicBand.getGenre().toString());
        if (musicBand.getOwnerLogin().equals(this.userLogin)) {
            allowedToSelect.add(musicBand.getID());
        }
        tableModel.addRow(row.toArray());

        customPanel.addMusicBand(musicBand);
    }

    private void RemoveMapElement(Long id) {
        collection = collection.stream().
                filter((MusicBand mb) -> mb.getID() != id).
                collect(Collectors.toCollection(ArrayList::new));
        refreshTableAndMap(collection);
    }

    private void clearUserElements(String userLogin) {
        collection = collection.stream().
                filter((MusicBand mb) -> !mb.getOwnerLogin().equals(userLogin)).
                collect(Collectors.toCollection(ArrayList::new));
        refreshTableAndMap(collection);
    }

    private void refreshTableAndMap(ArrayList<MusicBand> toShow) {
        tableModel.setRowCount(0);
        for (MusicBand musicBand : toShow) {
            ArrayList<String> row = new ArrayList<String>();
            row.add(musicBand.getID() + "");
            row.add(musicBand.getName());
            row.add(musicBand.getCoordinates().getX().toString());
            row.add(musicBand.getCoordinates().getY().toString());
            row.add(musicBand.getCreationDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            row.add(musicBand.getNumberOfParticipants().toString());
            row.add(musicBand.getBestAlbum().getName());
            row.add(musicBand.getBestAlbum().getLength().toString());
            row.add(musicBand.getBestAlbum().getTracks().toString());
            row.add(musicBand.getBestAlbum().getSales().toString());
            row.add(musicBand.getGenre().toString());
            if (musicBand.getOwnerLogin().equals(this.userLogin)) {
                allowedToSelect.add(musicBand.getID());
            }
            tableModel.addRow(row.toArray());
        }

        customPanel.clearMusicBand();
        for (MusicBand musicBand : toShow) {
            customPanel.addMusicBand(musicBand);
        }

        vertScrollBar.setValue(0);
        HorScrollBar.setValue(0);
        vertScrollBar.setMaximum((int) (collection.stream().map(MusicBand::getCoordinates).mapToDouble(Coordinates::getY).max().orElse(2000) / scale));
        HorScrollBar.setMaximum((int) (collection.stream().map(MusicBand::getCoordinates).mapToLong(Coordinates::getX).max().orElse(2000) / scale) / scale);
    }

    public void parseWarning(UpdateWarning uw) {
        if (uw instanceof ClearWarning) {
            clearUserElements(((ClearWarning) uw).getOwnerLogin());
        } else {
            lock.lock();
            try {
                if (IntStream.range(0, table1.getRowCount()).
                        mapToLong((int i) -> Long.parseLong(table1.getValueAt(i, 0).toString())).
                        anyMatch((long i) -> i == uw.getId())
                ) {
                    if (uw.getValue().isPresent()) {
                        UpdateMapElement(uw);
                    } else {
                        RemoveMapElement(uw.getId());
                    }
                } else {
                    AddMapElement(uw);
                }
            } finally {
                lock.unlock();
            }
        }
    }

}
