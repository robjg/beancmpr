package beancmpr.poi;

import dido.poi.BookOut;
import dido.poi.HeaderRowOut;
import dido.poi.RowOut;
import dido.poi.RowsOut;
import dido.poi.data.PoiRowsOut;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.results.MatchResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Writes cells from an {@link MatchResult}.
 *
 */
public class MatchResultCellWriter implements AutoCloseable {

    public static final String RESULT_TYPE_HEADER = "ResultType";

    public static final String RESULT_TYPE_EQUAL = "EQUAL";

    public static final String RESULT_TYPE_NOT_EQUAL = "DIFFERENT";

    public static final String RESULT_TYPE_MISSING = "MISSING";

    public static final String DEFAULT_X_PREFIX = "X_";

    public static final String DEFAULT_Y_PREFIX = "Y_";
    private final RowsOut rowsOut;

    private final List<ResultItemOut> template;

    private final boolean autoWidth;

    private final boolean autoFilter;

    private MatchResultCellWriter(RowsOut rowsOut,
                                  List<ResultItemOut> template,
                                  boolean autoWidth,
                                  boolean autoFilter) {
        this.rowsOut = rowsOut;
        this.template = template;
        this.autoWidth = autoWidth;
        this.autoFilter = autoFilter;
    }

    static class Options {

        private String sheetName;

        private int firstRow;

        private int firstColumn;

        private String xPrefix;

        private String yPrefix;

        private boolean autoWidth;

        private boolean autoFilter;

        public Options setSheetName(String sheetName) {
            this.sheetName = sheetName;
            return this;
        }

        public Options setFirstRow(int firstRow) {
            this.firstRow = firstRow;
            return this;
        }

        public Options setFirstColumn(int firstColumn) {
            this.firstColumn = firstColumn;
            return this;
        }

        public Options setxPrefix(String xPrefix) {
            this.xPrefix = xPrefix;
            return this;
        }

        public Options setyPrefix(String yPrefix) {
            this.yPrefix = yPrefix;
            return this;
        }

        public Options setAutoWidth(boolean autoWidth) {
            this.autoWidth = autoWidth;
            return this;
        }

        public Options setAutoFilter(boolean autoFilter) {
            this.autoFilter = autoFilter;
            return this;
        }

        public MatchResultCellWriter createFor(BookOut bookOut, MatchableMetaData metaData) {

            Sheet sheet = bookOut.getOrCreateSheet(this.sheetName);

            PoiRowsOut rowsOut = new PoiRowsOut(sheet,
                    bookOut.createStyles(new CmprStyleProivderFactory()),
                    firstRow,
                    firstColumn);

            List<ResultItemOut> template = new ArrayList<>();
            int column = 1;

            String xPrefix = Objects.requireNonNullElse(this.xPrefix, DEFAULT_X_PREFIX);
            String yPrefix = Objects.requireNonNullElse(this.yPrefix, DEFAULT_Y_PREFIX);

            template.add(new TypeOut(column, xPrefix, yPrefix));

            for (int keyIndex = 0; keyIndex < metaData.getKeyProperties().size(); ++keyIndex) {

                template.add(new KeyOut(++column, metaData, keyIndex));
            }

            for (int comparisonIndex = 0; comparisonIndex < metaData.getValueProperties().size(); ++comparisonIndex) {

                template.add(new ComparisonOut(++column, metaData, comparisonIndex,
                        xPrefix, yPrefix));
                ++column;
            }

            for (int otherIndex = 0; otherIndex < metaData.getOtherProperties().size(); ++otherIndex) {

                template.add(new OtherOut(++column, metaData, otherIndex,
                        xPrefix, yPrefix));
                ++column;
            }

            return new MatchResultCellWriter(rowsOut, template, autoWidth, autoFilter);
        }
    }

    public static Options withOptions() {
        return new Options();
    }

    public void writeHeader() {

        HeaderRowOut headerRowOut = rowsOut.headerRow(CmprStyleProivderFactory.HEADING_STYLE);

        for (ResultItemOut out : template) {
            out.writeHeader(headerRowOut);
        }
    }

    void write(MatchResult matchResult) {

        rowsOut.nextRow();
        RowOut rowOut = rowsOut.getRowOut();

        for (ResultItemOut out : template) {
            out.setValue(rowOut, matchResult);
        }
    }

    @Override
    public void close() {

        if (autoFilter) {
            rowsOut.autoFilter();
        }

        if (autoWidth) {
            rowsOut.autoWidth();
        }
    }

    interface ResultItemOut {

        void writeHeader(HeaderRowOut headerRowOut);

        void setValue(RowOut rowOut, MatchResult result);

    }


    static class TypeOut implements ResultItemOut {

        private final int sheetColumn;

        private final String xPrefix;

        private final String yPrefix;

        TypeOut(int sheetColumn, String xPrefix, String yPrefix) {
            this.sheetColumn = sheetColumn;
            this.xPrefix = xPrefix;
            this.yPrefix = yPrefix;
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {
            headerRowOut.setHeader(sheetColumn, RESULT_TYPE_HEADER);
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cell = rowOut.getCell(sheetColumn, CellType.STRING);
            String resultType;
            switch (result.getResultType()) {
                case EQUAL:
                    resultType = RESULT_TYPE_EQUAL;
                    break;
                case X_MISSING:
                    resultType = xPrefix + RESULT_TYPE_MISSING;
                    break;
                case Y_MISSING:
                    resultType = yPrefix + RESULT_TYPE_MISSING;
                    break;
                case NOT_EQUAL:
                    resultType = RESULT_TYPE_NOT_EQUAL;
                    break;
                default:
                    throw new IllegalStateException("Unknown Result Type.");
            }

            cell.setCellValue(resultType);
            cell.setCellStyle(rowOut.styleFor(CmprStyleProivderFactory.BEANCMPR_TYPE_STYLE));
        }
    }

    static class KeyOut implements ResultItemOut {

        private final int sheetIndex;

        private final String name;

        private final Type type;
        private final int keyIndex;

        KeyOut(int sheetIndex, MatchableMetaData metaData, int keyIndex) {
            this.sheetIndex = sheetIndex;
            this.keyIndex = keyIndex;
            this.name = metaData.getKeyProperties().get(keyIndex);
            this.type = metaData.getPropertyType(this.name);
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {
            headerRowOut.setHeader(sheetIndex, name);
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cell = createAndSetCell(rowOut, sheetIndex, type, result.getKey(keyIndex));
            String styleName;
            switch (result.getResultType()) {
                case EQUAL:
                    styleName = CmprStyleProivderFactory.BEANCMPR_KEY_MATCH_STYLE;
                    break;
                case X_MISSING:
                case Y_MISSING:
                    styleName = CmprStyleProivderFactory.BEANCMPR_KEY_MISSING_STYLE;
                    break;
                case NOT_EQUAL:
                    styleName = CmprStyleProivderFactory.BEANCMPR_KEY_DIFF_STYLE;
                    break;
                default:
                    throw new IllegalStateException("Unknown Result Type.");
            }
            cell.setCellStyle(rowOut.styleFor(styleName));
        }
    }

    static class ComparisonOut implements ResultItemOut {

        private final int sheetColumn;

        private final String name;

        private final Type type;

        private final int comparisonIndex;

        private final String xPrefix;

        private final String yPrefix;

        ComparisonOut(int sheetColumn, MatchableMetaData metaData, int comparisonIndex,
                      String xPrefix, String yPrefix) {
            this.sheetColumn = sheetColumn;
            this.comparisonIndex = comparisonIndex;
            this.name = metaData.getValueProperties().get(comparisonIndex);
            this.type = metaData.getPropertyType(this.name);
            this.xPrefix = xPrefix;
            this.yPrefix = yPrefix;
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {

            headerRowOut.setHeader(sheetColumn, xPrefix + name);
            headerRowOut.setHeader(sheetColumn + 1, yPrefix + name);
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cellX = createAndSetCell(rowOut, sheetColumn, type, result.getXValue(comparisonIndex));
            Cell cellY = createAndSetCell(rowOut, sheetColumn + 1, type, result.getYValue(comparisonIndex));

            String styleNameX;
            String styleNameY;
            switch (result.getResultType()) {
                case EQUAL:
                    styleNameX = CmprStyleProivderFactory.BEANCMPR_MATCH_STYLE;
                    styleNameY = CmprStyleProivderFactory.BEANCMPR_MATCH_STYLE;
                    break;
                case X_MISSING:
                    styleNameX = CmprStyleProivderFactory.BEANCMPR_BLANK_STYLE;
                    styleNameY = CmprStyleProivderFactory.BEANCMPR_MISSING_STYLE;
                    break;
                case Y_MISSING:
                    styleNameX = CmprStyleProivderFactory.BEANCMPR_MISSING_STYLE;
                    styleNameY = CmprStyleProivderFactory.BEANCMPR_BLANK_STYLE;
                    break;
                case NOT_EQUAL:
                    if (result.getComparison(comparisonIndex).getResult() == 0 ) {
                        styleNameX = CmprStyleProivderFactory.BEANCMPR_MATCH_STYLE;
                        styleNameY = CmprStyleProivderFactory.BEANCMPR_MATCH_STYLE;
                    }
                    else {
                        styleNameX = CmprStyleProivderFactory.BEANCMPR_DIFF_STYLE;
                        styleNameY = CmprStyleProivderFactory.BEANCMPR_DIFF_STYLE;
                    }
                    break;
                default:
                    throw new IllegalStateException("Unknown Result Type.");
            }

            cellX.setCellStyle(rowOut.styleFor(styleNameX));
            cellY.setCellStyle(rowOut.styleFor(styleNameY));
        }
    }

    static class OtherOut implements ResultItemOut {

        private final int sheetColumn;

        private final String name;

        private final Type type;

        private final int otherIndex;

        private final String xPrefix;

        private final String yPrefix;

        OtherOut(int sheetColumn, MatchableMetaData metaData, int otherIndex,
                      String xPrefix, String yPrefix) {
            this.sheetColumn = sheetColumn;
            this.otherIndex = otherIndex;
            this.name = metaData.getOtherProperties().get(otherIndex);
            this.type = metaData.getPropertyType(this.name);
            this.xPrefix = xPrefix;
            this.yPrefix = yPrefix;
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {

            headerRowOut.setHeader(sheetColumn, xPrefix + name);
            headerRowOut.setHeader(sheetColumn + 1, yPrefix + name);
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cellX = createAndSetCell(rowOut, sheetColumn, type, result.getXOther(otherIndex));
            Cell cellY = createAndSetCell(rowOut, sheetColumn + 1, type, result.getYOther(otherIndex));
        }
    }

    interface CellCreateAndSet {
        Cell createAndSetCell(RowOut rowOut, int index, Object value);
    }

    static class BlankCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            return rowOut.getCell(index, CellType.BLANK);
        }
    }

    static class DoubleCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            Cell cell = rowOut.getCell(index, CellType.NUMERIC);
            cell.setCellValue(((Number) value).doubleValue());
            return cell;
        }
    }

    static class IntCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            Cell cell = rowOut.getCell(index, CellType.NUMERIC);
            cell.setCellValue(((Number) value).intValue());
            return cell;
        }
    }

    static class BooleanCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            Cell cell = rowOut.getCell(index, CellType.BOOLEAN);
            cell.setCellValue((boolean) value);
            return cell;
        }
    }

    static class CharacterCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            Cell cell = rowOut.getCell(index, CellType.STRING);
            cell.setCellValue(String.valueOf((char) value));
            return cell;
        }
    }

    static class StringCell implements CellCreateAndSet {

        @Override
        public Cell createAndSetCell(RowOut rowOut, int index, Object value) {
            Cell cell = rowOut.getCell(index, CellType.STRING);
            cell.setCellValue(value.toString());
            return cell;
        }
    }

    private static final HashMap<Class<?>, CellCreateAndSet> cellSetters = new HashMap<>();

    static {
        cellSetters.put(Boolean.class, new BooleanCell());
        cellSetters.put(Byte.class, new IntCell());
        cellSetters.put(Short.class, new IntCell());
        cellSetters.put(Character.class, new CharacterCell());
        cellSetters.put(Integer.class, new IntCell());
        cellSetters.put(Long.class, new IntCell());
        cellSetters.put(Float.class, new DoubleCell());
        cellSetters.put(Double.class, new DoubleCell());
    }


    static Cell createAndSetCell(RowOut rowOut, int index, Type type, Object value) {

        CellCreateAndSet cellCreateAndSet;

        if (value == null) {
            cellCreateAndSet = new BlankCell();
        } else {
            cellCreateAndSet = cellSetters.get(type);
            if (cellCreateAndSet == null) {
                cellCreateAndSet = new StringCell();
            }
        }

        return cellCreateAndSet.createAndSetCell(rowOut, index, value);
    }

}
