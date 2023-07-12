package jp.dcworks.app.paiza_learn_track_web.mybatis;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TeamUserTaskProgressMap;

@Mapper
public interface TeamUserTaskProgressMapper {

	/**
	 * team_user_task_progress テーブルより受講生の最終着手課題を取得する。
	 *
	 * @return
	 */
	List<TeamUserTaskProgressMap> getLastAccessLesson();
}
