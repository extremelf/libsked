const express = require('express');
const {addPerson, 
       getAll, 
       getPerson,
       updatePerson,
       deletePerson,
       scheduleByPerson
      } = require('../controllers/personController');

const router = express.Router();

router.post('/person', addPerson);
router.get('/person', getAll);
router.get('/person/:id', getPerson);
router.put('/person/:id', updatePerson);
router.delete('/person/:id', deletePerson);
router.get('/person/schedules/:id', scheduleByPerson);


module.exports = {
    routes: router
}