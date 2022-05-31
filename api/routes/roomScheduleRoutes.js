const express = require('express');
const { addRoomSchedule,
    getAll,
    getRoomSchedule,
    updateRoomSchedule,
    deleteRoomSchedule
      } = require('../controllers/roomScheduleController');

const router = express.Router();

router.post('/roomSchedule', addRoomSchedule);
router.get('/roomSchedule', getAll);
router.get('/roomSchedule/:id', getRoomSchedule);
router.put('/roomSchedule/:id', updateRoomSchedule);
router.delete('/roomSchedule/:id', deleteRoomSchedule);


module.exports = {
    routes: router
}