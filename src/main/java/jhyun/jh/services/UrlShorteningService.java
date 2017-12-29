package jhyun.jh.services;

import java.util.Optional;

public interface UrlShorteningService {
    /**
     * `url` 을 줄여, shortened code으로.
     */
    String shorten(String url);

    /**
     * `shortenedCode` 을 다시 원래의 URL으로
     */
    Optional<String> expand(String shortenedCode);
}
