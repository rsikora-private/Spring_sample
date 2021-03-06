package pl.demo.core.service.advert;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import pl.demo.core.aspects.DetachEntity;
import pl.demo.core.model.entity.Advert;
import pl.demo.core.model.entity.Comment;
import pl.demo.core.model.entity.MediaResource;
import pl.demo.core.model.entity.User;
import pl.demo.core.model.repo.AdvertRepository;
import pl.demo.core.model.repo.fullTextSearch.SearchableRepository;
import pl.demo.core.service.basicservice.CRUDServiceImpl;
import pl.demo.core.service.mail.TemplateType;
import pl.demo.core.service.mail.event.SendMailEvent;
import pl.demo.core.service.resource.ResourceMediaService;
import pl.demo.core.service.searching.SearchService;
import pl.demo.core.service.security.AuthenticationContextProvider;
import pl.demo.core.service.security.SecurityUser;
import pl.demo.core.service.user.UserService;
import pl.demo.core.util.Assert;
import pl.demo.core.util.EntityUtils;
import pl.demo.web.HttpSessionContext;
import pl.demo.web.dto.EMail;
import pl.demo.web.dto.SearchCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public class AdvertServiceImpl extends CRUDServiceImpl<Long, Advert> implements AdvertService {

	private SearchService 				searchService;
	private UserService 				userService;
	private HttpSessionContext   		httpSessionContext;
	private ResourceMediaService 		resourceMediaService;

	@Transactional
	@Override
	public Advert save(final Advert advert) {
		Assert.notNull(advert);
		Advert saved;
		try {
			final SecurityUser securityUser = AuthenticationContextProvider.getAuthenticatedUser();
			advert.setUser(new User(securityUser.getId()));
			saved = super.save(advert);
			final Iterator<Long> idIterator = httpSessionContext.getUploadedResourcesId();
			while (idIterator.hasNext()) {
				final Long id = idIterator.next();
				final MediaResource mediaResource = resourceMediaService.findOne(id);
				mediaResource.setAdvert(saved);
				resourceMediaService.save(mediaResource);
			}
		}finally {
			httpSessionContext.close();
		}
		return saved;
	}

	@Transactional(readOnly = true)
	@DetachEntity
	@Override
	public Collection<Advert> findByUserId(final Long userId){
		Assert.notNull(userId);
		return getAdvertRepository().findByUserId(userId);
	}

	@Transactional(readOnly = true)
	@Override
	public Advert createNew() {
		final SecurityUser securityUser = AuthenticationContextProvider.getAuthenticatedUser();
		final User userDB = userService.findOne(securityUser.getId());
		Assert.notNull(userDB, "User doesn't exist in db !");
		return Advert.AdvertBuilder.anAdvert()
				.withOwnerName(userDB.getName())
				.withContact(userDB.getContact())
				.build();
	}

	@Transactional(readOnly = true)
	@Override
	public Page<Advert> findAll(final Pageable pageable) {
		Assert.notNull(pageable);
		return findBySearchCriteria(new SearchCriteria(), pageable);
	}

	@Transactional(readOnly = true)
	@DetachEntity
	@Override
	public Page<Advert> findBySearchCriteria(final SearchCriteria searchCriteriaDTO, final Pageable pageable) {

		Page<Advert> adverts = SearchableRepository.EMPTY_PAGE;
		if(searchCriteriaDTO.isEmpty()) {
			adverts = getAdvertRepository().findByActive(Boolean.TRUE, pageable);
		} else {
			Assert.isTrue(searchCriteriaDTO.isAdvertSearchMode() || searchCriteriaDTO.isCommentSearchMode());

			if(searchCriteriaDTO.isAdvertSearchMode()) {
				adverts = searchService.searchAdverts(searchCriteriaDTO, pageable);
			}else if(searchCriteriaDTO.isCommentSearchMode()){
				final Page<Comment> comments = searchService.searchComments(searchCriteriaDTO, pageable);
				if(comments.hasContent()){
					final List<Advert> advertList = new ArrayList<>();
					comments.getContent().forEach(c->advertList.add((Advert)EntityUtils.initializeHibernateEntity(c.getAdvert())));
					adverts = new PageImpl<>(advertList, pageable, advertList.size());
				}
			}
		}
		adverts.getContent().forEach(t-> t.setThumbUrl(resourceMediaService.getThumb(t.getId())));
		return adverts;
	}

	@Transactional
	@Override
	public void updateActiveStatus(final Long advertId, final Boolean status) {
		Assert.notNull(advertId, "Advert is required");
		final Advert dbAdvert = getDomainRepository().findOne(advertId);
		Assert.notResourceFound(dbAdvert);
		dbAdvert.setActive(status);
		super.save(dbAdvert);
	}

	@Override
	public void sendMail(final EMail eMailDTO) {
		Assert.notNull(eMailDTO, "Email data is required");
		publishBusinessEvent(new SendMailEvent(eMailDTO, TemplateType.ADVERT_TEMPLATE));
	}

	private AdvertRepository getAdvertRepository(){
		return (AdvertRepository)getDomainRepository();
	}

	@Autowired
	public void setSearchService(final SearchService searchService) {
		this.searchService = searchService;
	}

	@Autowired
	public void setUserService(final UserService userService) {
		this.userService = userService;
	}

	@Autowired
	public void setHttpSessionContext(final HttpSessionContext httpSessionContext) {
		this.httpSessionContext = httpSessionContext;
	}

	@Autowired
	public void setResourceMediaService(final ResourceMediaService resourceMediaService) {
		this.resourceMediaService = resourceMediaService;
	}
}
