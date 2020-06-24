cube(`Apps`, {
  sql: `SELECT * FROM sample.apps`,

  joins: {
    Workspace: {
      sql: `${CUBE}.workspace_id = ${Workspace}.id`,
      relationship: `belongsTo`
    }
  },

  measures: {
		count_git_company: {
			sql: 'git_company',
			type: 'count'
		},
		countDistinct_git_company: {
			sql: 'git_company',
			type: 'countDistinct'
		},
		countDistinctApprox_git_company: {
			sql: 'git_company',
			type: 'countDistinctApprox'
		},
		count_git_provider: {
			sql: 'git_provider',
			type: 'count'
		},
		countDistinct_git_provider: {
			sql: 'git_provider',
			type: 'countDistinct'
		},
		countDistinctApprox_git_provider: {
			sql: 'git_provider',
			type: 'countDistinctApprox'
		},
		count_auth_method: {
			sql: 'auth_method',
			type: 'count'
		},
		countDistinct_auth_method: {
			sql: 'auth_method',
			type: 'countDistinct'
		},
		countDistinctApprox_auth_method: {
			sql: 'auth_method',
			type: 'countDistinctApprox'
		},
		count_auth_table: {
			sql: 'auth_table',
			type: 'count'
		},
		countDistinct_auth_table: {
			sql: 'auth_table',
			type: 'countDistinct'
		},
		countDistinctApprox_auth_table: {
			sql: 'auth_table',
			type: 'countDistinctApprox'
		},
		count_branch_name: {
			sql: 'branch_name',
			type: 'count'
		},
		countDistinct_branch_name: {
			sql: 'branch_name',
			type: 'countDistinct'
		},
		countDistinctApprox_branch_name: {
			sql: 'branch_name',
			type: 'countDistinctApprox'
		},
		count_artifact_id: {
			sql: 'artifact_id',
			type: 'count'
		},
		countDistinct_artifact_id: {
			sql: 'artifact_id',
			type: 'countDistinct'
		},
		countDistinctApprox_artifact_id: {
			sql: 'artifact_id',
			type: 'countDistinctApprox'
		},
		count_caching: {
			sql: 'caching',
			type: 'count'
		},
		countDistinct_caching: {
			sql: 'caching',
			type: 'countDistinct'
		},
		countDistinctApprox_caching: {
			sql: 'caching',
			type: 'countDistinctApprox'
		},
		sum_id: {
			sql: 'id',
			type: 'sum'
		},
		avg_id: {
			sql: 'id',
			type: 'avg'
		},
		min_id: {
			sql: 'id',
			type: 'min'
		},
		max_id: {
			sql: 'id',
			type: 'max'
		},
		runningTotal_id: {
			sql: 'id',
			type: 'runningTotal'
		},
		count_id: {
			sql: 'id',
			type: 'count'
		},
		countDistinct_id: {
			sql: 'id',
			type: 'countDistinct'
		},
		countDistinctApprox_id: {
			sql: 'id',
			type: 'countDistinctApprox'
		},
		count_description: {
			sql: 'description',
			type: 'count'
		},
		countDistinct_description: {
			sql: 'description',
			type: 'countDistinct'
		},
		countDistinctApprox_description: {
			sql: 'description',
			type: 'countDistinctApprox'
		},
		count_destination_path: {
			sql: 'destination_path',
			type: 'count'
		},
		countDistinct_destination_path: {
			sql: 'destination_path',
			type: 'countDistinct'
		},
		countDistinctApprox_destination_path: {
			sql: 'destination_path',
			type: 'countDistinctApprox'
		},
		count_email_templates: {
			sql: 'email_templates',
			type: 'count'
		},
		countDistinct_email_templates: {
			sql: 'email_templates',
			type: 'countDistinct'
		},
		countDistinctApprox_email_templates: {
			sql: 'email_templates',
			type: 'countDistinctApprox'
		},
		count_entity_history: {
			sql: 'entity_history',
			type: 'count'
		},
		countDistinct_entity_history: {
			sql: 'entity_history',
			type: 'countDistinct'
		},
		countDistinctApprox_entity_history: {
			sql: 'entity_history',
			type: 'countDistinctApprox'
		},
		count_generation_type: {
			sql: 'generation_type',
			type: 'count'
		},
		countDistinct_generation_type: {
			sql: 'generation_type',
			type: 'countDistinct'
		},
		countDistinctApprox_generation_type: {
			sql: 'generation_type',
			type: 'countDistinctApprox'
		},
		count_group_id: {
			sql: 'group_id',
			type: 'count'
		},
		countDistinct_group_id: {
			sql: 'group_id',
			type: 'countDistinct'
		},
		countDistinctApprox_group_id: {
			sql: 'group_id',
			type: 'countDistinctApprox'
		},
		count_is_online: {
			sql: 'is_online',
			type: 'count'
		},
		countDistinct_is_online: {
			sql: 'is_online',
			type: 'countDistinct'
		},
		countDistinctApprox_is_online: {
			sql: 'is_online',
			type: 'countDistinctApprox'
		},
		count_jdbc_password: {
			sql: 'jdbc_password',
			type: 'count'
		},
		countDistinct_jdbc_password: {
			sql: 'jdbc_password',
			type: 'countDistinct'
		},
		countDistinctApprox_jdbc_password: {
			sql: 'jdbc_password',
			type: 'countDistinctApprox'
		},
		count_jdbc_url: {
			sql: 'jdbc_url',
			type: 'count'
		},
		countDistinct_jdbc_url: {
			sql: 'jdbc_url',
			type: 'countDistinct'
		},
		countDistinctApprox_jdbc_url: {
			sql: 'jdbc_url',
			type: 'countDistinctApprox'
		},
		count_jdbc_username: {
			sql: 'jdbc_username',
			type: 'count'
		},
		countDistinct_jdbc_username: {
			sql: 'jdbc_username',
			type: 'countDistinct'
		},
		countDistinctApprox_jdbc_username: {
			sql: 'jdbc_username',
			type: 'countDistinctApprox'
		},
		count_name: {
			sql: 'name',
			type: 'count'
		},
		countDistinct_name: {
			sql: 'name',
			type: 'countDistinct'
		},
		countDistinctApprox_name: {
			sql: 'name',
			type: 'countDistinctApprox'
		},
		count_repository_name: {
			sql: 'repository_name',
			type: 'count'
		},
		countDistinct_repository_name: {
			sql: 'repository_name',
			type: 'countDistinct'
		},
		countDistinctApprox_repository_name: {
			sql: 'repository_name',
			type: 'countDistinctApprox'
		},
		count_scheduler: {
			sql: 'scheduler',
			type: 'count'
		},
		countDistinct_scheduler: {
			sql: 'scheduler',
			type: 'countDistinct'
		},
		countDistinctApprox_scheduler: {
			sql: 'scheduler',
			type: 'countDistinctApprox'
		},
		count_schema: {
			sql: 'schema',
			type: 'count'
		},
		countDistinct_schema: {
			sql: 'schema',
			type: 'countDistinct'
		},
		countDistinctApprox_schema: {
			sql: 'schema',
			type: 'countDistinctApprox'
		},
		count_upgrade: {
			sql: 'upgrade',
			type: 'count'
		},
		countDistinct_upgrade: {
			sql: 'upgrade',
			type: 'countDistinct'
		},
		countDistinctApprox_upgrade: {
			sql: 'upgrade',
			type: 'countDistinctApprox'
		},
		count_user_only: {
			sql: 'user_only',
			type: 'count'
		},
		countDistinct_user_only: {
			sql: 'user_only',
			type: 'countDistinct'
		},
		countDistinctApprox_user_only: {
			sql: 'user_only',
			type: 'countDistinctApprox'
		},
		min_created_date: {
			sql: 'created_date',
			type: 'min'
		},
		max_created_date: {
			sql: 'created_date',
			type: 'max'
		},
		count_created_date: {
			sql: 'created_date',
			type: 'count'
		},
		countDistinct_created_date: {
			sql: 'created_date',
			type: 'countDistinct'
		},
		countDistinctApprox_created_date: {
			sql: 'created_date',
			type: 'countDistinctApprox'
		},
		count_change: {
            sql: `${CUBE.count_created_date} + 5 * 3`,
            type: `number`,
        },

  },

  dimensions: {
    gitCompany: {
      sql: `git_company`,
      type: `string`
    },

    gitProvider: {
      sql: `git_provider`,
      type: `string`
    },

    authMethod: {
      sql: `auth_method`,
      type: `string`
    },

    authTable: {
      sql: `auth_table`,
      type: `string`
    },

    branchName: {
      sql: `branch_name`,
      type: `string`
    },

    artifactId: {
      sql: `artifact_id`,
      type: `string`
    },

    caching: {
      sql: `caching`,
      type: `string`
    },

    id: {
      sql: `id`,
      type: `number`,
      primaryKey: true
    },

    description: {
      sql: `description`,
      type: `string`
    },

    destinationPath: {
      sql: `destination_path`,
      type: `string`
    },

    emailTemplates: {
      sql: `email_templates`,
      type: `string`
    },

    entityHistory: {
      sql: `entity_history`,
      type: `string`
    },

    generationType: {
      sql: `generation_type`,
      type: `string`
    },

    groupId: {
      sql: `group_id`,
      type: `string`
    },

    isOnline: {
      sql: `is_online`,
      type: `string`
    },

    jdbcPassword: {
      sql: `jdbc_password`,
      type: `string`
    },

    jdbcUrl: {
      sql: `jdbc_url`,
      type: `string`
    },

    jdbcUsername: {
      sql: `jdbc_username`,
      type: `string`
    },

    name: {
      sql: `name`,
      type: `string`
    },

    repositoryName: {
      sql: `repository_name`,
      type: `string`
    },

    scheduler: {
      sql: `scheduler`,
      type: `string`
    },

    schema: {
      sql: `schema`,
      type: `string`
    },

    upgrade: {
      sql: `upgrade`,
      type: `string`
    },

    userOnly: {
      sql: `user_only`,
      type: `string`
    },

    createdDate: {
      sql: `created_date`,
      type: `time`
    }
  }
});
