package com.fastcode.demopet.domain;

import org.hibernate.boot.model.relational.Namespace;
import org.hibernate.boot.model.relational.Sequence;
import org.hibernate.mapping.Table;
import org.hibernate.tool.schema.spi.SchemaFilter;

public class CustomSchemaFilter implements SchemaFilter {
	public static final CustomSchemaFilter INSTANCE = new CustomSchemaFilter();

	@Override
	public boolean includeNamespace(Namespace namespace) {
		return true;
	}

	@Override
	public boolean includeTable(Table table) {
		Boolean canInclude = true;
		if(table.getName().equalsIgnoreCase("ACT_ID_TOKEN")) {
			canInclude = false;
		}
		else if(table.getName().equalsIgnoreCase("ACT_ID_USER")) {
			canInclude = false;
		}
		else if(table.getName().equalsIgnoreCase("ACT_ID_PRIV_MAPPING")) {
			canInclude = false;
		}
		else if(table.getName().equalsIgnoreCase("ACT_ID_PRIV")) {
			canInclude = false;
		}
		else if(table.getName().equalsIgnoreCase("ACT_ID_MEMBERSHIP")) {
			canInclude = false;
		}
		else if(table.getName().equalsIgnoreCase("ACT_ID_GROUP")) {
			canInclude = false;
		}
			return canInclude;
	}

	@Override
	public boolean includeSequence(Sequence sequence) {
		return true;
	}
	
}
