package aka.media.jfilenamescanner;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.text.IsEqualIgnoringCase.equalToIgnoringCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

/**
 * JUnitTest for Movies.
 */
public class Movies_JUnitTest {

    /**
     * Expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

    @NonNull
    private final List<@NonNull Movie> moviesList = new ArrayList<>();

    private static final boolean DEBUG = false;

    /**
     * Initialization.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    @Before
    public void init() throws IOException, URISyntaxException {
        final File file = new File(ClassLoader.getSystemClassLoader().getResource("movietitle.csv").toURI());
        final FileReader fr = new FileReader(file);
        final BufferedReader in = new BufferedReader(fr);

        final CsvToBean<CSVMovies> csvToBean = new CsvToBeanBuilder<CSVMovies>(fr)
                .withSeparator(';')
                .withType(CSVMovies.class)
                .withIgnoreLeadingWhiteSpace(true)
                .build();

        final List<CSVMovies> csvUsers = csvToBean.parse();

        for (final CSVMovies csvUser : csvUsers) {
            final Movie movie = new Movie();
            movie.originalFileName = csvUser.getOriginalFileName();
            movie.expectedMovieName = csvUser.getExpectedMovieName();
            if (csvUser.getExpectedMovieYear() != null && csvUser.getExpectedMovieYear().length() > 0) {
                movie.expectedMovieYear = csvUser.getExpectedMovieYear();
            }
            this.moviesList.add(movie);
        }

        in.close();
    }

    /**
     * File is a directory.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void TestFileIsADirectory() throws Exception {
        this.thrown.expect(Exception.class);
        this.thrown.expectMessage(endsWith("is not a file, maybe a directory ?"));

        final Path tempDir = Files.createTempDirectory("tempfiles");

        final File file = tempDir.toFile();
        assert file != null;
        JFileNameScanner.getMovieName(file);

        file.deleteOnExit();
    }

    /**
     * No matching found, file name returned.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void TestNoMatchingFound() throws Exception {

        final Path tempFile = Files.createTempFile("", "");

        final File file = tempFile.toFile();
        assert file != null;
        final String name = JFileNameScanner.getMovieName(file);

        assertEquals(name, file.getName());

        file.delete();
    }

    /**
     * No matching found, file name returned.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void TestNoMatchingFound_2() throws Exception {

        final Path tempFile = Files.createTempFile("", ".tmp");

        final File file = tempFile.toFile();
        assert file != null;
        final String name = JFileNameScanner.getMovieName(file);
        assertEquals(name, FilenameUtils.getBaseName(file.getName()));

        file.delete();
    }

    /**
     * Check files.
     *
     * @throws Exception
     */
    @org.junit.Test
    public void TestFileNames() throws Exception {
        for (final Movie movie : this.moviesList) {
            @Nullable
            final String originalFileName = movie.originalFileName;
            if (originalFileName != null) {
                final String name = JFileNameScanner.getMovieName(originalFileName);
                final String year = JFileNameScanner.getMovieYear(originalFileName);
                if (DEBUG) {
                    System.err.println("File name = \"" + originalFileName + "\"");
                    System.err.println("    Expected name = \"" + movie.expectedMovieName + "\" :: Scanned name = \"" + name + "\"");
                    System.err.println("    Expected year = \"" + movie.expectedMovieYear + "\" :: Scanned year = \"" + year + "\"");
                }
                assertThat(name, is(equalToIgnoringCase(movie.expectedMovieName)));
                assertEquals(year, movie.expectedMovieYear);
            }
        }
    }

    private class Movie {

        @Nullable
        public String originalFileName;
        @Nullable
        public String expectedMovieName;
        @Nullable
        public String expectedMovieYear;

    }
}
