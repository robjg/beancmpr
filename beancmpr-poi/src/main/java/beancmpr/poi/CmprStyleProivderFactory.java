package beancmpr.poi;

import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Workbook;
import org.oddjob.dido.poi.style.StyleBean;
import org.oddjob.dido.poi.style.StyleFactoryRegistry;
import org.oddjob.dido.poi.style.StyleProvider;
import org.oddjob.dido.poi.style.StyleProviderFactory;

/**
 * Provide the styles for the comparison spreadsheet.
 *
 * @author rob
 */
public class CmprStyleProivderFactory implements StyleProviderFactory {

    /**
     * The name of the style used for headings.
     */
    public static String HEADING_STYLE = "heading";

    /**
     * The name of the style used for a beancmpr value missing.
     */
    public static String BEANCMPR_TYPE_STYLE = "beancmpr-type";

    /**
     * The name of the style used for a beancmpr key column for a row
     */
    public static String BEANCMPR_KEY_MATCH_STYLE = "beancmpr-key-match";

    /**
     * The name of the style used for a beancmpr key column for a row
     */
    public static String BEANCMPR_KEY_DIFF_STYLE = "beancmpr-key-difference";

    /**
     * The name of the style used for a beancmpr key column for a row
     */
    public static String BEANCMPR_KEY_MISSING_STYLE = "beancmpr-key-missing";

    /**
     * The name of the style used for a beancmpr value difference.
     */
    public static String BEANCMPR_DIFF_STYLE = "beancmpr-difference";

    /**
     * The name of the style used for a beancmpr value match.
     */
    public static String BEANCMPR_MATCH_STYLE = "beancmpr-match";

    /**
     * The name of the style used for a beancmpr value where other missing.
     */
    public static String BEANCMPR_MISSING_STYLE = "beancmpr-missing";

    /**
     * The name of the style used for a beancmpr value missing.
     */
    public static String BEANCMPR_BLANK_STYLE = "beancmpr-blank";

    private final StyleFactoryRegistry factory = new StyleFactoryRegistry();

    /**
     * Create a new instance.
     */
    public CmprStyleProivderFactory() {

        {
            StyleBean style = new StyleBean();
            style.setBold(true);
            factory.registerStyle(HEADING_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setBold(true);
            factory.registerStyle(BEANCMPR_TYPE_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.YELLOW);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_KEY_MATCH_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.PINK);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_KEY_DIFF_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.TURQUOISE);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_KEY_MISSING_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.RED);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_DIFF_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.BRIGHT_GREEN);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_MATCH_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.LIGHT_BLUE);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_MISSING_STYLE, style);
        }
        {
            StyleBean style = new StyleBean();
            style.setFillForegroundColour(IndexedColors.GREY_25_PERCENT);
            style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            factory.registerStyle(BEANCMPR_BLANK_STYLE, style);
        }
    }

    @Override
    public StyleProvider providerFor(Workbook workbook) {
        return factory.providerFor(workbook);
    }
}
