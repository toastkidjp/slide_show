package jp.toastkid.slideshow.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
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
import jp.toastkid.slideshow.slide.CssDemoSlide;
import jp.toastkid.slideshow.slide.LineFactory;
import jp.toastkid.slideshow.slide.Slide;
import jp.toastkid.slideshow.slide.TitleSlide;

/**
 * Markdown file to Slides.
 *
 * @author Toast kid
 */
public class MarkdownToSlides extends BaseConverter {

    /** Tag of CSS demo. */
    private static final String TAG_CSS_DEMO = "{css_demo}";

    /** Background image pattern. */
    private static final Pattern BACKGROUND = Pattern.compile("\\!\\[background\\]\\((.+?)\\)");

    /** In-line image pattern. */
    private static final Pattern IMAGE = Pattern.compile("\\!\\[(.+?)\\]\\((.+?)\\)");

    /** CSS specifying pattern. */
    private static final Pattern CSS = Pattern.compile("\\[css\\]\\((.+?)\\)");

    /** Source's path */
    private final Path p;

    /** Slide. */
    private Slide s = new TitleSlide();

    /** Code block processing. */
    private boolean isInCodeBlock = false;

    /**
     * Init with source's path.
     * @param p
     */
    public MarkdownToSlides(final Path p) {
        this.p = p;
    }

    /**
     * Convert to Slides.
     * @return List&lt;Slide&gt;
     */
    @Override
    public MutableList<Slide> convert() {
        final MutableList<Slide> slides = Lists.mutable.empty();
        try (final Stream<String> lines = Files.lines(p)) {
            final MutableList<String> texts = Lists.mutable.empty();
            final StringBuilder code = new StringBuilder();
            lines.forEach(line -> {

                if (TAG_CSS_DEMO.equals(line)) {
                    slides.add(new CssDemoSlide());
                    return;
                }

                if (line.startsWith("#")) {
                    if (s.hasTitle()) {
                        addSlide(slides, texts);
                        s = line.startsWith("# ") ? new TitleSlide() : new Slide();
                        texts.clear();
                    }
                    //final String[] split = line.split(" ");
                    s.setTitle(line.substring(line.indexOf(" ")));
                    return;
                }

                if (line.startsWith("{background")) {
                    return;
                }

                if (line.startsWith("![")) {

                    if (line.startsWith("![background](")) {
                        Optional.ofNullable(extractBackgroundUrl(line))
                            .ifPresent(image -> s.setBgImage(image));
                        return;
                    }

                    Optional.ofNullable(extractImageUrl(line))
                            .ifPresent(image -> s.addContents(LineFactory.centering(new ImageView(image))));
                    return;
                }

                // Adding code block.
                if (line.startsWith("```")) {
                    isInCodeBlock = !isInCodeBlock;
                    if (!isInCodeBlock && code.length() != 0) {
                        final CodeArea codeArea = new CodeArea();
                        new SimpleHighlighter(codeArea, "fx.txt", "keywords/groovy.txt")
                            .highlight();
                        codeArea.setEditable(false);
                        codeArea.setStyle("-fx-font-size: 40pt;");
                        codeArea.replaceText(code.toString());
                        codeArea.setParagraphGraphicFactory(LineNumberFactory.get(codeArea));
                        final int height = code.toString().split(LINE_SEPARATOR).length * 80;
                        codeArea.setMinHeight(height);
                        s.addContents(codeArea);
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
                    texts.add(line);
                }
            });
            addSlide(slides, texts);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return slides;
    }

    /**
     * Add slide to passed List.
     * @param slides list
     * @param texts text list.
     */
    private void addSlide(final List<Slide> slides, final MutableList<String> texts) {
        if (s instanceof TitleSlide && !texts.isEmpty()) {
            s.addContents(LineFactory.centeredText(texts.get(0)));
        } else {
            texts.collect(LineFactory::normal).each(s::addContents);
        }
        slides.add(s);
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
