package jp.dcworks.app.paiza_learn_track_web.mybatis;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.ProgressRatesMap;

@Mapper
public interface ProgressRatesMapper {

	/**
	 * progress_rates テーブルより team_users_id でグルーピングした学習時間（時）の合計を取得する。
	 *
	 * @param reportDate 集計日
	 * @param sumLearningMinutes 学習時間合計
	 * @return
	 */
	List<ProgressRatesMap> getSumTotalLearningHours(@Param("report_date") Date reportDate, @Param("sum_learning_minutes") Double sumLearningMinutes);
}
