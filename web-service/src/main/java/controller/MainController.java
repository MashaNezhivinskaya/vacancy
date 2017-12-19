package controller;

import dto.NameAndCount;
import dto.VacancySearchDto;
import entities.DictionaryEntity;
import entities.UiVacancy;
import jdbc.DataQueryManager;
import jdbc.MySqlManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import util.VacancyManager;

import java.io.UnsupportedEncodingException;
import java.util.List;


@RestController
public class MainController {

//	private Timer timer = new Timer() {
//		{
//			schedule(new TimerTask() {
//
//				@Override
//				public void run() {
//					try {
//						handleResults();
//					} catch (Exception e) {
//					}
//				}
//
//			}, 11000, 60000);
//		}
//	};

//	@Autowired
//	ParticipantRepository participantRepository;
//
//	@Autowired
//	ResultRepository resultRepository;
//
//	@Autowired
//	PassportRepository passportRepository;
//
//	@Autowired
//	ResultStashRepository resultStashRepository;

//	@RequestMapping(method = RequestMethod.GET, value = "/participants/{id}", produces = "application/json; charset=UTF-8")
//	public List<Participant> get(@PathVariable("id") Integer regionId) throws IOException, ParseException {
//		try {
//			return participantRepository.getParticipantsByRegion(regionId);
//		} catch (Exception e) {
//			return new ArrayList<Participant>();
//		}
//	}
//
//	@RequestMapping(method = RequestMethod.GET, value = "/result/{id}", produces = "application/json; charset=UTF-8")
//	public List<Result> getResultByRegionId(@PathVariable("id") Integer regionId) throws IOException, ParseException {
//		try {
//			return resultRepository.getResultsByRegion(regionId);
//		} catch (Exception e) {
//			return new ArrayList<Result>();
//		}
//	}
//
//	@RequestMapping(method = RequestMethod.GET, value = "/canVote/{passportNumber}", produces = "application/json; charset=UTF-8")
//	public ResultMsg canVote(@PathVariable("passportNumber") String passportNumber) {
//		return new ResultMsg(passportRepository.canVote(passportNumber));
//	}
//
//	@RequestMapping(method = RequestMethod.GET, value = "/addPassport/{passportNumber}", produces = "application/json; charset=UTF-8")
//	public String addPassport(@PathVariable("passportNumber") String passportNumber) throws SQLException {
//		passportRepository.set(new Passport(passportNumber));
//		return "OK";
//	}

	//TODO Например: http://localhost:8080/dictionary?name=profarea


	@RequestMapping(method = RequestMethod.GET, value = "/dictionary", produces = "application/json; charset=UTF-8")
	public List<DictionaryEntity> getDictionary(@RequestParam String name) {
		return MySqlManager.getInstance().getList(String.format("select id, name from vacancy_schema.%s", name), resultSet -> {
			DictionaryEntity dictionaryEntity = new DictionaryEntity();
			dictionaryEntity.setId(resultSet.getString("id"));
			dictionaryEntity.setName(resultSet.getString("name"));
			return dictionaryEntity;
		});
	}

	@RequestMapping(method = RequestMethod.GET, value = "/specialization/count", produces = "application/json; charset=UTF-8")
	public List<NameAndCount> getSpecializationGroups() {
		return DataQueryManager.getSpecializationGroups();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/profarea/count", produces = "application/json; charset=UTF-8")
	public List<NameAndCount> getProfareaGroups() {
		return DataQueryManager.getProfareaGroups();
	}

	@RequestMapping(method = RequestMethod.POST, value = "/vacancy/create", produces = "application/json; charset=UTF-8")
	public Object putResultToStash(@RequestBody UiVacancy vacancy) {
		VacancyManager.instertVacancyNullValid(vacancy);
		return "OK";
	}

	@RequestMapping(method = RequestMethod.POST, value = "/vacancy/search", produces = "application/json; charset=UTF-8")
	public List<VacancySearchDto> getVacanciesForTable(@RequestBody UiVacancy vacancy) {
		return DataQueryManager.getVacanciesSearch(vacancy);
	}

    @RequestMapping(method = RequestMethod.GET, value = "/vacancies/count", produces = "application/json; charset=UTF-8")
    public Integer getVacanciesCount() {
        return DataQueryManager.getVacanciesCount();
    }
	
	private String decodeStr(String results) throws UnsupportedEncodingException {
		results = java.net.URLDecoder.decode(results, "UTF-8");
		if (results.endsWith("=")) {
			results = results.substring(0, results.length()-1);
		}
		return results;
	}

}
