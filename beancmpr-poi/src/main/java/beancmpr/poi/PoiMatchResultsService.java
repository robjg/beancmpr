package beancmpr.poi;

import dido.poi.BookOutProvider;
import org.oddjob.beancmpr.results.MatchResult;
import org.oddjob.dido.poi.BookOut;

import java.io.IOException;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * A service that can be used in a Bean Bus to create an Excel
 * spreadsheet of results created using the {@link org.oddjob.beancmpr.results.MatchResultHandlerFactory} as
 * the Result Handler of an {@link org.oddjob.beancmpr.BeanCompareJob}.
 */
public class PoiMatchResultsService implements Consumer<MatchResult> {

    private volatile String name;

    private volatile BookOutProvider bookOutProvider;

    private volatile String sheetName;

    private volatile int firstRow;

    private volatile int firstColumn;

    private volatile String xHeaderPrefix;

    private volatile String yHeaderPrefix;

    private volatile BookOut bookOut;

    private volatile MatchResultCellWriter cellWriter;

    private volatile Consumer<? super MatchResult> to;

    public void start() throws Exception {

        this.bookOut = bookOutProvider.provideBookOut();
    }

    @Override
    public void accept(MatchResult matchResult) {

        if (cellWriter == null) {

            try {

                this.cellWriter = MatchResultCellWriter.withOptions()
                        .setSheetName(this.sheetName)
                        .setFirstRow(this.firstRow)
                        .setFirstColumn(this.firstColumn)
                        .setxPrefix(this.xHeaderPrefix)
                        .setyPrefix(this.yHeaderPrefix)
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
            this.cellWriter.close();
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

    public String getxHeaderPrefix() {
        return xHeaderPrefix;
    }

    public void setxHeaderPrefix(String xHeaderPrefix) {
        this.xHeaderPrefix = xHeaderPrefix;
    }

    public String getyHeaderPrefix() {
        return yHeaderPrefix;
    }

    public void setyHeaderPrefix(String yHeaderPrefix) {
        this.yHeaderPrefix = yHeaderPrefix;
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
