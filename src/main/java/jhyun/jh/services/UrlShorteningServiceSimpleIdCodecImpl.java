package jhyun.jh.services;

import jhyun.jh.storage.entities.Url;
import jhyun.jh.storage.repositories.UrlRepository;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static jhyun.jh.IdCodec.decode;
import static jhyun.jh.IdCodec.encode;

@Slf4j
@Primary
@Service
public class UrlShorteningServiceSimpleIdCodecImpl implements UrlShorteningService {

    private UrlRepository urlRepository;

    @Autowired
    public UrlShorteningServiceSimpleIdCodecImpl(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;
    }

    private String getShortenedCode(final Url url) {
        val id = url.getId();
        if (id != null) {
            return encode(id);
        } else {
            throw new RuntimeException(
                    String.format("Found saved `Url` but without id -- %s", url));
        }
    }

    @Override
    public String shorten(String url) {
        val savedUrl = urlRepository.findOneByUrl(url);
        if (savedUrl != null) {
            return getShortenedCode(savedUrl);
        } else {
            val it = urlRepository.save(Url.builder().url(url).build());
            if (it != null) {
                return getShortenedCode(it);
            } else {
                throw new RuntimeException(
                        String.format("Saving new `Url` for %s FAILED", url));
            }
        }
    }

    @Override
    public Optional<String> expand(String shortenedCode) {
        val id = decode(shortenedCode);
        val savedUrl = urlRepository.findOne(id);
        if (savedUrl != null) {
            return Optional.of(savedUrl.getUrl());
        } else {
            return Optional.empty();
        }
    }
}
