cube(`Owners`, {
	sql: `SELECT * FROM sample.owners`,

	joins: {

	},

	measures: {
		count_address: {
			sql: 'address',
			type: 'count'
		},
		countDistinct_address: {
			sql: 'address',
			type: 'countDistinct'
		},
		countDistinctApprox_address: {
			sql: 'address',
			type: 'countDistinctApprox'
		},
		count_city: {
			sql: 'city',
			type: 'count'
		},
		countDistinct_city: {
			sql: 'city',
			type: 'countDistinct'
		},
		countDistinctApprox_city: {
			sql: 'city',
			type: 'countDistinctApprox'
		},
		sum_user_id: {
			sql: 'user_id',
			type: 'sum'
		},
		avg_user_id: {
			sql: 'user_id',
			type: 'avg'
		},
		min_user_id: {
			sql: 'user_id',
			type: 'min'
		},
		max_user_id: {
			sql: 'user_id',
			type: 'max'
		},
		runningTotal_user_id: {
			sql: 'user_id',
			type: 'runningTotal'
		},
		count_user_id: {
			sql: 'user_id',
			type: 'count'
		},
		countDistinct_user_id: {
			sql: 'user_id',
			type: 'countDistinct'
		},
		countDistinctApprox_user_id: {
			sql: 'user_id',
			type: 'countDistinctApprox'
		},
,

	},

	dimensions: {
		address: {
			sql: `address`,
			type: `string`
		},

		city: {
			sql: `city`,
			type: `string`
		},

		id: {
			sql: `user_id`,
			type: `number`,
			primaryKey: true
		},
	}
});
