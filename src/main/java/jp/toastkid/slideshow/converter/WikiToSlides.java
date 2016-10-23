package jp.toastkid.slideshow.converter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

import jp.toastkid.slideshow.slide.LineFactory;
import jp.toastkid.slideshow.slide.Slide;
import jp.toastkid.slideshow.slide.TitleSlide;

/**
 * Wiki file 2 Slides.
 *
 * @author Toast kid
 */
public class WikiToSlides {

    /** source's path */
    private final Path p;

    /**
     * Init with source's path.
     * @param p
     */
    public WikiToSlides(final Path p) {
        this.p = p;
    }

    private Slide s = new Slide();

    /**
     * Convert to Slides.
     * @return List&lt;Slide&gt;
     */
    public List<Slide> convert() {
        final List<Slide> slides = Lists.mutable.empty();
        try (final Stream<String> lines = Files.lines(p)) {
            final MutableList<String> texts = Lists.mutable.empty();
            lines.forEach(line -> {
                if (line.startsWith("*")) {
                    if (s.hasTitle()) {
                        if (s instanceof TitleSlide && !texts.isEmpty()) {
                            s.addContents(LineFactory.centeredText(texts.get(0)));
                        } else {
                            texts.collect(LineFactory::normal).each(s::addContents);
                        }
                        slides.add(s);
                        s = line.startsWith("* ") ? new TitleSlide() : new Slide();
                        texts.clear();
                    }
                    final String[] split = line.split(" ");
                    s.setTitle(line.substring(line.indexOf(" ")));
                    return;
                }
                texts.add(line);
            });
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return slides;
    }
}
