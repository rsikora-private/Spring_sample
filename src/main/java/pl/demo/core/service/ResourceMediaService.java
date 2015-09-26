package pl.demo.core.service;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;
import pl.demo.core.model.entity.MediaResource;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by robertsikora on 29.07.15.
 */

@Validated
public interface ResourceMediaService extends CRUDService<Long, MediaResource> {
    @NotNull
    Long upload(@NotNull MultipartFile file);

    void deleteImage(@NotNull Serializable id);

    void saveOnCallback(@NotNull Long id, @NotNull String publicId);

    MediaResource getFirst(@NotNull Long advertId);

    @NotNull @NotBlank
    String getThumb(@NotNull Serializable id);
}
