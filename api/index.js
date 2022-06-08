const express = require('express');
const cors = require('cors');
const bodyParser = require('body-parser');
const config = require('./config');
const roomRoutes = require('./routes/roomRoutes');
const scheduleRoomRoutes = require('./routes/roomScheduleRoutes');
const personRoutes = require('./routes/personRoutes');


const app = express();

app.use(express.json());
app.use(cors());
app.use(bodyParser.json());

app.use('/api',roomRoutes.routes);
app.use('/api',scheduleRoomRoutes.routes);
app.use('/api',personRoutes.routes);




app.listen(config.port, () => console.log('App is listening on url http://localhost:' + config.port));