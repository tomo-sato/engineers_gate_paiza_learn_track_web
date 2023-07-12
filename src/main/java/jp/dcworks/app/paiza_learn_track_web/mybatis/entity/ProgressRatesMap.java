package jp.dcworks.app.paiza_learn_track_web.mybatis.entity;

import lombok.Data;

/**
 * MyBatis課題進捗率Entityクラス。
 *
 * @author tomo-sato
 */
@Data
public class ProgressRatesMap {

	/** チームユーザーID */
	private Long teamUsersId;

	/** 名前 */
	private String name;

	/** 学習時間合計（時） */
	private String sumTotalLearningHours;
}
