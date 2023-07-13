package jp.dcworks.app.paiza_learn_track_web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.dcworks.app.paiza_learn_track_web.dto.ProgressRatesDto;
import jp.dcworks.app.paiza_learn_track_web.dto.UserProgressRatesDto;
import jp.dcworks.app.paiza_learn_track_web.entity.ProgressRates;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TasksMap;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TeamUserTaskProgressMap;
import jp.dcworks.app.paiza_learn_track_web.service.ProgressRatesService;
import jp.dcworks.app.paiza_learn_track_web.service.TasksService;
import jp.dcworks.app.paiza_learn_track_web.service.TeamUserTaskProgressService;
import lombok.extern.log4j.Log4j2;

/**
 * ホームコントローラー。
 *
 * @author tomo-sato
 */
@Log4j2
@Controller
@RequestMapping("/")
public class HomeController {

	@Autowired
	TasksService tasksService;
	@Autowired
	ProgressRatesService progressRatesService;
	@Autowired
	TeamUserTaskProgressService teamUserTaskProgressService;

	/**
	 * [GET]講座別一覧画面のアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 * @throws ParseException
	 */
	@GetMapping(path = {"", "/"})
	public String index(Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = sdf.parse("2023-07-05");

		Double sumLearningHours = tasksService.findGroupBySumLearningHours();
		List<ProgressRatesMap> progressRatesList = progressRatesService.getSumTotalLearningHours(reportDate, sumLearningHours);
		Map<Long, TeamUserTaskProgressMap> lastAccessLessonMap = teamUserTaskProgressService.getLastAccessLessonMap();

		List<ProgressRatesDto> progressRatesDtoList = convertProgressRatesDto(progressRatesList, lastAccessLessonMap);

		model.addAttribute("progressRatesDtoList", progressRatesDtoList);
		return "index";
	}

	/**
	 * [GET]講座別進捗一覧画面のアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 * @throws ParseException
	 */
	@GetMapping("/detail/{teamUsersId}")
	public String detail(@PathVariable Long teamUsersId, Model model) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = sdf.parse("2023-07-05");

		List<TasksMap> tasksMapList = tasksService.findGroupByLesson();
		Map<String, ProgressRates> progressRatesMap = progressRatesService.findByTeamUsersIdAndReportDateOrderMap(teamUsersId, reportDate);

		List<UserProgressRatesDto> userProgressRatesDtoList = convertUserProgressRatesDto(tasksMapList, progressRatesMap);

		model.addAttribute("userProgressRatesDtoList", userProgressRatesDtoList);
		return "detail";
	}

	private List<UserProgressRatesDto> convertUserProgressRatesDto(List<TasksMap> tasksMapList,
			Map<String, ProgressRates> progressRatesMap) {

		List<UserProgressRatesDto> retList = new ArrayList<UserProgressRatesDto>();

		for (TasksMap item : tasksMapList) {
			String lessonId = item.getLessonId();

			UserProgressRatesDto userProgressRatesDto = new UserProgressRatesDto();
			userProgressRatesDto.setMaxTasksId(item.getMaxTasksId());
			userProgressRatesDto.setCourseId(item.getCourseId());
			userProgressRatesDto.setCourseName(item.getCourseName());
			userProgressRatesDto.setLessonName(item.getLessonName());
			userProgressRatesDto.setSumTotalLearningHours(item.getSumLearningMinutes());

			if (progressRatesMap.containsKey(lessonId)) {
				ProgressRates progressRates = progressRatesMap.get(lessonId);

				userProgressRatesDto.setSumAchievedLearningHours(progressRates.getAchievedLearningHours());
				userProgressRatesDto.setLearningRate(progressRates.getTaskProgressRate());
			}

			retList.add(userProgressRatesDto);
		}

		return retList;
	}

	private List<ProgressRatesDto> convertProgressRatesDto(List<ProgressRatesMap> progressRatesList,
			Map<Long, TeamUserTaskProgressMap> lastAccessLessonMap) {

		List<ProgressRatesDto> retList = new ArrayList<ProgressRatesDto>();

		for (ProgressRatesMap item : progressRatesList) {
			Long teamUsersId = item.getTeamUsersId();

			ProgressRatesDto progressRatesDto = new ProgressRatesDto();
			progressRatesDto.setTeamUsersId(teamUsersId);
			progressRatesDto.setName(item.getName());
			progressRatesDto.setSumTotalLearningHours(item.getSumTotalLearningHours());

			if (lastAccessLessonMap.containsKey(teamUsersId)) {
				TeamUserTaskProgressMap teamUserTaskProgressMap = lastAccessLessonMap.get(teamUsersId);
				progressRatesDto.setCourseName(teamUserTaskProgressMap.getCourseName());
				progressRatesDto.setLessonName(teamUserTaskProgressMap.getLessonName());
				progressRatesDto.setChapterName(teamUserTaskProgressMap.getChapterName());
				progressRatesDto.setChapterLastAccessDatetime(teamUserTaskProgressMap.getChapterLastAccessDatetime());
			}

			retList.add(progressRatesDto);
		}

		return retList;
	}
}
