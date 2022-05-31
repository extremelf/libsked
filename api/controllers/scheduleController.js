const firebase = require('../db.js');
const Schedule = require('../models/schedule');
const firestore = firebase.firestore();


const addSchedule = async (req, res, next) => {
    try {
        const data = req.body;
        await firestore.collection('schedule').doc().set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAll = async (req, res, next) => {
    try {
        const schedules = await firestore.collection('schedule');
        const data = await schedules.get();
        const schedulesArray = [];
        if(data.empty) {
            res.status(404).send('No schedule record found');
        }else {
            data.forEach(doc => {
                const schedule = new Schedule(
                    doc.id,
                    doc.data().name,
                    doc.data().room_number,
                    doc.data().chairs_number,
                    doc.data().sockets_number,
                    doc.data().description
                );
                schedulesArray.push(schedule);
            });
            res.send(schedulesArray);
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        const schedule = await firestore.collection('schedule').doc(id);
        const data = await schedule.get();
        if(!data.exists) {
            res.status(404).send('Schedule with the given ID not found');
        }else {
            res.send(data.data());
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const updateSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const schedule =  await firestore.collection('schedule').doc(id);
        await schedule.update(data);
        res.send('Schedule record updated successfuly');        
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        await firestore.collection('schedule').doc(id).delete();
        res.send('Record deleted successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    addSchedule,
    getAll,
    getSchedule,
    updateSchedule,
    deleteSchedule
}