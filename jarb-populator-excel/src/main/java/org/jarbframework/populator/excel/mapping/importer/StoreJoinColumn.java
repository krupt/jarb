package org.jarbframework.populator.excel.mapping.importer;

import org.jarbframework.populator.excel.metamodel.Definition;
import org.jarbframework.populator.excel.metamodel.PropertyDefinition;
import org.jarbframework.populator.excel.workbook.Sheet;
import org.jarbframework.populator.excel.workbook.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Stores the value of a JoinColumn.
 * @author Sander Benschop
 * @author Willem Eppen
 *
 */
public final class StoreJoinColumn {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreJoinColumn.class);

    /** Private constructor. */
    private StoreJoinColumn() {
    }

    /**
     * Stores a JoinColumn in ExcelRow.
     * @param excel Representation of excel file
     * @param definition ClassDefinition used to determine columnPosition
     * @param columnDefinition ColumnDefinition is the superclass of Column, JoinColumn and JoinTable.
     * @param rowPosition Vertical position number of the excelRecord
     * @param excelRow ExcelRow to save to.
     */
    public static void storeValue(Workbook excel, Definition<?> definition, PropertyDefinition columnDefinition, Integer rowPosition, ExcelRow excelRow) {
        Sheet sheet = excel.getSheet(definition.getTableName());
        Object cellValue = sheet.getValueAt(rowPosition, columnDefinition.getColumnName());
        LOGGER.debug("field: " + columnDefinition.getName() + " column: " + columnDefinition.getColumnName() + " value:[" + cellValue + "]");
        if (cellValue != null) {
            // Sets the Key
            Key keyValue = new JoinColumnKey();
            keyValue.setKeyValue(cellValue);
            keyValue.setForeignClass(columnDefinition.getField().getType());
            excelRow.addValue(columnDefinition, keyValue);
        }
    }
}
