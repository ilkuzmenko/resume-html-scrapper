package main;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.Arrays;

public class JobsUAScrapper extends BaseScrapper {

    public JobsUAScrapper(String url) {
        super(url);
        this.fildSettings = Arrays.asList(
                new FildSetting("NAME", "span", "span.fn"),
                new FildSetting("AGE", "span", "div.b-resume-full__profile > div.b-resume-full__block-row:first-of-type > span.control"),
                new FildSetting("CITY", "a", "span.b-vacancy-full__tech__item > a.link__hidden, span.m-r-1 > a.link__hidden"),
                new FildSetting("DATE", "span", "span.b-vacancy-full__tech__item:first-of-type, span.m-r-1:first-of-type", "\\d{1,2}\\s+\\W{5,9}"),
                new FildSetting("PROFESSION", "h1", "h1.default__full-title, h1.first-letter-uppr")
        );
    }

    @Override
    public void updateResumeInfo(Document document, String link) {
        Elements userResumeCard = document.select("div.b-resume-full__block");

        String resumeCard = userResumeCard.text().replace("'", "''");
        updateSQL("RESUME", resumeCard, link);
    }
}
