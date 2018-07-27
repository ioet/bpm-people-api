'use strict';

import express from 'express';
import generateSafeId from 'generate-safe-id';
import dynamoose  from 'dynamoose';
import bodyParser from 'body-parser'; 
import Person from './../domain/Person'

let router = express.Router();

const peopleRoutes = (app) => {

    app.use(bodyParser.json())

    router
        .get('/', (req, res) => {
            Person.scan().exec(function (err, data) {
                if(err) {
                    return res.status(500).send({message: 'Doh...'});
                } else {
                    res.status(200).send(data);
                }
            });
        });

    router
        .post('/', (req, res) => {
            
            let newPerson = new Person({
                guid: generateSafeId(), 
                display_name: req.body.name, 
                authentication_identity: req.body.authentication_identity
            });
            
            newPerson.save(function (err) {
                if(err) {
                    return res.status(500).send({message: 'Doh...'});
                } else {
                    res.status(201).send(newPerson);
                }
            });
        });

    router
        .delete('/:personId', (req, res) => {

            Person.delete({guid: req.params.personId}, function(err) {
                if(err) {
                    return res.status(500).send({message: 'Doh...'});
                } else {
                    res.status(200).send();
                }
            });
        });    
 
    app.use('/people', router);
};
 
module.exports = peopleRoutes;
