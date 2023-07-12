package jp.dcworks.app.paiza_learn_track_web.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.mybatis.ProgressRatesMapper;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;

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

	public List<ProgressRatesMap> getSumTotalLearningHours(Date reportDate, Double sumLearningMinutes) {
		return progressRatesMapper.getSumTotalLearningHours(reportDate, sumLearningMinutes);
	}
}
