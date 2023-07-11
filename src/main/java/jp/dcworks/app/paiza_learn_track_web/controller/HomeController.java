package jp.dcworks.app.paiza_learn_track_web.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;
import jp.dcworks.app.paiza_learn_track_web.service.ProgressRatesService;
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
	ProgressRatesService progressRatesService;

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

		List<ProgressRatesMap> progressRatesList = progressRatesService.getSumTotalLearningHours(reportDate);

		model.addAttribute("progressRatesList", progressRatesList);
		return "index";
	}

	/**
	 * [GET]講座別進捗一覧画面のアクション。
	 *
	 * @param model 画面にデータを送るためのオブジェクト
	 */
	@GetMapping("/detail/{id}")
	public String detail(@PathVariable Long id, Model model) {
		return "detail";
	}
}
