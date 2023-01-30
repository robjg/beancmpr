package beancmpr.poi;

import dido.poi.BookOutProvider;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.oddjob.beancmpr.matchables.MatchableMetaData;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.beancmpr.results.MatchResultType;
import org.oddjob.dido.poi.BookOut;
import org.oddjob.dido.poi.HeaderRowOut;
import org.oddjob.dido.poi.RowOut;
import org.oddjob.dido.poi.RowsOut;
import org.oddjob.dido.poi.data.PoiRowsOut;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class PoiMatchResultsService implements Consumer<MatchResult> {

    private volatile String name;

    private volatile BookOutProvider bookOutProvider;

    private volatile String sheetName;

    private volatile int firstRow;

    private volatile int firstColumn;

    private volatile BookOut bookOut;

    private volatile CellWriter cellWriter;

    private volatile Consumer<? super MatchResult> to;

    public void start() throws Exception {

        this.bookOut = bookOutProvider.provideBookOut();
    }

    @Override
    public void accept(MatchResult matchResult) {

        if (cellWriter == null) {

            try {
                Sheet sheet = bookOut.getOrCreateSheet(this.sheetName);

                PoiRowsOut rowsOut = new PoiRowsOut(sheet,
                        bookOut.createStyles(new CmprStyleProivderFactory()),
                        firstRow,
                        firstColumn);

                List<ResultItemOut> template = new ArrayList<>();
                int column = firstColumn < 1 ? 1 : firstColumn;

                template.add(new TypeOut(column));

                MatchableMetaData metaData = matchResult.getMetaData();

                for (int keyIndex = 0; keyIndex < metaData.getKeyProperties().size(); ++keyIndex) {

                    template.add(new KeyOut(++column, metaData, keyIndex));
                }
                for (int comparisonIndex = 0; comparisonIndex < metaData.getKeyProperties().size(); ++comparisonIndex) {

                    template.add(new ComparisonOut(++column, metaData, comparisonIndex));
                    ++column;
                }

                this.cellWriter = new CellWriter(rowsOut, template);
                this.cellWriter.writeHeader();

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        this.cellWriter.write(matchResult);

        if (to != null) {
            to.accept(matchResult);
        }
    }

    public void stop() throws IOException {
        this.bookOut.close();
    }

    static class CellWriter {

        private final RowsOut rowsOut;

        private final List<ResultItemOut> template;

        CellWriter(RowsOut rowsOut, List<ResultItemOut> template) {
            this.rowsOut = rowsOut;
            this.template = template;
        }

        void writeHeader() {

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
    }


    interface ResultItemOut {

        void writeHeader(HeaderRowOut headerRowOut);

        void setValue(RowOut rowOut, MatchResult result);

    }


    class TypeOut implements ResultItemOut {

        private final int sheetIndex;

        TypeOut(int sheetIndex) {
            this.sheetIndex = sheetIndex;
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {
            headerRowOut.setHeader(sheetIndex, "Type");
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cell = rowOut.getCell(sheetIndex, CellType.STRING);
            cell.setCellValue(result.getResultType().toString());
        }
    }

    class KeyOut implements ResultItemOut {

        private final int sheetIndex;

        private final String name;

        private final Class<?> type;
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
            cell.setCellStyle(rowOut.styleFor(result.getResultType() == MatchResultType.X_MISSING ||
                    result.getResultType() == MatchResultType.Y_MISSING ?
                    CmprStyleProivderFactory.BEANCMPR_KEY_DIFF_STYLE :
                    CmprStyleProivderFactory.BEANCMPR_KEY_MATCH_STYLE));
        }
    }

    class ComparisonOut implements ResultItemOut {

        private final int scheetIndex;

        private final String name;

        private final Class<?> type;
        private final int comparisonIndex;

        ComparisonOut(int scheetIndex, MatchableMetaData metaData, int comparisonIndex) {
            this.scheetIndex = scheetIndex;
            this.comparisonIndex = comparisonIndex;
            this.name = metaData.getValueProperties().get(comparisonIndex);
            this.type = metaData.getPropertyType(this.name);
        }

        @Override
        public void writeHeader(HeaderRowOut headerRowOut) {
            headerRowOut.setHeader(scheetIndex, name);
        }

        @Override
        public void setValue(RowOut rowOut, MatchResult result) {
            Cell cellX = createAndSetCell(rowOut, scheetIndex, type, result.getXValue(comparisonIndex));
            Cell cellY = createAndSetCell(rowOut, scheetIndex + 1, type, result.getYValue(comparisonIndex));
            CellStyle cellStyle = null;
            if (result.getResultType() == MatchResultType.EQUAL ||
                    result.getResultType() == MatchResultType.NOT_EQUAL &&
                            result.getComparison(comparisonIndex).getResult() == 0 ) {
                cellStyle = rowOut.styleFor(CmprStyleProivderFactory.BEANCMPR_MATCH_STYLE);
            }
            else if (result.getResultType() == MatchResultType.NOT_EQUAL) {
                cellStyle = rowOut.styleFor(CmprStyleProivderFactory.BEANCMPR_DIFF_STYLE);
            }

            cellX.setCellStyle(cellStyle);
            cellY.setCellStyle(cellStyle);
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


    static Cell createAndSetCell(RowOut rowOut, int index, Class<?> type, Object value) {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookOutProvider getBookOutProvider() {
        return bookOutProvider;
    }

    public void setBookOutProvider(BookOutProvider bookOutProvider) {
        this.bookOutProvider = bookOutProvider;
    }

    public String getSheetName() {
        return sheetName;
    }

    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    public int getFirstRow() {
        return firstRow;
    }

    public void setFirstRow(int firstRow) {
        this.firstRow = firstRow;
    }

    public int getFirstColumn() {
        return firstColumn;
    }

    public void setFirstColumn(int firstColumn) {
        this.firstColumn = firstColumn;
    }

    public Consumer<? super MatchResult> getTo() {
        return to;
    }

    public void setTo(Consumer<? super MatchResult> to) {
        this.to = to;
    }

    @Override
    public String toString() {
        return Objects.requireNonNullElseGet(this.name, () -> getClass().getSimpleName());
    }
}
