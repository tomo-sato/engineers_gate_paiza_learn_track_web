package jp.dcworks.app.paiza_learn_track_web.dto;

import java.util.Date;

import lombok.Data;

/**
 * MyBatis課題進捗率Entityクラス。
 *
 * @author tomo-sato
 */
@Data
public class UserProgressRatesDto {

	/** 課題ID */
	private Long maxTasksId;

	/** 講座ID */
	private Integer courseId;

	/** 講座名 */
	private String courseName;

	/** レッスン名 */
	private String lessonName;

	/** 学習時間合計（時） */
	private Double sumTotalLearningHours;

	/** 学習時間実績（時） */
	private Double sumAchievedLearningHours;

	/** 進捗率 */
	@SuppressWarnings("unused")
	private Double learningRate;

	/** レッスン最終受講日時 */
	private Date lessonLastAccessDatetime;

	public Double getLearningRate() {
		if (this.sumAchievedLearningHours == null) {
			return 0.0;
		}
		return (this.sumAchievedLearningHours / this.sumTotalLearningHours) * 100;
	}
}
