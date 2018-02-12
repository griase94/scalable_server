# Routes
## create party
Methode: PUT

URL: localhost:5000/party

Body: String mit namen der Party 

Response: JSON mit (id: String,
                 name: String,
                 password:String,
                 createdAt: LocalDateTime)

## add song to party
Methode: PUT

URL: localhost:5000/party/PARTY_ID

Body: Party(streamingServiceID: String,
                     name: String,
                     artist: String,
                     album: String,
                     albumCoverUrl: String)
                     
Response: SongReturn(id:Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String,
                upvotes: Int,
                downvotes: Int,
                played: Boolean)
                
Wichtig: header "Content-Type" muss den Wert "application/json" haben
                
## get songs for party
Methode: GET

URL: localhost:5000/party/PARTY_ID

                     
Response: liste aus SongReturn(id:Long,
                streamingServiceID: String,
                name: String,
                artist: String,
                album: String,
                albumCoverUrl: String,
                upvotes: Int,
                downvotes: Int,
                played: Boolean)
                
## vote song  for party
Methode: POST

URL: localhost:5000/party/vote

Body: PartyVote(partyID: String,
                     songID: Long,
                     positive:Boolean) //positive = true wenn upvote, false wenn downvote

                     
Response: 1 wenn geklappt, ansonsten 0 (wenn kein Song für die Daten existiert) oder ein Fehler

Wichtig: header "Content-Type" muss den Wert "application/json" haben
                
## Song auf played setzen
Methode: POST

URL: localhost:5000/party/song/

Body: SongPlayed(id:Long,
                      partyID:String)
                     
Response: 1 wenn geklappt, ansonsten 0 (wenn kein Song für die Daten existiert) oder ein Fehler

Wichtig: header "Content-Type" muss den Wert "application/json" haben
                
                
