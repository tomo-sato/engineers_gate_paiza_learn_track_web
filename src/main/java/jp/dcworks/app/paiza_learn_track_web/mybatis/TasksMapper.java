package jp.dcworks.app.paiza_learn_track_web.mybatis;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface TasksMapper {

	/**
	 * tasks テーブルより task_types_id:1.paiza でグルーピングした学習時間（時）の合計を取得する。
	 *
	 * @return
	 */
	Double findGroupBySumLearningHours();
}
