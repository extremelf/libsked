const firebase = require('../db.js');
const Person = require('../models/person');
const RoomSchedule = require('../models/roomSchedule');
const firestore = firebase.firestore();


const addPerson = async (req, res, next) => {
    try {
        const data = req.body;
        await firestore.collection('person').doc().set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAll = async (req, res, next) => {
    try {
        const people = await firestore.collection('person');
        const data = await people.get();
        const personArray = [];
        if(data.empty) {
            res.status(404).send('No person record found');
        }else {
            data.forEach(doc => {
                const person = new Person(
                    doc.id,
                    doc.data().name,
                    doc.data().cc
                );
                personArray.push(person);
            });
            res.send(personArray);
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getPerson = async (req, res, next) => {
    try {
        const id = req.params.id;
        const person = await firestore.collection('person').doc(id);
        const data = await person.get();
        if(!data.exists) {
            res.status(404).send('Person with the given ID not found');
        }else {
            res.send(data.data());
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const updatePerson = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const person =  await firestore.collection('person').doc(id);
        await person.update(data);
        res.send('Person record updated successfuly');        
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deletePerson = async (req, res, next) => {
    try {
        const id = req.params.id;
        await firestore.collection('person').doc(id).delete();
        res.send('Record deleted successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const scheduleByPerson = async (req, res, next) => {
    try {
        const id = req.params.id;
        const person = await firestore.collection('person').doc(id);
        const data = await person.get();
        if(!data.exists) {
            res.status(404).send('Person with the given ID not found');
        }else {
            const personSchedules = await firestore.collection('roomSchedule').where('personID', '==', req.params.id);
            const data2 = await personSchedules.get();
            const personScheduleArray = [];
            if(data2.empty) {
                res.status(404).send('There are no schedules for that person!');
            }else {
                data2.forEach(doc => {
                    const personSchedule = new RoomSchedule(
                        doc.id,
                        doc.data().creation_timestamp,
                        doc.data().start_time,
                        doc.data().end_time,
                        doc.data().roomID,
                        doc.data().personID
                    );
                    personScheduleArray.push(personSchedule );
                });
                res.send(personScheduleArray);

                }
            }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    addPerson,
    getAll,
    getPerson,
    updatePerson,
    deletePerson,
    scheduleByPerson
}