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
