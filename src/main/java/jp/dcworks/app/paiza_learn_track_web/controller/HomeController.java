package jp.dcworks.app.paiza_learn_track_web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.dcworks.app.paiza_learn_track_web.dto.ProgressRatesDto;
import jp.dcworks.app.paiza_learn_track_web.dto.RequestTaskProgressRate;
import jp.dcworks.app.paiza_learn_track_web.dto.UserProgressRatesDto;
import jp.dcworks.app.paiza_learn_track_web.entity.OriginalTaskProgress;
import jp.dcworks.app.paiza_learn_track_web.entity.ProgressRates;
import jp.dcworks.app.paiza_learn_track_web.entity.Tasks;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TasksMap;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TeamUserTaskProgressMap;
import jp.dcworks.app.paiza_learn_track_web.service.OriginalTaskProgressService;
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

	/** 課題サービス */
	@Autowired
	TasksService tasksService;
	/** 課題進捗率サービス */
	@Autowired
	ProgressRatesService progressRatesService;
	/** チームユーザー課題進捗サービス */
	@Autowired
	TeamUserTaskProgressService teamUserTaskProgressService;
	/** オリジナル課題進捗管理サービス */
	@Autowired
	OriginalTaskProgressService originalTaskProgressService;

	/**
	 * [GET]講座別一覧画面のアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 * @throws ParseException
	 */
	@GetMapping(path = {"", "/"})
	public String index(@RequestParam(name = "reportDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate, Model model) throws ParseException {

		Double sumLearningHours = tasksService.findGroupBySumLearningHours();
		List<ProgressRatesMap> progressRatesList = progressRatesService.getSumTotalLearningHours(reportDate, sumLearningHours);
		Map<Long, TeamUserTaskProgressMap> lastAccessLessonMap = teamUserTaskProgressService.getLastAccessLessonMap(reportDate);

		List<ProgressRatesDto> progressRatesDtoList = convertProgressRatesDto(progressRatesList, lastAccessLessonMap);

		model.addAttribute("reportDate", reportDate);
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
	public String detail(@RequestParam(name = "reportDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date reportDate, @PathVariable Long teamUsersId, Model model) throws ParseException {

		// tasks テーブルより レッスンでグルーピングした結果を表出するリストのベースとする。（ここで取得した結果が全課題。）
		List<TasksMap> tasksMapList = tasksService.findGroupByLesson();
		Map<String, ProgressRates> progressRatesMap = progressRatesService.findByTeamUsersIdAndReportDateOrderMap(teamUsersId, reportDate);

		List<UserProgressRatesDto> userProgressRatesDtoList = convertUserProgressRatesDto(tasksMapList, progressRatesMap);

		model.addAttribute("reportDate", reportDate);
		model.addAttribute("userProgressRatesDtoList", userProgressRatesDtoList);
		model.addAttribute("teamUsersId", teamUsersId);

		if (!model.containsAttribute("requestTaskProgressRate")) {
			model.addAttribute("requestTaskProgressRate", new RequestTaskProgressRate());
		}
		return "detail";
	}

	@PostMapping("/registRate/{teamUsersId}/{maxTasksId}")
	public String registRate(@Validated @ModelAttribute RequestTaskProgressRate requestTaskProgressRate,
			@PathVariable Long teamUsersId,
			@PathVariable Long maxTasksId,
			BindingResult result,
			RedirectAttributes redirectAttributes) throws ParseException {

		log.info("進捗率登録処理のアクションが呼ばれました。：requestTaskProgressRate={}", requestTaskProgressRate);

		// 日付がないとそもそも機能しないので、チェック無しで取得して、だめなら落とす。
		String strReportDate = requestTaskProgressRate.getReportDate();

		// バリデーション。
		if (result.hasErrors()) {
			log.warn("バリデーションエラーが発生しました。：requestTaskProgressRate={}, result={}", requestTaskProgressRate, result);

			redirectAttributes.addFlashAttribute("validationErrors", result);
			redirectAttributes.addFlashAttribute("requestTaskProgressRate", requestTaskProgressRate);

			// 入力画面へリダイレクト。
			return "redirect:/detail/" + teamUsersId + "?reportDate=" + strReportDate;
		}

		// 課題IDから、課題を抽出しチャプターIDを取得する。
		Tasks tasks = tasksService.findById(maxTasksId);
		log.info("課題データ取得。：tasks={}", tasks);

		if (tasks == null) {
			log.warn("課題データが取得できませんでした。：maxTasksId={}", maxTasksId);

			// 入力画面へリダイレクト。
			return "redirect:/detail/" + teamUsersId;
		}
		Integer chapterId = tasks.getChapterId();
		String strTaskProgressRate = requestTaskProgressRate.getTaskProgressRate();
		Double taskProgressRate = NumberUtils.toDouble(strTaskProgressRate);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date reportDate = sdf.parse(strReportDate);

		// オリジナル課題進捗管理テーブルにデータを登録する。
		OriginalTaskProgress originalTaskProgress = originalTaskProgressService.save(teamUsersId, chapterId, taskProgressRate, reportDate);
		log.info("オリジナル課題進捗管理を登録しました。：originalTaskProgress={}", originalTaskProgress);

		// 課題進捗率テーブルにデータを登録する。
		progressRatesService.save(tasks, originalTaskProgress, reportDate);

		return "redirect:/detail/" + teamUsersId + "?reportDate=" + strReportDate;
	}

	/**
	 * DTOコンバータ
	 *
	 * @param tasksMapList
	 * @param progressRatesMap
	 * @return
	 */
	private List<UserProgressRatesDto> convertUserProgressRatesDto(List<TasksMap> tasksMapList,
			Map<String, ProgressRates> progressRatesMap) {

		List<UserProgressRatesDto> retList = new ArrayList<UserProgressRatesDto>();

		for (TasksMap item : tasksMapList) {
			String lessonId = item.getLessonId();

			UserProgressRatesDto userProgressRatesDto = new UserProgressRatesDto();
			userProgressRatesDto.setMaxTasksId(item.getMaxTasksId());
			userProgressRatesDto.setTaskTypesId(item.getTaskTypesId());
			userProgressRatesDto.setCourseId(item.getCourseId());
			userProgressRatesDto.setCourseName(item.getCourseName());
			userProgressRatesDto.setLessonName(item.getLessonName());
			userProgressRatesDto.setSumTotalLearningHours(item.getSumLearningMinutes());

			if (progressRatesMap.containsKey(lessonId)) {
				ProgressRates progressRates = progressRatesMap.get(lessonId);

				userProgressRatesDto.setSumAchievedLearningHours(progressRates.getAchievedLearningHours());
				userProgressRatesDto.setTaskProgressRate(progressRates.getTaskProgressRate());
				userProgressRatesDto.setChapterLastAccessDatetime(progressRates.getChapterLastAccessDatetime());
			}

			retList.add(userProgressRatesDto);
		}

		return retList;
	}

	/**
	 * DTOコンバータ
	 *
	 * @param progressRatesList
	 * @param lastAccessLessonMap
	 * @return
	 */
	private List<ProgressRatesDto> convertProgressRatesDto(List<ProgressRatesMap> progressRatesList,
			Map<Long, TeamUserTaskProgressMap> lastAccessLessonMap) {

		List<ProgressRatesDto> retList = new ArrayList<ProgressRatesDto>();

		for (ProgressRatesMap item : progressRatesList) {
			Long teamUsersId = item.getTeamUsersId();

			Double sumTotalLearningHours = item.getSumTotalLearningHours();
			Integer elapsedDays = item.getElapsedDays();
			Integer predictedEndDuration = (int) (elapsedDays / (sumTotalLearningHours / 100)) - elapsedDays;

			ProgressRatesDto progressRatesDto = new ProgressRatesDto();
			progressRatesDto.setTeamUsersId(teamUsersId);
			progressRatesDto.setName(item.getName());
			progressRatesDto.setSumTotalLearningHours(sumTotalLearningHours);
			progressRatesDto.setLearningStartDate(item.getLearningStartDate());
			progressRatesDto.setElapsedDays(elapsedDays);

			// 学習狩猟予測日数
			progressRatesDto.setPredictedEndDuration(predictedEndDuration);

			// 学習終了予測日
			Date currentDate = new Date();
			long millisecondsToAdd = predictedEndDuration * 24 * 60 * 60 * 1000L;
			Date predictedEndDate = new Date(currentDate.getTime() + millisecondsToAdd);
			progressRatesDto.setPredictedEndDate(predictedEndDate);

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
