package jp.dcworks.app.paiza_learn_track_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.app.paiza_learn_track_web.entity.TeamUserTaskProgress;

/**
 * チームユーザー課題進捗関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface TeamUserTaskProgressRepository extends PagingAndSortingRepository<TeamUserTaskProgress, Long>, CrudRepository<TeamUserTaskProgress, Long> {
}
