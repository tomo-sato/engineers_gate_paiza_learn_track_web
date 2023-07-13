package jp.dcworks.app.paiza_learn_track_web.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TasksMap;

@Mapper
public interface TasksMapper {

	/**
	 * tasks テーブルより task_types_id:1.paiza でグルーピングした学習時間（時）の合計を取得する。
	 *
	 * @return
	 */
	Double findGroupBySumLearningHours();

	/**
	 * tasks テーブルより レッスンでグルーピングした結果を取得する。
	 *
	 * @return
	 */
	List<TasksMap> findGroupByLesson();
}
