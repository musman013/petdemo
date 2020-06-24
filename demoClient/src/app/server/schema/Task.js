cube(`Task`, {
  sql: `SELECT * FROM sample.task`,
  
  joins: {
    Apps: {
      sql: `${CUBE}.app_id = ${Apps}.id`,
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
		count_status: {
			sql: 'status',
			type: 'count'
		},
		countDistinct_status: {
			sql: 'status',
			type: 'countDistinct'
		},
		countDistinctApprox_status: {
			sql: 'status',
			type: 'countDistinctApprox'
		},
		count_type: {
			sql: 'type',
			type: 'count'
		},
		countDistinct_type: {
			sql: 'type',
			type: 'countDistinct'
		},
		countDistinctApprox_type: {
			sql: 'type',
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
    
    status: {
      sql: `status`,
      type: `string`
    },
    
    type: {
      sql: `type`,
      type: `string`
    }
  }
});
