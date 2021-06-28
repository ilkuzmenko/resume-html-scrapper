package main;

public class Main {

    public static void main(String[] args) {

        WorkUAScrapper workUAScrapper = new WorkUAScrapper("https://www.work.ua");
        workUAScrapper.insertLinksPostgreSQL("/resumes/?page=1", "div.card > h2 > a, div.card-hover > h2 > a, div.resume-link > h2 > a, div.card-visited > h2 > a, div.wordwrap > h2 > a");
        workUAScrapper.updateResumePostgreSQL();

        JobsUAScrapper jobsUAScrapper = new JobsUAScrapper("https://jobs.ua");
        jobsUAScrapper.insertLinksPostgreSQL("/resume/page-1", "a.b-vacancy__top__title, a.js-item_title");
        jobsUAScrapper.updateResumePostgreSQL();

    }
}