cube(`Invoices`, {
  sql: `SELECT * FROM sample.invoices`,
  
  joins: {
    Visits: {
      sql: `${CUBE}.visit_id = ${Visits}.id`,
      relationship: `belongsTo`
    }
  },
  
  measures: {
		sum_amount: {
			sql: `amount`,
			type: `sum`
		},
		avg_amount: {
			sql: 'amount',
			type: 'avg'
		},
		min_amount: {
			sql: 'amount',
			type: 'min'
		},
		max_amount: {
			sql: 'amount',
			type: 'max'
		},
		runningTotal_amount: {
			sql: 'amount',
			type: 'runningTotal'
		},
		count_amount: {
			sql: 'amount',
			type: 'count'
		},
		countDistinct_amount: {
			sql: 'amount',
			type: 'countDistinct'
		},
		countDistinctApprox_amount: {
			sql: 'amount',
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
		count_process_instance_id: {
			sql: 'process_instance_id',
			type: 'count'
		},
		countDistinct_process_instance_id: {
			sql: 'process_instance_id',
			type: 'countDistinct'
		},
		countDistinctApprox_process_instance_id: {
			sql: 'process_instance_id',
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

  },
  
  dimensions: {
    id: {
      sql: `id`,
      type: `number`,
      primaryKey: true
    },
	amount: {
      sql: `amount`,
      type: `number`
    },
    processInstanceId: {
      sql: `process_instance_id`,
      type: `string`
    },
    
    status: {
      sql: `status`,
      type: `string`
    }
  }
});
