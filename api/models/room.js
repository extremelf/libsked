class Room {
    constructor(id, name, room_number, chairs_number,sockets_number,description ) {
            this.id = id;
            this.name = name;
            this.room_number = room_number;
            this.chairs_number = chairs_number;
            this.sockets_number = sockets_number;
            this.description = description;
    }
}

module.exports = Room;