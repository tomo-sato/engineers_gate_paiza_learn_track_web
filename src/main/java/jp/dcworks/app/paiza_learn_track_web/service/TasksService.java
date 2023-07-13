package jp.dcworks.app.paiza_learn_track_web.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.mybatis.TasksMapper;
import jp.dcworks.app.paiza_learn_track_web.mybatis.entity.TasksMap;

/**
 * 課題サービスクラス。
 *
 * @author tomo-sato
 */
@Service
public class TasksService {

	/** Mapperインターフェース。 */
	@Autowired
	private TasksMapper tasksMapper;

	public Double findGroupBySumLearningHours() {
		return tasksMapper.findGroupBySumLearningHours();
	}

	public List<TasksMap> findGroupByLesson() {
		return tasksMapper.findGroupByLesson();
	}
}
