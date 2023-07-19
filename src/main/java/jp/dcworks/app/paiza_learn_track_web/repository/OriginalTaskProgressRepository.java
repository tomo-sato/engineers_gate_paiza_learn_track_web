package jp.dcworks.app.paiza_learn_track_web.repository;

import java.util.Date;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.app.paiza_learn_track_web.entity.OriginalTaskProgress;

/**
 * オリジナル課題進捗管理リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface OriginalTaskProgressRepository extends PagingAndSortingRepository<OriginalTaskProgress, Long>, CrudRepository<OriginalTaskProgress, Long> {

	Optional<OriginalTaskProgress> findByTeamUsersIdAndChapterIdAndReportDate(Long teamUsersId, Integer chapterId, Date reportDate);
}
