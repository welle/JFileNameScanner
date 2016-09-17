package aka.media.jfilenamescanner;

import static org.hamcrest.CoreMatchers.endsWith;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.commons.io.FilenameUtils;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

/**
 * JUnitTest for TV Shows.
 */
public class Series_JUnitTest {

    /**
     * Expected exception.
     */
    @Rule
    public final ExpectedException thrown = ExpectedException.none();

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
        JFileNameScanner.getTVShowName(file);

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
        final String name = JFileNameScanner.getTVShowName(file);

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
        final String name = JFileNameScanner.getTVShowName(file);
        assertEquals(name, FilenameUtils.getBaseName(file.getName()));

        file.delete();
    }
}
