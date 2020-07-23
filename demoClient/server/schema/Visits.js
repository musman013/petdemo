cube(`Visits`, {
  sql: `SELECT * FROM sample.visits`,
  
  joins: {
    Pets: {
      sql: `${CUBE}.pet_id = ${Pets}.id`,
      relationship: `belongsTo`
    },
    Vets: {
      sql: `${CUBE}.vet_id = ${Vets}.id`,
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
		count_visit_notes: {
			sql: 'visit_notes',
			type: 'count'
		},
		countDistinct_visit_notes: {
			sql: 'visit_notes',
			type: 'countDistinct'
		},
		countDistinctApprox_visit_notes: {
			sql: 'visit_notes',
			type: 'countDistinctApprox'
		},
		min_visit_date: {
			sql: 'visit_date',
			type: 'min'
		},
		max_visit_date: {
			sql: 'visit_date',
			type: 'max'
		},
		count_visit_date: {
			sql: 'visit_date',
			type: 'count'
		},
		countDistinct_visit_date: {
			sql: 'visit_date',
			type: 'countDistinct'
		},
		countDistinctApprox_visit_date: {
			sql: 'visit_date',
			type: 'countDistinctApprox'
		},
,

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
    
    visitNotes: {
      sql: `visit_notes`,
      type: `string`
    },
    
    visitDate: {
      sql: `visit_date`,
      type: `time`
    }
  }
});
   
    visitDate: {
      sql: `visit_date`,
      type: `time`
    }
  }
});
