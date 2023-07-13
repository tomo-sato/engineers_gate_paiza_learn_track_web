package jp.dcworks.app.paiza_learn_track_web.service;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.entity.ProgressRates;
import jp.dcworks.app.paiza_learn_track_web.mybatis.ProgressRatesMapper;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;
import jp.dcworks.app.paiza_learn_track_web.repository.ProgressRatesRepository;

/**
 * 課題進捗率サービスクラス。
 *
 * @author tomo-sato
 */
@Service
public class ProgressRatesService {

	/** Mapperインターフェース。 */
	@Autowired
	private ProgressRatesMapper progressRatesMapper;
	/** リポジトリインターフェース。 */
	@Autowired
	private ProgressRatesRepository progressRatesRepository;

	/**
	 * progress_rates テーブルより team_users_id でグルーピングした学習時間（時）の合計を取得する。
	 *
	 * @param reportDate 集計日
	 * @param sumLearningMinutes 学習時間合計
	 * @return
	 */
	public List<ProgressRatesMap> getSumTotalLearningHours(Date reportDate, Double sumLearningMinutes) {
		return progressRatesMapper.getSumTotalLearningHours(reportDate, sumLearningMinutes);
	}

	/**
	 * ユーザーID、集計日で検索した結果を取得する。
	 *
	 * @param teamUsersId
	 * @param reportDate
	 * @return Map<String{レッスンID}, ProgressRates>
	 */
	public Map<String, ProgressRates> findByTeamUsersIdAndReportDateOrderMap(Long teamUsersId, Date reportDate) {
		List<ProgressRates> list = progressRatesRepository.findByTeamUsersIdAndReportDate(teamUsersId, reportDate);

		Map<String, ProgressRates> retMap = new LinkedHashMap<String, ProgressRates>();
		for (ProgressRates item : list) {
			String lessonId = item.getLessonId();
			retMap.put(lessonId, item);
		}

		return retMap;
	}
}
