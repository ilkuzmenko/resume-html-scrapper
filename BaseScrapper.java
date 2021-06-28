package main;

import db.PostgreSQL;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BaseScrapper {

    protected List<FildSetting> fildSettings;
    protected String url;

    protected void updateSQL(String colName, String fildValue, String link){
        String sql = "UPDATE USERS SET " + colName + " = '" + fildValue + "' where LINK = '" + link + "';";
        System.out.println(sql);
        PostgreSQL.update(sql);
    }

    protected void updateResumeInfo(Document document, String link){

    }

    public BaseScrapper(String url) {
        this.url = url;
    }

    private void updatePropertyByTag(FildSetting setting, Document document, String link){
        Elements domList = document.select(setting.cssQuery);
        for (Element list : domList) {
            String fildValue = list.getElementsByTag(setting.tagName).text().replace("'", "''");
            if (!setting.pattern.isEmpty() && !fildValue.isEmpty()) {
                Pattern pattern = Pattern.compile(setting.pattern);
                Matcher matcher = pattern.matcher(fildValue);
                matcher.find();
                fildValue = matcher.group();
            }
            updateSQL(setting.colName, fildValue, link);
        }
    }

    public void insertLinksPostgreSQL(String path, String cssQuery) {
        try {
            Document doc = Jsoup.connect(this.url + path).userAgent("Chrome/86.0.4240.198").get();
            Elements nameLink = doc.select(cssQuery);
            for (Element workersList : nameLink) {
                String link = workersList.attr("abs:href");
                String sql = "INSERT INTO USERS (LINK) VALUES ('" + link + "') ON CONFLICT DO NOTHING;";
                System.out.println(sql);
                PostgreSQL.insert(sql);
            }
            System.out.println("USERS table, column LINK, INSERT SQL complete.");
        }
        catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public void updateResumePostgreSQL() {
        List<String> usersLinks = PostgreSQL.select("USERS WHERE LINK LIKE '" + this.url + "%' AND (RESUME, PROFESSION) IS NULL", "LINK");
        try {
            for (String link : usersLinks){
                System.out.println();
                Document document = Jsoup.connect(link).userAgent("Chrome/86.0.4240.198").ignoreHttpErrors(true).get();
                for (FildSetting setting : fildSettings) {
                    updatePropertyByTag(setting, document, link);
                }
                updateResumeInfo(document, link);
            }
            System.out.println("USERS table, column LINK, UPDATE SQL complete.");
        }
        catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    public static class FildSetting {
        public String colName;
        public String tagName;
        public String cssQuery;
        public String pattern = "";


        public FildSetting (String colName, String tagName, String cssQuery){
            this.colName = colName;
            this.tagName = tagName;
            this.cssQuery = cssQuery;
        }

        public FildSetting (String colName, String tagName, String cssQuery, String pattern){
            this.colName = colName;
            this.tagName = tagName;
            this.cssQuery = cssQuery;
            this.pattern = pattern;
        }
    }
}
