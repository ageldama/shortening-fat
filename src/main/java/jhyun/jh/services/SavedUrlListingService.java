package jhyun.jh.services;

import jhyun.jh.storage.entities.Url;
import jhyun.jh.storage.repositories.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class SavedUrlListingService {
    private UrlRepository urlRepository;

    @Autowired
    public SavedUrlListingService(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    public Page<Url> list(final int page, final int size) {
        return urlRepository.findAll(new PageRequest(page, size, Sort.Direction.DESC, "id"));
    }
}
