package jhyun.jh.controllers;

import com.google.common.base.Strings;
import io.swagger.annotations.*;
import jhyun.jh.controllers.exceptions.SavedUrlNotFoundException;
import jhyun.jh.services.SavedUrlListingService;
import jhyun.jh.services.UrlShorteningService;
import jhyun.jh.storage.entities.Url;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import static com.google.common.base.Preconditions.checkArgument;

@Api
@RestController
public class IndexController {

    // TODO: test

    private UrlShorteningService urlShorteningService;
    private SavedUrlListingService savedUrlListingService;

    @Autowired
    public IndexController(UrlShorteningService urlShorteningService,
                           SavedUrlListingService savedUrlListingService) {
        this.urlShorteningService = urlShorteningService;
        this.savedUrlListingService = savedUrlListingService;
    }

    @RequestMapping("/")
    public RedirectView index() {
        return new RedirectView("/index.html");
    }

    @ApiOperation(value = "POST field `url`으로 주어진 URL을 shortened-code으로 축약해 저장")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK, `text/plain`으로 응답, 응답본문 전체가 축약코드")
    })
    @RequestMapping(value = "/urls/shorten", method = RequestMethod.POST,
            produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> shortenUrl(
            @ApiParam(value = "축약할 URL")
            @RequestParam(value = "url", required = true)
                    String url
    ) {
        checkArgument(!Strings.isNullOrEmpty(url));
        val shortenedCode = urlShorteningService.shorten(url);
        return new ResponseEntity<>(shortenedCode, HttpStatus.OK);
    }

    @ApiOperation(value = "저장된 URL정보 리스팅")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK")
    })
    @RequestMapping(value = "/urls/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<Url> listUrls(
            @ApiParam(value = "page number (starting from 0)")
            @RequestParam(value = "page", required = false, defaultValue = "0")
                    Integer page,
            @ApiParam(value = "page size (rows per page)")
            @RequestParam(value = "size", required = false, defaultValue = "50")
                    Integer size
    ) {
        return savedUrlListingService.list(page, size);
    }

    @ApiOperation(value = "축약 코드에 저장된 원본URL으로 Redirect")
    @ApiResponses({
            @ApiResponse(code = 302, message = "Redirect to saved URL"),
            @ApiResponse(code = 404, message = "Saved URL for Shortened code not found")
    })
    @RequestMapping(value = "/{shortenedCode:[a-zA-Z0-9]+}")
    public RedirectView redirectToUrl(
            @ApiParam(value = "축약코드", required = true)
            @PathVariable("shortenedCode")
                    String shortenedCode
    ) {
        checkArgument(!Strings.isNullOrEmpty(shortenedCode));
        val url = urlShorteningService.expand(shortenedCode);
        if (url.isPresent()) {
            return new RedirectView(url.get(), false);
        } else {
            throw new SavedUrlNotFoundException();
        }
    }
}
