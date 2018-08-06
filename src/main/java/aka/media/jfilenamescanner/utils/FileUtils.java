package aka.media.jfilenamescanner.utils;

import java.io.File;

import org.eclipse.jdt.annotation.NonNull;

/**
 * Utils related to File.
 *
 * @author Cha
 */
public final class FileUtils {

    private FileUtils() {
        // private constructor, to ensure the class can not be instantiated
    }

    /**
     * Check if file or directory exits.
     *
     * @param path
     * @return <code>true</code> if path exist
     */
    public static boolean fileOrDirectoryExists(@NonNull final String path) {
        var result = false;

        final var file = new File(path);
        result = file.exists();

        return result;
    }

    /**
     * Check if given param dir is a root directory.
     *
     * @param dir Directory
     * @return <code>true</code> if it is a directory
     */
    public static boolean isRootDir(@NonNull final File dir) {
        if (!dir.isDirectory()) {
            return false;
        }

        final var roots = File.listRoots();
        for (final File root : roots) {
            if (root.equals(dir)) {
                return true;
            }
        }
        return false;
    }

}
