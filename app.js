'use strict';

import express from 'express'
import bodyParser from 'body-parser';

let app = express();

app.use(function(req, res, next) {
    res.header("Access-Control-Allow-Origin", "*");
    res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
    next();
}).use(bodyParser.json());
 
require('./manage-people/routes/PeopleRoutes')(app);
 
app.listen(3000, () => {
    console.log('Server up!');
});