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
import org.eclipse.collections.impl.utility.ArrayIterate;
import org.fxmisc.richtext.CodeArea;
import org.fxmisc.richtext.LineNumberFactory;

import javafx.scene.image.ImageView;
import jp.toastkid.script.highlight.SimpleHighlighter;
import jp.toastkid.slideshow.slide.CssDemoSlide;
import jp.toastkid.slideshow.slide.LineFactory;
import jp.toastkid.slideshow.slide.Slide;
import jp.toastkid.slideshow.slide.TitleSlide;

/**
 * Wiki file 2 Slides.
 *
 * @author Toast kid
 */
public class WikiToSlides {

    /** Tag of CSS demo. */
    private static final String TAG_CSS_DEMO = "{css_demo}";

    /** System line separator. */
    private static final String LINE_SEPARATOR = System.lineSeparator();

    /** Background image pattern. */
    private static final Pattern BACKGROUND = Pattern.compile("\\{background:(.+?)\\}");

    /** In-line image pattern. */
    private static final Pattern IMAGE = Pattern.compile("\\{img:(.+?)\\}");

    /** CSS specifying pattern. */
    private static final Pattern CSS = Pattern.compile("\\{css:(.+?)\\}");

    /** Source's path */
    private final Path p;

    /** Slide. */
    private Slide s = new TitleSlide();

    /** Code block processing. */
    private boolean isInCodeBlock = false;

    /** CSS title. */
    private String css;

    /**
     * Init with source's path.
     * @param p
     */
    public WikiToSlides(final Path p) {
        this.p = p;
    }

    /**
     * Convert to Slides.
     * @return List&lt;Slide&gt;
     */
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

                if (line.startsWith("*")) {
                    if (s.hasTitle()) {
                        addSlide(slides, texts);
                        s = line.startsWith("* ") ? new TitleSlide() : new Slide();
                        texts.clear();
                    }
                    //final String[] split = line.split(" ");
                    s.setTitle(line.substring(line.indexOf(" ")));
                    return;
                }
                if (line.startsWith("{background")) {
                    Optional.ofNullable(extractImageUrl(line, BACKGROUND))
                            .ifPresent(image -> s.setBgImage(image));
                    return;
                }

                if (line.startsWith("{img")) {
                    Optional.ofNullable(extractImageUrl(line, IMAGE))
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
                if (line.startsWith("{css:")) {
                    final Matcher matcher = CSS.matcher(line);
                    if (matcher.find()) {
                        css = matcher.group(1);
                        return;
                    }
                }
                // Not code.
                texts.add(line);
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
     * Extract image url from text.
     * @param line line
     * @param pattern regex pattern
     * @return image url
     */
    private String extractImageUrl(final String line, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            return null;
        }
        final String matches = matcher.group(1);
        final String[] split = matches.split("\\|");
        return ArrayIterate.select(split, piece -> piece.startsWith("src="))
                           .collect(piece -> piece.substring("src=\"".length(), piece.length() - 1))
                           .getFirst();
    }

    /**
     * Getter of css.
     * @return
     */
    public String getCss() {
        return css;
    }
}
