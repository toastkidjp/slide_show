package jp.toastkid.slideshow.converter;

import java.util.List;
import java.util.stream.IntStream;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Control;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.text.Text;

/**
 * Table builder.
 *
 * @author Toast kid
 *
 */
public class TableBuilder {

    /** {@link TableView} object. */
    private final TableView<ObservableList<String>> tableView;

    /**
     * Initialize table object.
     *
     */
    public TableBuilder() {
        tableView = new TableView<>();
        tableView.setFocusTraversable(false);
        tableView.setStyle("-fx-font-size: 30pt;");
    }

    /**
     * Add table's line.
     * @param lineElements
     */
    public void addTableLine(final List<String> lineElements) {
        final ObservableList<TableColumn<ObservableList<String>, ?>> tableColumns = tableView.getColumns();
        if (tableColumns == null) {
            return;
        }
        tableView.getItems().add(FXCollections.observableArrayList(lineElements));
    }

    /**
     * Get {@link TableView} object.
     * @return
     */
    public TableView<ObservableList<String>> get() {
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return this.tableView;
    }

    /**
     * Has any column?
     * @return
     */
    public boolean hasColumns() {
        return !this.tableView.getColumns().isEmpty();
    }

    /**
     * Set first header column.
     * @param tableColumns
     */
    public void setColumns(final List<TableColumn<ObservableList<String>, String>> tableColumns) {
        IntStream.range(0, tableColumns.size()).forEach(i -> {
            final TableColumn<ObservableList<String>, String> tableColumn = tableColumns.get(i);
            tableColumn
                .setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue().get(i)));
            tableColumn.setCellFactory(param -> {
                    final TableCell<ObservableList<String>, String> cell = new TableCell<>();
                    final Text text = new Text();
                    cell.setGraphic(text);
                    cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
                    text.wrappingWidthProperty().bind(tableColumn.widthProperty());
                    text.textProperty().bind(cell.itemProperty());
                    return cell;
            });
            tableView.getColumns().add(tableColumn);
        });
    }

}
