const express = require('express');
const {addRoom, 
       getAll, 
       getRoom,
       updateRoom,
       deleteRoom,
       scheduleByRoom
      } = require('../controllers/roomController');

const router = express.Router();

router.post('/room', addRoom);
router.get('/room', getAll);
router.get('/room/:id', getRoom);
router.put('/room/:id', updateRoom);
router.delete('/room/:id', deleteRoom);
router.get('/room/schedules/:id', scheduleByRoom);


module.exports = {
    routes: router
}