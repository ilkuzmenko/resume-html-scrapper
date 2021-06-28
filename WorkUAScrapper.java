package main;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class WorkUAScrapper extends BaseScrapper {

    public WorkUAScrapper(String url) {
        super(url);
        this.fildSettings = Arrays.asList(
                new FildSetting("NAME", "h1", "div.col-sm-8, div.col-sm-12"),
                new FildSetting("AGE", "dd", "dl.dl-horizontal > dd:first-of-type"),
                new FildSetting("CITY", "dd", "dl.dl-horizontal dd:last-of-type"),
                new FildSetting("DATE", "span", "p.cut-bottom-print", "\\d{1,2}\\s+\\W{5,9}"),
                new FildSetting("PROFESSION", "h2", "div.col-sm-8, div.col-sm-12")
        );
    }


    @Override
    protected void updateResumeInfo(Document document, String link) {
        Elements userResumeCard = document.select("div.card.card-indent.wordwrap > h2, h3, p");
        List<String> resumeCard = new LinkedList<>();
        for (Element list : userResumeCard) {
            resumeCard.add(list.text().replace("'", "''"));
        }
        if (resumeCard.size() < 5)
        {
            return;
        }
        resumeCard = resumeCard.subList(5, resumeCard.size() - 2);
        updateSQL("RESUME", resumeCard.toString(), link);
    }
}