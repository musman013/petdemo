cube(`Gitproject`, {
  sql: `SELECT * FROM sample.gitproject`,
  
  joins: {
    
  },
  
  measures: {
		count_git_projects: {
			sql: 'git_projects',
			type: 'count'
		},
		countDistinct_git_projects: {
			sql: 'git_projects',
			type: 'countDistinct'
		},
		countDistinctApprox_git_projects: {
			sql: 'git_projects',
			type: 'countDistinctApprox'
		},

  },
  
  dimensions: {
    gitProjects: {
      sql: `git_projects`,
      type: `string`
    }
  }
});

