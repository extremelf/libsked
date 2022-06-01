class roomSchedule {
    constructor(id, creation_timestamp, start_time, end_time,roomID,personID ) {
            this.id = id;
            this.creation_timestamp= creation_timestamp;
            this.start_time = start_time;
            this.end_time = end_time;
            this.roomID = roomID;
            this.personID = personID;
    }
}

module.exports = roomSchedule;