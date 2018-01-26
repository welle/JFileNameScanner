package aka.media.jfilenamescanner;

import com.opencsv.bean.CsvBindByPosition;

/**
 * CSVMovies.
 *
 * @author charlottew
 */
public class CSVMovies {
    @CsvBindByPosition(position = 0)
    private String originalFileName;

    @CsvBindByPosition(position = 1)
    private String expectedMovieName;

    @CsvBindByPosition(position = 2)
    private String expectedMovieYear;

    /**
     * @return the originalFileName
     */
    public String getOriginalFileName() {
        return this.originalFileName;
    }

    /**
     * @return the expectedMovieName
     */
    public String getExpectedMovieName() {
        return this.expectedMovieName;
    }

    /**
     * @return the expectedMovieYear
     */
    public String getExpectedMovieYear() {
        return this.expectedMovieYear;
    }

}
