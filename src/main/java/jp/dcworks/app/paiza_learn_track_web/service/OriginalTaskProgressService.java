package jp.dcworks.app.paiza_learn_track_web.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.dcworks.app.paiza_learn_track_web.entity.OriginalTaskProgress;
import jp.dcworks.app.paiza_learn_track_web.repository.OriginalTaskProgressRepository;

/**
 * オリジナル課題進捗管理サービスクラス。
 *
 * @author tomo-sato
 */
@Service
public class OriginalTaskProgressService {

	/** リポジトリインターフェース。 */
	@Autowired
	private OriginalTaskProgressRepository originalTaskProgressRepository;

	/**
	 * オリジナル課題進捗管理を登録する。
	 *
	 * @param teamUsersId
	 * @param chapterId
	 * @param taskProgressRate
	 * @param reportDate
	 * @return
	 */
	public OriginalTaskProgress save(Long teamUsersId, Integer chapterId, Double taskProgressRate, Date reportDate) {

		OriginalTaskProgress originalTaskProgress =
				originalTaskProgressRepository.findByTeamUsersIdAndChapterIdAndReportDate(teamUsersId, chapterId, reportDate).orElse(null);

		if (originalTaskProgress == null) {
			originalTaskProgress = new OriginalTaskProgress();
			originalTaskProgress.setTeamUsersId(teamUsersId);
			originalTaskProgress.setChapterId(chapterId);
			originalTaskProgress.setReportDate(reportDate);
		}

		originalTaskProgress.setTaskProgressRate(taskProgressRate);
		return originalTaskProgressRepository.save(originalTaskProgress);
	}
}
