package jp.dcworks.app.paiza_learn_track_web.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.mybatis.TeamUserTaskProgressMapper;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TeamUserTaskProgressMap;

/**
 * 課題サービスクラス。
 *
 * @author tomo-sato
 */
@Service
public class TeamUserTaskProgressService {

	/** Mapperインターフェース。 */
	@Autowired
	private TeamUserTaskProgressMapper teamUserTaskProgressMapper;

	public List<TeamUserTaskProgressMap> getLastAccessLesson() {
		return teamUserTaskProgressMapper.getLastAccessLesson();
	}

	public Map<Long, TeamUserTaskProgressMap> getLastAccessLessonMap() {
		List<TeamUserTaskProgressMap> list = teamUserTaskProgressMapper.getLastAccessLesson();

		Map<Long, TeamUserTaskProgressMap> retMap = new HashMap<Long, TeamUserTaskProgressMap>();
		for (TeamUserTaskProgressMap item : list) {
			Long id = item.getTeamUsersId();
			retMap.put(id, item);
		}

		return retMap;
	}
}
