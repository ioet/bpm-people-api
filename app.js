'use strict';

import express from 'express'
import bodyParser from 'body-parser';

let app = express();
app.use(bodyParser.json());
 
require('./manage-people/routes/PeopleRoutes')(app);
 
app.listen(3000, () => {
    console.log('Server up!');
});