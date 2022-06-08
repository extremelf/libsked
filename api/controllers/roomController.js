const firebase = require('../db.js');
const Room = require('../models/room');
const RoomSchedule = require('../models/roomSchedule');
const firestore = firebase.firestore();


const addRoom = async (req, res, next) => {
    try {
        const data = req.body;
        await firestore.collection('room').doc().set(data);
        res.send('Record saved successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getAll = async (req, res, next) => {
    try {
        const rooms= await firestore.collection('room');
        const data = await rooms.get();
        const roomArray = [];
        if(data.empty) {
            res.status(404).send('No room record found');
        }else {
            data.forEach(doc => {
                const room = new Room(
                    doc.id,
                    doc.data().name,
                    doc.data().room_number,
                    doc.data().chairs_number,
                    doc.data().sockets_number,
                    doc.data().description
                );
                roomArray.push(room);
            });
            res.send(roomArray);
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const getRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const room = await firestore.collection('room').doc(id);
        const data = await room.get();
        if(!data.exists) {
            res.status(404).send('Room with the given ID not found');
        }else {
            res.send(data.data());
        }
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const updateRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const data = req.body;
        const room =  await firestore.collection('room').doc(id);
        await room.update(data);
        res.send('Room record updated successfuly');        
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const deleteRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        await firestore.collection('room').doc(id).delete();
        res.send('Record deleted successfuly');
    } catch (error) {
        res.status(400).send(error.message);
    }
}

const scheduleByRoom = async (req, res, next) => {
    try {
        const id = req.params.id;
        const room = await firestore.collection('room').doc(id);
        const data = await room.get();
        if(!data.exists) {
            res.status(404).send('Room with the given ID not found');
        }else {
            const roomSchedules = await firestore.collection('roomSchedule').where('roomID', '==', req.params.id);
            const data2 = await roomSchedules.get();
            const roomScheduleArray = [];
            if(data2.empty) {
                res.status(404).send('There are no schedules for that room!');
            }else {
                data2.forEach(doc => {
                    const roomSchedule = new RoomSchedule(
                        doc.id,
                        doc.data().creation_timestamp,
                        doc.data().start_time,
                        doc.data().end_time,
                        doc.data().roomID,
                        doc.data().personID
                    );
                    roomScheduleArray.push(roomSchedule );
                });
                res.send(roomScheduleArray);

                }
            }
    } catch (error) {
        res.status(400).send(error.message);
    }
}


module.exports = {
    addRoom,
    getAll,
    getRoom,
    updateRoom,
    deleteRoom,
    scheduleByRoom
}