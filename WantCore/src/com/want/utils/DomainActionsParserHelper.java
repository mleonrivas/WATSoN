package com.want.utils;

import com.want.core.IDomainActionsParser;
import com.want.domains.DefaultActionsParser;

public class DomainActionsParserHelper {

	public static IDomainActionsParser getDomainActionsParser() {
		IDomainActionsParser result = null;
		try {
			Class<?> t = Class.forName(ConfigurationProperties.getInstance()
					.getProperties().getProperty("current_domain_parser",
							DefaultProperties.DEFAULT_DOMAIN_PARSER));
			result = (IDomainActionsParser) t.newInstance();
		} catch (Exception e) {
			e.printStackTrace();

		}
		if (result == null) {
			result = new DefaultActionsParser();
		}
		System.out.println("------%%%%%-- RESULT: "
				+ result.getClass().getName());
		return result;
	}

}
