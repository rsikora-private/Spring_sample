package pl.demo.core.service.basic_service;

import org.springframework.security.access.prepost.PreAuthorize;
import pl.demo.core.model.entity.BaseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;

/**
 * Created by Robert on 12.01.15.
 */

public interface CRUDService<PK extends Serializable, E extends BaseEntity> {

    @NotNull
    @Valid
    Collection<E> findAll();

    @NotNull
    @Valid
    E findOne(@NotNull PK id);

    @PreAuthorize("isAuthenticated()")
    void delete(@NotNull PK id);

    @PreAuthorize("isAuthenticated()")
    void edit(@NotNull PK id, @NotNull @Valid E entity);

    @NotNull
    @Valid
    E save(@NotNull @Valid E entity);
}