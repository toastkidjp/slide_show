package jp.toastkid.slideshow;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Test resources.
 *
 * @author Toast kid
 *
 */
public class TestResources {

    /**
     * Return test source.
     * @return {@link Path}
     * @throws URISyntaxException
     */
    public static Path source() throws URISyntaxException {
        return Paths.get(TestResources.class.getClassLoader()
                .getResource("jp/toastkid/slideshow/converter/test.md").toURI());
    }
}
