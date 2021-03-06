package org.nlpcn.es4sql.query;

import org.elasticsearch.action.ActionRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.support.IndicesOptions;
import org.elasticsearch.client.Client;
import org.nlpcn.es4sql.domain.Query;
import org.nlpcn.es4sql.domain.Select;
import org.nlpcn.es4sql.domain.hints.Hint;
import org.nlpcn.es4sql.domain.hints.HintType;
import org.nlpcn.es4sql.exception.SqlParseException;

/**
 * Abstract class. used to transform Select object (Represents SQL query) to
 * SearchRequestBuilder (Represents ES query)
 */
public abstract class QueryAction {

	protected org.nlpcn.es4sql.domain.Query query;

	protected Client client;


	public QueryAction(Client client, Query query) {
		this.client = client;
		this.query = query;
	}

    protected void updateWithIndicesOptionsIfNeeded(Select select,SearchRequestBuilder request) {
        boolean ignore = false;
        for(Hint hint : select.getHints()){
            if(hint.getType() == HintType.IGNORE_UNAVAILABLE){
                ignore = true;
                break;
            }
        }
        if(ignore)
            //saving the defaults from TransportClient search
            request.setIndicesOptions(IndicesOptions.fromOptions(true, false, true, false, IndicesOptions.strictExpandOpenAndForbidClosed()));
    }



	/**
	 * Prepare the request, and return ES request.
	 * @return ActionRequestBuilder (ES request)
	 * @throws SqlParseException
	 */
	public abstract SqlElasticRequestBuilder explain() throws SqlParseException;

}
