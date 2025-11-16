package beancmpr.poi;

import dido.poi.BookOutProvider;
import org.oddjob.beancmpr.results.MatchResult;
import dido.poi.BookOut;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @oddjob.description A service that can be used in a Bean Bus to create an Excel
 * spreadsheet of comparison results. The result will typically be created
 * using the {@link org.oddjob.beancmpr.results.MatchResultHandlerFactory} as
 * the Result Handler of an {@link org.oddjob.beancmpr.BeanCompareJob}.
 */
public class PoiMatchResultsService implements Consumer<MatchResult> {

    /**
     * @oddjob.property
     * @oddjob.description The name of the component as seen in Oddjob.
     * @oddjob.required No.
     */
    private volatile String name;

    /**
     * @oddjob.property
     * @oddjob.description The provider of the Excel workbook to write to.
     * Typically, this will be a <a href="https://github.com/robjg/dido/blob/master/docs/reference/dido/poi/data/PoiWorkbook.md">dido-poi:workbook</a>
     * @oddjob.required Yes.
     */
    private volatile BookOutProvider workbook;

    /**
     * @oddjob.property
     * @oddjob.description The name of the sheet that will be created.
     * @oddjob.required No.
     */
    private volatile String sheetName;

    /**
     * @oddjob.property
     * @oddjob.description The position of the first row. 1 being the top
     * @oddjob.required No. Defaults to 1.
     */
    private volatile int firstRow;

    /**
     * @oddjob.property
     * @oddjob.description The position of the first column. 1 being the leftmost.
     * @oddjob.required No. Defaults to 1.
     */
    private volatile int firstColumn;

    /**
     * @oddjob.property
     * @oddjob.description The prefix that will be used for the X titles.
     * @oddjob.required No. Defaults to X_.
     */
    private volatile String xPrefix;

    /**
     * @oddjob.property
     * @oddjob.description The prefix that will be used for the Y titles.
     * @oddjob.required No. Defaults to Y_.
     */
    private volatile String yPrefix;

    /**
     * @oddjob.property
     * @oddjob.description Should the column width expand to fit the data.
     * @oddjob.required No. Defaults to false.
     */
    private boolean autoWidth;

    /**
     * @oddjob.property
     * @oddjob.description Should filters be applied to the columns.
     * @oddjob.required No. Defaults to false.
     */
    private boolean autoFilter;

    /** The book being created */
    private volatile BookOut bookOut;

    private volatile MatchResultCellWriter cellWriter;

    /**
     * @oddjob.property
     * @oddjob.description An additional consumer to send results to. If the service
     * is the middle of Bean Bus pipeline then this will be automatically set to
     * the next component. Otherwise, might be useful for testing but can otherwise be
     * ignored.
     * @oddjob.required No.
     */
    private volatile Consumer<? super MatchResult> to;

    public void start() throws Exception {

        this.bookOut = workbook.provideBookOut();
    }

    @Override
    public void accept(MatchResult matchResult) {

        if (cellWriter == null) {

            try {

                this.cellWriter = MatchResultCellWriter.withOptions()
                        .setSheetName(this.sheetName)
                        .setFirstRow(this.firstRow)
                        .setFirstColumn(this.firstColumn)
                        .setxPrefix(this.xPrefix)
                        .setyPrefix(this.yPrefix)
                        .setAutoFilter(this.autoFilter)
                        .setAutoWidth(this.autoWidth)
                        .createFor(this.bookOut, matchResult.getMetaData());

                this.cellWriter.writeHeader();

            } catch (Exception e) {
                throw new RuntimeException("Failed creating Cell Writer for first Match Result " +
                        matchResult, e);
            }
        }

        this.cellWriter.write(matchResult);

        if (to != null) {
            to.accept(matchResult);
        }
    }

    public void stop() throws IOException {
        try {
            // If no lines written, cell writer will be null still.
            if (this.cellWriter != null) {
                this.cellWriter.close();
            }
            this.bookOut.close();
        }
        finally {
            this.cellWriter = null;
            this.bookOut = null;
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BookOutProvider getWorkbook() {
        return workbook;
    }

    public void setWorkbook(BookOutProvider workbook) {
        this.workbook = workbook;
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

    public String getxPrefix() {
        return xPrefix;
    }

    public void setxPrefix(String xPrefix) {
        this.xPrefix = xPrefix;
    }

    public String getyPrefix() {
        return yPrefix;
    }

    public void setyPrefix(String yPrefix) {
        this.yPrefix = yPrefix;
    }

    public boolean isAutoWidth() {
        return autoWidth;
    }

    public void setAutoWidth(boolean autoWidth) {
        this.autoWidth = autoWidth;
    }

    public boolean isAutoFilter() {
        return autoFilter;
    }

    public void setAutoFilter(boolean autoFilter) {
        this.autoFilter = autoFilter;
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
