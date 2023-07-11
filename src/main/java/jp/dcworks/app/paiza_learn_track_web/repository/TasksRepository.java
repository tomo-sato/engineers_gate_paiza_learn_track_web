package jp.dcworks.app.paiza_learn_track_web.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import jp.dcworks.app.paiza_learn_track_web.entity.Tasks;

/**
 * 課題関連リポジトリインターフェース。
 *
 * @author tomo-sato
 */
public interface TasksRepository extends PagingAndSortingRepository<Tasks, Long>, CrudRepository<Tasks, Long> {
}
