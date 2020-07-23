cube(`Pets`, {
  sql: `SELECT * FROM sample.pets`,
  
  joins: {
    Types: {
      sql: `${CUBE}.type_id = ${Types}.id`,
      relationship: `belongsTo`
    },
    Owners: {
      sql: `${CUBE}.owner_id = ${Owners}.id`,
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
		min_birth_date: {
			sql: 'birth_date',
			type: 'min'
		},
		max_birth_date: {
			sql: 'birth_date',
			type: 'max'
		},
		count_birth_date: {
			sql: 'birth_date',
			type: 'count'
		},
		countDistinct_birth_date: {
			sql: 'birth_date',
			type: 'countDistinct'
		},
		countDistinctApprox_birth_date: {
			sql: 'birth_date',
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
    
    name: {
      sql: `name`,
      type: `string`
    },
    
    birthDate: {
      sql: `birth_date`,
      type: `time`
    }
  }
});
     sql: `birth_date`,
      type: `time`
    }
  }
});
