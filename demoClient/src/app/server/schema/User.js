cube(`User`, {
  sql: `SELECT * FROM sample.user`,
  
  joins: {
    
  },
  
  measures: {
		count_is_phone_number_confirmed: {
			sql: 'is_phone_number_confirmed',
			type: 'count'
		},
		countDistinct_is_phone_number_confirmed: {
			sql: 'is_phone_number_confirmed',
			type: 'countDistinct'
		},
		countDistinctApprox_is_phone_number_confirmed: {
			sql: 'is_phone_number_confirmed',
			type: 'countDistinctApprox'
		},
		count_two_factor_enabled: {
			sql: 'two_factor_enabled',
			type: 'count'
		},
		countDistinct_two_factor_enabled: {
			sql: 'two_factor_enabled',
			type: 'countDistinct'
		},
		countDistinctApprox_two_factor_enabled: {
			sql: 'two_factor_enabled',
			type: 'countDistinctApprox'
		},
		count_authentication_source: {
			sql: 'authentication_source',
			type: 'count'
		},
		countDistinct_authentication_source: {
			sql: 'authentication_source',
			type: 'countDistinct'
		},
		countDistinctApprox_authentication_source: {
			sql: 'authentication_source',
			type: 'countDistinctApprox'
		},
		count_email_address: {
			sql: 'email_address',
			type: 'count'
		},
		countDistinct_email_address: {
			sql: 'email_address',
			type: 'countDistinct'
		},
		countDistinctApprox_email_address: {
			sql: 'email_address',
			type: 'countDistinctApprox'
		},
		count_email_confirmation_code: {
			sql: 'email_confirmation_code',
			type: 'count'
		},
		countDistinct_email_confirmation_code: {
			sql: 'email_confirmation_code',
			type: 'countDistinct'
		},
		countDistinctApprox_email_confirmation_code: {
			sql: 'email_confirmation_code',
			type: 'countDistinctApprox'
		},
		count_first_name: {
			sql: 'first_name',
			type: 'count'
		},
		countDistinct_first_name: {
			sql: 'first_name',
			type: 'countDistinct'
		},
		countDistinctApprox_first_name: {
			sql: 'first_name',
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
		count_is_active: {
			sql: 'is_active',
			type: 'count'
		},
		countDistinct_is_active: {
			sql: 'is_active',
			type: 'countDistinct'
		},
		countDistinctApprox_is_active: {
			sql: 'is_active',
			type: 'countDistinctApprox'
		},
		count_is_email_confirmed: {
			sql: 'is_email_confirmed',
			type: 'count'
		},
		countDistinct_is_email_confirmed: {
			sql: 'is_email_confirmed',
			type: 'countDistinct'
		},
		countDistinctApprox_is_email_confirmed: {
			sql: 'is_email_confirmed',
			type: 'countDistinctApprox'
		},
		count_is_lockout_enabled: {
			sql: 'is_lockout_enabled',
			type: 'count'
		},
		countDistinct_is_lockout_enabled: {
			sql: 'is_lockout_enabled',
			type: 'countDistinct'
		},
		countDistinctApprox_is_lockout_enabled: {
			sql: 'is_lockout_enabled',
			type: 'countDistinctApprox'
		},
		count_last_name: {
			sql: 'last_name',
			type: 'count'
		},
		countDistinct_last_name: {
			sql: 'last_name',
			type: 'countDistinct'
		},
		countDistinctApprox_last_name: {
			sql: 'last_name',
			type: 'countDistinctApprox'
		},
		count_password: {
			sql: 'password',
			type: 'count'
		},
		countDistinct_password: {
			sql: 'password',
			type: 'countDistinct'
		},
		countDistinctApprox_password: {
			sql: 'password',
			type: 'countDistinctApprox'
		},
		count_password_reset_code: {
			sql: 'password_reset_code',
			type: 'count'
		},
		countDistinct_password_reset_code: {
			sql: 'password_reset_code',
			type: 'countDistinct'
		},
		countDistinctApprox_password_reset_code: {
			sql: 'password_reset_code',
			type: 'countDistinctApprox'
		},
		count_phone_number: {
			sql: 'phone_number',
			type: 'count'
		},
		countDistinct_phone_number: {
			sql: 'phone_number',
			type: 'countDistinct'
		},
		countDistinctApprox_phone_number: {
			sql: 'phone_number',
			type: 'countDistinctApprox'
		},
		count_tenant: {
			sql: 'tenant',
			type: 'count'
		},
		countDistinct_tenant: {
			sql: 'tenant',
			type: 'countDistinct'
		},
		countDistinctApprox_tenant: {
			sql: 'tenant',
			type: 'countDistinctApprox'
		},
		count_user_name: {
			sql: 'user_name',
			type: 'count'
		},
		countDistinct_user_name: {
			sql: 'user_name',
			type: 'countDistinct'
		},
		countDistinctApprox_user_name: {
			sql: 'user_name',
			type: 'countDistinctApprox'
		},
		min_last_login_time: {
			sql: 'last_login_time',
			type: 'min'
		},
		max_last_login_time: {
			sql: 'last_login_time',
			type: 'max'
		},
		count_last_login_time: {
			sql: 'last_login_time',
			type: 'count'
		},
		countDistinct_last_login_time: {
			sql: 'last_login_time',
			type: 'countDistinct'
		},
		countDistinctApprox_last_login_time: {
			sql: 'last_login_time',
			type: 'countDistinctApprox'
		},
		min_lockout_end_date_utc: {
			sql: 'lockout_end_date_utc',
			type: 'min'
		},
		max_lockout_end_date_utc: {
			sql: 'lockout_end_date_utc',
			type: 'max'
		},
		count_lockout_end_date_utc: {
			sql: 'lockout_end_date_utc',
			type: 'count'
		},
		countDistinct_lockout_end_date_utc: {
			sql: 'lockout_end_date_utc',
			type: 'countDistinct'
		},
		countDistinctApprox_lockout_end_date_utc: {
			sql: 'lockout_end_date_utc',
			type: 'countDistinctApprox'
		},
		min_password_token_expiration: {
			sql: 'password_token_expiration',
			type: 'min'
		},
		max_password_token_expiration: {
			sql: 'password_token_expiration',
			type: 'max'
		},
		count_password_token_expiration: {
			sql: 'password_token_expiration',
			type: 'count'
		},
		countDistinct_password_token_expiration: {
			sql: 'password_token_expiration',
			type: 'countDistinct'
		},
		countDistinctApprox_password_token_expiration: {
			sql: 'password_token_expiration',
			type: 'countDistinctApprox'
		},
  },
  
  dimensions: {
    isPhoneNumberConfirmed: {
      sql: `is_phone_number_confirmed`,
      type: `string`
    },
    
    twoFactorEnabled: {
      sql: `two_factor_enabled`,
      type: `string`
    },
    
    authenticationSource: {
      sql: `authentication_source`,
      type: `string`
    },
    
    emailAddress: {
      sql: `email_address`,
      type: `string`
    },
    
    emailConfirmationCode: {
      sql: `email_confirmation_code`,
      type: `string`
    },
    
    firstName: {
      sql: `first_name`,
      type: `string`
    },
    
    id: {
      sql: `id`,
      type: `number`,
      primaryKey: true
    },
    
    isActive: {
      sql: `is_active`,
      type: `string`
    },
    
    isEmailConfirmed: {
      sql: `is_email_confirmed`,
      type: `string`
    },
    
    isLockoutEnabled: {
      sql: `is_lockout_enabled`,
      type: `string`
    },
    
    lastName: {
      sql: `last_name`,
      type: `string`
    },
    
    password: {
      sql: `password`,
      type: `string`
    },
    
    passwordResetCode: {
      sql: `password_reset_code`,
      type: `string`
    },
    
    phoneNumber: {
      sql: `phone_number`,
      type: `string`
    },
    
    tenant: {
      sql: `tenant`,
      type: `string`
    },
    
    userName: {
      sql: `user_name`,
      type: `string`
    },
    
    lastLoginTime: {
      sql: `last_login_time`,
      type: `time`
    },
    
    lockoutEndDateUtc: {
      sql: `lockout_end_date_utc`,
      type: `time`
    },
    
    passwordTokenExpiration: {
      sql: `password_token_expiration`,
      type: `time`
    }
  }
});
