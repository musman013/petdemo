cube(`VetSpecialties`, {
  sql: `SELECT * FROM sample.vet_specialties`,
  
  joins: {
    Specialties: {
      sql: `${CUBE}.specialty_id = ${Specialties}.id`,
      relationship: `belongsTo`
    },
    Vets: {
      sql: `${CUBE}.vet_id = ${Vets}.id`,
      relationship: `belongsTo`
    }
  },
  
  measures: {

  },
  
  dimensions: {
    id: {
      sql: `${CUBE}.vet_id || '-' || ${CUBE}.specialty_id`,
      type: `string`,
      primaryKey: true
    }
  }
});
