package pl.demo.core.model.repo.fullTextSearch.queryBuilder;

import org.apache.lucene.search.Query;
import org.hibernate.search.query.dsl.QueryBuilder;
import pl.demo.core.util.Assert;
import pl.demo.web.dto.SearchCriteria;

import java.util.Optional;

/**
 * Created by robertsikora on 29.09.15.
 */
public class CommentSearchQueryBuiderImpl implements SearchQueryBuilder{

    private final static String TEXT_FIELD = "text";

    private final SearchCriteria searchCriteria;

    public CommentSearchQueryBuiderImpl(final SearchCriteria searchCriteria) {
        this.searchCriteria = searchCriteria;
    }

    private Query applyKeywordCriteria(final QueryBuilder qb){
        if (searchCriteria.hasKeyword()) {
            return qb
                    .keyword()
                    .onFields(TEXT_FIELD)
                    .matching(searchCriteria.getKeyWords())
                    .createQuery();
        }
        return null;
    }

    @Override
    public Optional<Query> build(final QueryBuilder queryBuilder) {
        Assert.notNull(queryBuilder);

        return Optional.ofNullable(applyKeywordCriteria(queryBuilder));
    }
}
