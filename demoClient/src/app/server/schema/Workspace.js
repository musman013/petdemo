cube(`Workspace`, {
  sql: `SELECT * FROM sample.workspace`,
  
  joins: {
    
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

  },
  
  dimensions: {
    id: {
      sql: `id`,
      type: `number`,
      primaryKey: true
    },
    
    description: {
      sql: `description`,
      type: `string`
    },
    
    name: {
      sql: `name`,
      type: `string`
    },
    
    createdDate: {
      sql: `created_date`,
      type: `time`
    }
  }
});
