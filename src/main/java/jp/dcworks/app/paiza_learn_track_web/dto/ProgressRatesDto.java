package jp.dcworks.app.paiza_learn_track_web.dto;

import java.util.Date;

import lombok.Data;

/**
 * MyBatis課題進捗率Entityクラス。
 *
 * @author tomo-sato
 */
@Data
public class ProgressRatesDto {

	/** チームユーザーID */
	private Long teamUsersId;

	/** 名前 */
	private String name;

	/** 学習時間合計（時） */
	private String sumTotalLearningHours;

	/** 講座名 */
	private String courseName;

	/** レッスン名 */
	private String lessonName;

	/** チャプター名 */
	private String chapterName;

	/** チャプター最終受講日時 */
	private Date chapterLastAccessDatetime;
}