package jp.toastkid.slideshow.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.scene.image.ImageView;
import jp.toastkid.script.highlight.SimpleHighlighter;
import jp.toastkid.slideshow.slide.LineFactory;
import jp.toastkid.slideshow.slide.Slide;

/**
 * Markdown file to Slides.
 *
 * @author Toast kid
 */
public class MarkdownToSlides extends BaseConverter {

    /** Background image pattern. */
    private static final Pattern BACKGROUND = Pattern.compile("\\!\\[background\\]\\((.+?)\\)");

    /** In-line image pattern. */
    private static final Pattern IMAGE = Pattern.compile("\\!\\[(.+?)\\]\\((.+?)\\)");

    /** CSS specifying pattern. */
    private static final Pattern CSS = Pattern.compile("\\[css\\]\\((.+?)\\)");

    /** Source's path */
    private final Path p;

    /** Slide. */
    private Slide.Builder builder;

    /** Code block processing. */
    private boolean isInCodeBlock = false;

    /**
     * Init with source's path.
     * @param p
     */
    public MarkdownToSlides(final Path p) {
        this.p = p;
        builder = new Slide.Builder();
    }

    /**
     * Convert to Slides.
     * @return List&lt;Slide&gt;
     */
    @Override
    public MutableList<Slide> convert() {
        final MutableList<Slide> slides = Lists.mutable.empty();
        try (final Stream<String> lines = Files.lines(p)) {
            final StringBuilder code = new StringBuilder();
            lines.forEach(line -> {

                if (line.startsWith("#")) {
                    if (builder.hasTitle()) {
                        slides.add(builder.build());
                        builder = new Slide.Builder();
                    }
                    if (line.startsWith("# ")) {
                        builder.isFront(true);
                    }
                    builder.title(line.substring(line.indexOf(" ")));
                    return;
                }

                if (line.startsWith("![")) {

                    if (line.startsWith("![background](")) {
                        Optional.ofNullable(extractBackgroundUrl(line))
                            .ifPresent(builder::background);
                        return;
                    }

                    Optional.ofNullable(extractImageUrl(line))
                            .ifPresent(image -> builder.withContents(LineFactory.centering(new ImageView(image))));
                    return;
                }

                // Adding code block.
                if (line.startsWith("```")) {
                    isInCodeBlock = !isInCodeBlock;
                    if (!isInCodeBlock && code.length() != 0) {
                        final CodeArea codeArea = new CodeArea();
                        new SimpleHighlighter(codeArea, "keywords/groovy.txt", "keywords/java.txt")
                            .highlight();
                        codeArea.setEditable(false);
                        codeArea.setStyle("-fx-font-size: 40pt;");
                        codeArea.replaceText(code.toString());
                        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
                        final int height = code.toString().split(LINE_SEPARATOR).length * 80;
                        codeArea.setMinHeight(height);
                        builder.withContents(codeArea);
                        code.setLength(0);
                    }
                    return;
                }

                if (isInCodeBlock && !line.startsWith("```")) {
                    code.append(code.length() != 0 ? LINE_SEPARATOR : "").append(line);
                    return;
                }

                if (line.startsWith("[css](")) {
                    final Matcher matcher = CSS.matcher(line);
                    if (matcher.find()) {
                        setCss(matcher.group(1));
                        return;
                    }
                }

                // Not code.
                if (!line.isEmpty()) {
                    builder.addLines(line);
                }
            });
            slides.add(builder.build());
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return slides;
    }

    /**
     * Extract background image url from text.
     * @param line line
     * @return image url
     */
    private String extractBackgroundUrl(final String line) {
        final Matcher matcher = BACKGROUND.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(1);
    }

    /**
     * Extract image url from text.
     * @param line line
     * @return image url
     */
    private String extractImageUrl(final String line) {
        final Matcher matcher = IMAGE.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        return matcher.group(2);
    }

}
