cube(`Gitaccount`, {
  sql: `SELECT * FROM sample.gitaccount`,
  
  joins: {
    User: {
      sql: `${CUBE}.user_id = ${User}.id`,
      relationship: `belongsTo`
    }
  },
  
  measures: {
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
		count_git_email: {
			sql: 'git_email',
			type: 'count'
		},
		countDistinct_git_email: {
			sql: 'git_email',
			type: 'countDistinct'
		},
		countDistinctApprox_git_email: {
			sql: 'git_email',
			type: 'countDistinctApprox'
		},
		count_git_oauth_token: {
			sql: 'git_oauth_token',
			type: 'count'
		},
		countDistinct_git_oauth_token: {
			sql: 'git_oauth_token',
			type: 'countDistinct'
		},
		countDistinctApprox_git_oauth_token: {
			sql: 'git_oauth_token',
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
		count_git_user: {
			sql: 'git_user',
			type: 'count'
		},
		countDistinct_git_user: {
			sql: 'git_user',
			type: 'countDistinct'
		},
		countDistinctApprox_git_user: {
			sql: 'git_user',
			type: 'countDistinctApprox'
		},
		count_github_company: {
			sql: 'github_company',
			type: 'count'
		},
		countDistinct_github_company: {
			sql: 'github_company',
			type: 'countDistinct'
		},
		countDistinctApprox_github_company: {
			sql: 'github_company',
			type: 'countDistinctApprox'
		},
		count_github_location: {
			sql: 'github_location',
			type: 'count'
		},
		countDistinct_github_location: {
			sql: 'github_location',
			type: 'countDistinct'
		},
		countDistinctApprox_github_location: {
			sql: 'github_location',
			type: 'countDistinctApprox'
		},
		count_is_company_account: {
			sql: 'is_company_account',
			type: 'count'
		},
		countDistinct_is_company_account: {
			sql: 'is_company_account',
			type: 'countDistinct'
		},
		countDistinctApprox_is_company_account: {
			sql: 'is_company_account',
			type: 'countDistinctApprox'
		},

  },
  
  dimensions: {
    id: {
      sql: `id`,
      type: `number`,
      primaryKey: true
    },
    
    gitEmail: {
      sql: `git_email`,
      type: `string`
    },
    
    gitOauthToken: {
      sql: `git_oauth_token`,
      type: `string`
    },
    
    gitProvider: {
      sql: `git_provider`,
      type: `string`
    },
    
    gitUser: {
      sql: `git_user`,
      type: `string`
    },
    
    githubCompany: {
      sql: `github_company`,
      type: `string`
    },
    
    githubLocation: {
      sql: `github_location`,
      type: `string`
    },
    
    isCompanyAccount: {
      sql: `is_company_account`,
      type: `string`
    }
  }
});
