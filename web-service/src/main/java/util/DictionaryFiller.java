package util;

import com.google.gson.Gson;
import entities.Currency;
import entities.DictionaryEntity;
import entities.Specialization;
import jdbc.MySqlManager;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Maria on 24.11.17.
 */
public class DictionaryFiller {
    public static void fillAllDictionaries() throws IOException {

        String dictionaries = Jsoup.connect("https://api.hh.ru/dictionaries").ignoreContentType(true).execute().body();

        List<Currency> currencies = DictionaryResolver.getObjects(dictionaries, "currency", Currency.class);
        MySqlManager.getInstance().executeBatchStatement("INSERT INTO `vacancy_schema`.`currency` (`name`, `id`, `rate`) VALUES (?, ?, ?)",
                currencies, (preparedStatement, object) -> {
                    preparedStatement.setString(1, object.getName());
                    preparedStatement.setString(2, object.getCode());
                    preparedStatement.setDouble(3, object.getRate());
                });

        DictionaryFiller.fillSimpleDictionary(MySqlManager.getInstance(), dictionaries, "schedule");
        DictionaryFiller.fillSimpleDictionary(MySqlManager.getInstance(), dictionaries, "experience");
        DictionaryFiller.fillSimpleDictionary(MySqlManager.getInstance(), dictionaries, "employment");

        String specializationsStr = Jsoup.connect("https://api.hh.ru/specializations").ignoreContentType(true).execute().body();
        Specialization[] specializationObjects = new Gson().fromJson(specializationsStr, Specialization[].class);

        Arrays.stream(specializationObjects).forEach(specialization -> {
            MySqlManager.getInstance().executePreparedStatement("INSERT INTO `vacancy_schema`.`profarea` (`id`, `name`) VALUES (? , ?)", preparedStatement -> {
                        preparedStatement.setInt(1, specialization.getId());
                        preparedStatement.setString(2, specialization.getName());
                    }
            );
            MySqlManager.getInstance().executeBatchStatement("INSERT INTO `vacancy_schema`.`specialization` (`id`, `name`, `profarea_id`) VALUES (?, ?, " + specialization.getId() + ")",
                    specialization.getSpecializations(), (preparedStatement, object) -> {
                        preparedStatement.setString(1, object.getId());
                        preparedStatement.setString(2, object.getName());
                    });

        });
    }

    private static void fillSimpleDictionary(MySqlManager manager, String dictionaries, String code) {
        List<DictionaryEntity> employerTypes = DictionaryResolver.getObjects(dictionaries, code, DictionaryEntity.class);
        manager.executeBatchStatement("INSERT INTO `vacancy_schema`.`" + code + "` (`id`, `name`) VALUES (?, ?)",
                employerTypes, (preparedStatement, object) -> {
                    preparedStatement.setString(1, object.getId());
                    preparedStatement.setString(2, object.getName());
                });
    }
}
