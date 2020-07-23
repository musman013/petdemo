cube(`PgAggregate`, {
  sql: `SELECT * FROM pg_catalog.pg_aggregate`,
  
  joins: {
    
  },
  
  measures: {
		count_aggmfinalfn: {
			sql: 'aggmfinalfn',
			type: 'count'
		},
		countDistinct_aggmfinalfn: {
			sql: 'aggmfinalfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggmfinalfn: {
			sql: 'aggmfinalfn',
			type: 'countDistinctApprox'
		},
		count_aggmtransfn: {
			sql: 'aggmtransfn',
			type: 'count'
		},
		countDistinct_aggmtransfn: {
			sql: 'aggmtransfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggmtransfn: {
			sql: 'aggmtransfn',
			type: 'countDistinctApprox'
		},
		count_aggtransfn: {
			sql: 'aggtransfn',
			type: 'count'
		},
		countDistinct_aggtransfn: {
			sql: 'aggtransfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggtransfn: {
			sql: 'aggtransfn',
			type: 'countDistinctApprox'
		},
		count_aggfinalfn: {
			sql: 'aggfinalfn',
			type: 'count'
		},
		countDistinct_aggfinalfn: {
			sql: 'aggfinalfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggfinalfn: {
			sql: 'aggfinalfn',
			type: 'countDistinctApprox'
		},
		count_aggkind: {
			sql: 'aggkind',
			type: 'count'
		},
		countDistinct_aggkind: {
			sql: 'aggkind',
			type: 'countDistinct'
		},
		countDistinctApprox_aggkind: {
			sql: 'aggkind',
			type: 'countDistinctApprox'
		},
		count_aggcombinefn: {
			sql: 'aggcombinefn',
			type: 'count'
		},
		countDistinct_aggcombinefn: {
			sql: 'aggcombinefn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggcombinefn: {
			sql: 'aggcombinefn',
			type: 'countDistinctApprox'
		},
		count_aggfnoid: {
			sql: 'aggfnoid',
			type: 'count'
		},
		countDistinct_aggfnoid: {
			sql: 'aggfnoid',
			type: 'countDistinct'
		},
		countDistinctApprox_aggfnoid: {
			sql: 'aggfnoid',
			type: 'countDistinctApprox'
		},
		count_aggserialfn: {
			sql: 'aggserialfn',
			type: 'count'
		},
		countDistinct_aggserialfn: {
			sql: 'aggserialfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggserialfn: {
			sql: 'aggserialfn',
			type: 'countDistinctApprox'
		},
		count_aggdeserialfn: {
			sql: 'aggdeserialfn',
			type: 'count'
		},
		countDistinct_aggdeserialfn: {
			sql: 'aggdeserialfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggdeserialfn: {
			sql: 'aggdeserialfn',
			type: 'countDistinctApprox'
		},
		count_aggminvtransfn: {
			sql: 'aggminvtransfn',
			type: 'count'
		},
		countDistinct_aggminvtransfn: {
			sql: 'aggminvtransfn',
			type: 'countDistinct'
		},
		countDistinctApprox_aggminvtransfn: {
			sql: 'aggminvtransfn',
			type: 'countDistinctApprox'
		},
		count_aggfinalextra: {
			sql: 'aggfinalextra',
			type: 'count'
		},
		countDistinct_aggfinalextra: {
			sql: 'aggfinalextra',
			type: 'countDistinct'
		},
		countDistinctApprox_aggfinalextra: {
			sql: 'aggfinalextra',
			type: 'countDistinctApprox'
		},
		count_aggmfinalextra: {
			sql: 'aggmfinalextra',
			type: 'count'
		},
		countDistinct_aggmfinalextra: {
			sql: 'aggmfinalextra',
			type: 'countDistinct'
		},
		countDistinctApprox_aggmfinalextra: {
			sql: 'aggmfinalextra',
			type: 'countDistinctApprox'
		},
		count_aggsortop: {
			sql: 'aggsortop',
			type: 'count'
		},
		countDistinct_aggsortop: {
			sql: 'aggsortop',
			type: 'countDistinct'
		},
		countDistinctApprox_aggsortop: {
			sql: 'aggsortop',
			type: 'countDistinctApprox'
		},
		count_aggtranstype: {
			sql: 'aggtranstype',
			type: 'count'
		},
		countDistinct_aggtranstype: {
			sql: 'aggtranstype',
			type: 'countDistinct'
		},
		countDistinctApprox_aggtranstype: {
			sql: 'aggtranstype',
			type: 'countDistinctApprox'
		},
		count_aggmtranstype: {
			sql: 'aggmtranstype',
			type: 'count'
		},
		countDistinct_aggmtranstype: {
			sql: 'aggmtranstype',
			type: 'countDistinct'
		},
		countDistinctApprox_aggmtranstype: {
			sql: 'aggmtranstype',
			type: 'countDistinctApprox'
		},
		count_agginitval: {
			sql: 'agginitval',
			type: 'count'
		},
		countDistinct_agginitval: {
			sql: 'agginitval',
			type: 'countDistinct'
		},
		countDistinctApprox_agginitval: {
			sql: 'agginitval',
			type: 'countDistinctApprox'
		},
		count_aggminitval: {
			sql: 'aggminitval',
			type: 'count'
		},
		countDistinct_aggminitval: {
			sql: 'aggminitval',
			type: 'countDistinct'
		},
		countDistinctApprox_aggminitval: {
			sql: 'aggminitval',
			type: 'countDistinctApprox'
		},

  },
  
  dimensions: {
    aggmfinalfn: {
      sql: `aggmfinalfn`,
      type: `string`
    },
    
    aggmtransfn: {
      sql: `aggmtransfn`,
      type: `string`
    },
    
    aggtransfn: {
      sql: `aggtransfn`,
      type: `string`
    },
    
    aggfinalfn: {
      sql: `aggfinalfn`,
      type: `string`
    },
    
    aggkind: {
      sql: `aggkind`,
      type: `string`
    },
    
    aggcombinefn: {
      sql: `aggcombinefn`,
      type: `string`
    },
    
    aggfnoid: {
      sql: `aggfnoid`,
      type: `string`
    },
    
    aggserialfn: {
      sql: `aggserialfn`,
      type: `string`
    },
    
    aggdeserialfn: {
      sql: `aggdeserialfn`,
      type: `string`
    },
    
    aggminvtransfn: {
      sql: `aggminvtransfn`,
      type: `string`
    },
    
    aggfinalextra: {
      sql: `aggfinalextra`,
      type: `string`
    },
    
    aggmfinalextra: {
      sql: `aggmfinalextra`,
      type: `string`
    },
    
    aggsortop: {
      sql: `aggsortop`,
      type: `string`
    },
    
    aggtranstype: {
      sql: `aggtranstype`,
      type: `string`
    },
    
    aggmtranstype: {
      sql: `aggmtranstype`,
      type: `string`
    },
    
    agginitval: {
      sql: `agginitval`,
      type: `string`
    },
    
    aggminitval: {
      sql: `aggminitval`,
      type: `string`
    }
  }
});
