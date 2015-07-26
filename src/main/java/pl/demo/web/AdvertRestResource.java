package pl.demo.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.util.Utils;
import pl.demo.web.dto.EMailDTO;
import pl.demo.web.dto.SearchCriteriaDTO;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.service.AdvertService;

import javax.validation.Valid;

@RestController
@RequestMapping("/adverts")
public class AdvertRestResource extends AbstractCRUDResource<Long, Advert> {

    private final AdvertService advertService;

    @Autowired
    public AdvertRestResource(final AdvertService advertService) {
        super(advertService);
        this.advertService = advertService;
    }

    @RequestMapping(value = "/new",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Advert> createNew() {
        return new ResponseEntity<>(advertService.createNew(), HttpStatus.OK);
    }

    @RequestMapping(value = "/search",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<?> search(final SearchCriteriaDTO searchCriteriaDTO, final Pageable pageable) {

        return ResponseEntity
                .ok()
                // .header("Cache-Control", "private, no-store, max-age=120") for HTTP 1.1
                //  .header("Expires", cal.getTime().toGMTString()) turn on browser cache for HTTP 1.0.
                //  Now is the 'Expires' header is deprecated
                .body(this.advertService.findBySearchCriteria(searchCriteriaDTO, pageable));
    }

    @RequestMapping(value = "/{id}/status",
            method = RequestMethod.POST)

    public ResponseEntity<?> updatePartially(@PathVariable("id") final Long id, @RequestParam(value="status") final String status) {
        advertService.updateActive(id, Boolean.valueOf(status));
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/email",
            method = RequestMethod.POST)

    public ResponseEntity<?> sendEmail(@PathVariable("id") final Long id, @Valid @RequestBody final EMailDTO email, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Utils.createErrorMessage(bindingResult));
        }
        advertService.sendMail(email);
        return ResponseEntity.noContent().build();
    }

    @RequestMapping(value = "/{id}/comment",
            method = RequestMethod.POST)

    public ResponseEntity<?> postComment(@PathVariable("id") final Long id, @Valid @RequestBody final Comment comment, final BindingResult bindingResult) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(Utils.createErrorMessage(bindingResult));
        }
        advertService.postComment(id, comment);
        return ResponseEntity.noContent().build();
    }
}
