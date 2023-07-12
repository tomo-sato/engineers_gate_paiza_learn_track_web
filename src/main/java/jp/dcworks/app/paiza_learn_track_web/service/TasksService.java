package jp.dcworks.app.paiza_learn_track_web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.mybatis.TasksMapper;

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
}
