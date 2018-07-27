'use strict';

import dynamoose from 'dynamoose';

dynamoose.setDefaults( { create: true, prefix: 'people_' });  

const PeopleSchema = { guid: String, display_name: String, authentication_identity: String };
const Person = dynamoose.model('person', PeopleSchema);

export { Person as default }
