package jhyun.jh.storage.repositories;

import jhyun.jh.storage.entities.Url;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UrlRepository extends PagingAndSortingRepository<Url, Integer> {

    Url findOneByUrl(String url);
    
}
