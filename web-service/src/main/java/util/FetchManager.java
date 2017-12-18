package util;

import com.google.gson.Gson;
import entities.Item;
import entities.Vacancy;
import entities.VacancyPage;
import jdbc.MySqlManager;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Maria on 13.12.17.
 */
public class FetchManager {

    public static void fetchData() throws IOException {
        DictionaryFiller.fillAllDictionaries();
        List<Integer> ids = MySqlManager.getInstance().getList("select id from `vacancy_schema`.`profarea`", resultSet -> resultSet.getInt("id"));
        try {
            System.out.println("Время начала: " + LocalDateTime.now());
            for (int i = 0; i < ids.size(); i++) {
                int currentId = ids.get(i);
                String vacancies = Jsoup.connect("https://api.hh.ru/vacancies?per_page=100&specialization=" + currentId).ignoreContentType(true).execute().body();
                String pagesSubstring = vacancies.substring(vacancies.indexOf("\"pages\":"));
                Integer pagesCount = Integer.valueOf(pagesSubstring.substring(8, pagesSubstring.indexOf(",")));
                for (int j = 0; j < pagesCount; j++) {
                    String pageVacancies = Jsoup.connect("https://api.hh.ru/vacancies?per_page=100&page=" + j + "&specialization=" + currentId).ignoreContentType(true).execute().body();
                    List<String> urls = new Gson().fromJson(pageVacancies, VacancyPage.class).getItems().stream().map(Item::getUrl).collect(Collectors.toList());
                    for (int k = 0; k < urls.size(); k++) {
                        String vacancyStr = Jsoup.connect(urls.get(k)).ignoreContentType(true).execute().body();
                        try {
                            Vacancy vacancy = new Gson().fromJson("[" + vacancyStr + "]", Vacancy[].class)[0];
                            VacancyManager.insertVacancy(vacancy);
                        } catch (Exception e) {
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        System.out.println("Время окончания: " + LocalDateTime.now());
    }
}
