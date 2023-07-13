package jp.dcworks.app.paiza_learn_track_web.mybatis.entity;

import lombok.Data;

/**
 * MyBatis課題進捗率Entityクラス。
 *
 * @author tomo-sato
 */
@Data
public class TasksMap {

	/** 課題ID */
	private Long maxTasksId;

	/** 講座ID */
	private Integer courseId;

	/** 講座名 */
	private String courseName;

	/** レッスンID */
	private String lessonId;

	/** レッスン名 */
	private String lessonName;

	/** 学習時間合計（時） */
	private Double sumLearningMinutes;
}
