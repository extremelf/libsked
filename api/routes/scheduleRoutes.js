const express = require('express');
const {addSchedule, 
       getAll, 
       getSchedule,
       updateSchedule,
       deleteSchedule
      } = require('../controllers/scheduleController');

const router = express.Router();

router.post('/schedule', addSchedule);
router.get('/schedule', getAll);
router.get('/schedule/:id', getSchedule);
router.put('/schedule/:id', updateSchedule);
router.delete('/schedule/:id', deleteSchedule);


module.exports = {
    routes: router
}