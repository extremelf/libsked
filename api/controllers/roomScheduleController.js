const firebase = require('../db.js');
const RoomSchedule = require('../models/roomSchedule');
const firestore = firebase.firestore();


const addRoomSchedule = async (req, res, next) => {
    try {
        const data = req.body;
        await firestore.collection('roomSchedule').doc().set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAll = async (req, res, next) => {
    try {
        const roomSchedules= await firestore.collection('roomSchedule');
        const data = await roomSchedules.get();
        const roomSchedulesArray = [];
        if(data.empty) {
            res.status(404).send('No room schedule record found');
        }else {
            data.forEach(doc => {
                const roomSchedule = new RoomSchedule(
                    doc.id,
                    doc.data().creation_timestamp,
                    doc.data().start_time,
                    doc.data().end_time,
                    doc.data().roomID,
                    doc.data().personID
                );
                roomSchedulesArray.push(roomSchedule);
            });
            res.send(roomSchedulesArray);
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getRoomSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        const roomSchedule = await firestore.collection('roomSchedule').doc(id);
        const data = await roomSchedule.get();
        if(!data.exists) {
            res.status(404).send('Room schedule with the given ID not found');
        }else {
            res.send(data.data());
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const updateRoomSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const roomSchedule =  await firestore.collection('roomSchedule').doc(id);
        await roomSchedule.update(data);
        res.send('Room Schedule record updated successfuly');        
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteRoomSchedule = async (req, res, next) => {
    try {
        const id = req.params.id;
        await firestore.collection('roomSchedule').doc(id).delete();
        res.send('Record deleted successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

module.exports = {
    addRoomSchedule,
    getAll,
    getRoomSchedule,
    updateRoomSchedule,
    deleteRoomSchedule
}