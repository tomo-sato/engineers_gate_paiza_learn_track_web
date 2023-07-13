package jp.dcworks.app.paiza_learn_track_web.dto;

import java.util.Date;

import lombok.Data;

/**
 * 講座別進捗一覧画面DTOクラス。
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

	/**
	 * 進捗率の計算結果を取得する。
	 *
	 * @return
	 */
	public Double getLearningRate() {
		if (this.sumAchievedLearningHours == null) {
			return 0.0;
		}
		return (this.sumAchievedLearningHours / this.sumTotalLearningHours) * 100;
	}
}
